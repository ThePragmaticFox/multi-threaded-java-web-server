package com.server;

import java.io.IOException;
import java.util.logging.Level;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Optional;

public class WebServer implements Runnable {

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final ServerSocket serverSocket;
    private final ExecutorService serverThreadPool;

    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        config = webServerConfig;
        isRunning = new AtomicBoolean(true);
        serverSocket =
                new ServerSocket(config.getPort(), config.getBacklogSize(), InetAddress.getByName(config.getHost()));
        serverThreadPool = Executors.newFixedThreadPool(config.getNbPoolThreads());
    }

    public static Optional<WebServer> start(final WebServerConfig webServerConfig) {

        WebServer webServer = null;

        try {
            webServer = new WebServer(webServerConfig);
        } catch (IOException ioException) {
            ServerLogger.log(Level.WARNING, ioException.getMessage());
        }

        if (webServer != null) {
            final Thread mainServerThread = new Thread(webServer);
            mainServerThread.start();
            ServerLogger.log(Level.INFO, "Server started.");
            return Optional.of(webServer);
        }

        return Optional.empty();
    }

    public synchronized void stop() {
        isRunning.set(false);
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ServerLogger.log(Level.WARNING, ioException.getMessage());
        }
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try {
                serverThreadPool.execute(new WebServerWorker(config, serverSocket.accept()));
            } catch (IOException ioException) {
                if (isRunning.get()) {
                    ServerLogger.log(Level.SEVERE, ioException.getMessage());
                } else {
                    serverShutdown();
                }
            }
        }
    }

    private synchronized void serverShutdown() {
        try {
            serverThreadPool.shutdown();
            serverThreadPool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            ServerLogger.log(Level.INFO, e1.getMessage());
            serverThreadPool.shutdownNow();
        }
        ServerLogger.log(Level.INFO, "Server shutdown.");
    }
}
