package com.server;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class WebServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final InetAddress hostInetAddress;
    private final ServerSocket serverSocket;
    private final QueuedThreadPool threadPool;


    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        this.config = webServerConfig;
        this.isRunning = new AtomicBoolean(true);
        this.hostInetAddress = InetAddress.getByName(this.config.getHost());
        this.serverSocket = new ServerSocket(this.config.getPort(), this.config.getBacklogSize(),
                this.hostInetAddress);
        this.threadPool = new QueuedThreadPool(this.config.getNbPoolThreads());
    }

    public static WebServer start(final WebServerConfig webServerConfig) {
        WebServer webServer = null;
        try {
            webServer = new WebServer(webServerConfig);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        final Thread mainServerThread = new Thread(webServer);
        mainServerThread.start();
        return webServer;
    }

    public synchronized void stop() {
        this.isRunning.set(false);
        try {
            this.serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void run() {
        while (isRunning.get()) {
            try (final Socket clientSocket = this.serverSocket.accept()) {
                final RequestHandler requestHandler = new RequestHandler(clientSocket, this.config);
                requestHandler.handle();
            } catch (IOException ioException) {
                if (!this.isRunning.get()) {
                    // LOGGER.log(Level.INFO, "Server shutdown.");
                    break;
                }
                ioException.printStackTrace();
            }
        }
    }
}
