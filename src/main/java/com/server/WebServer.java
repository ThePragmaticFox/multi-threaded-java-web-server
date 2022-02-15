package com.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class WebServer implements Runnable {

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final InetAddress hostInetAddress;
    private final ServerSocket serverSocket;
    private final QueuedThreadPool threadPool;
    private final HTTPResponseHandler responseHandler;


    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        this.config = webServerConfig;
        this.isRunning = new AtomicBoolean(true);
        this.hostInetAddress = InetAddress.getByName(this.config.getHost());
        this.serverSocket = new ServerSocket(this.config.getPort(), this.config.getBacklogSize(),
                this.hostInetAddress);
        this.threadPool = new QueuedThreadPool(this.config.getNbPoolThreads());
        this.responseHandler = new HTTPResponseHandler();
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
                final RequestHandler requestHandler =
                        new RequestHandler(clientSocket, this.config, responseHandler);
                requestHandler.handle();
            } catch (IOException ioException) {
                if (!this.isRunning.get()) {
                    System.out.println("Server shutdown");
                    break;
                }
                ioException.printStackTrace();
            }
        }
    }
}
