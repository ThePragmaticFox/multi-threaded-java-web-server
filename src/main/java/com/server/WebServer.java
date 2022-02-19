package com.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Optional;

public class WebServer implements Runnable {

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final InetAddress hostInetAddress;
    private final ServerSocket serverSocket;
    private final ExecutorService serverThreadPool;

    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        config = webServerConfig;
        isRunning = new AtomicBoolean(true);
        hostInetAddress = InetAddress.getByName(config.getHost());
        serverSocket = new ServerSocket(config.getPort(), config.getBacklogSize(), hostInetAddress);
        serverThreadPool = Executors.newFixedThreadPool(config.getNbPoolThreads());
    }

    public static Optional<WebServer> start(final WebServerConfig webServerConfig) {
        WebServer webServer = null;
        try {
            webServer = new WebServer(webServerConfig);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (webServer != null) {
            final Thread mainServerThread = new Thread(webServer);
            mainServerThread.start();
            return Optional.of(webServer);
        }
        return Optional.empty();
    }

    public synchronized void stop() {
        isRunning.set(false);
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void run() {
        while (isRunning.get()) {
            try {
                serverThreadPool.execute(new WebServerWorker(serverSocket.accept(), config));
            } catch (IOException ioException) {
                if (!isRunning.get()) {
                    System.out.println("Server shutdown.");
                    break;
                }
                ioException.printStackTrace();
            }
        }
    }
}
