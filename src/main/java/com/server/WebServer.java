package com.server;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.Optional;

public class WebServer implements Runnable {

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final InetAddress hostInetAddress;
    private final ServerSocket serverSocket;
    private final CountDownLatch isRunningLatch;
    private final ExecutorService serverThreadPool;
    // private final List<WebServerWorker> serverThreadPool;

    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        config = webServerConfig;
        isRunning = new AtomicBoolean(true);
        hostInetAddress = InetAddress.getByName(config.getHost());
        serverSocket = new ServerSocket(config.getPort(), config.getBacklogSize(), hostInetAddress);
        isRunningLatch = new CountDownLatch(1);
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
            return Optional.of(webServer);
        }

        return Optional.empty();
    }

    public synchronized void stop() {
        isRunning.set(false);
        isRunningLatch.countDown();
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ServerLogger.log(Level.WARNING, ioException.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            IntStream.range(0, config.getNbPoolThreads()).boxed()
                    .forEach(index -> serverThreadPool.execute(new WebServerWorker(config, isRunning, serverSocket)));
            isRunningLatch.await();
            if (!serverThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                serverThreadPool.shutdownNow();
                if (!serverThreadPool.awaitTermination(60, TimeUnit.SECONDS))
                    ServerLogger.log(Level.WARNING, "Server thread pool has dangling threads.");
            }
        } catch (InterruptedException iException) {
            if (isRunning.get()) {
                ServerLogger.log(Level.DEBUG, iException.getMessage());
            } else {
                ServerLogger.log(Level.WARNING, "Forcing Threads to shutdown.");
                serverThreadPool.shutdownNow();
            }
        }
        if (serverThreadPool.isTerminated()) {
            ServerLogger.log(Level.INFO, "Server thread pool shutdown.");
        } else {
            ServerLogger.log(Level.INFO, "Server thread pool shutdown failed; dangling threads remain.");
        }
    }
}
