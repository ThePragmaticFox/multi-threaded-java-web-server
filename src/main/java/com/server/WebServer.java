package com.server;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Iterator;
import java.util.Optional;

public class WebServer implements Runnable {

    private static final int BUFFER_SIZE = 1024;
    private final static int DEFAULT_PORT = 9090;

    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final ExecutorService serverThreadPool;
    private final Selector selector;
    // private final List<WebServerWorker> serverThreadPool;

    public WebServer(final WebServerConfig webServerConfig) throws IOException {
        config = webServerConfig;
        isRunning = new AtomicBoolean(true);
        selector = initSelector();
        serverThreadPool = Executors.newFixedThreadPool(config.getNbPoolThreads());
    }

    private Selector initSelector() throws IOException {
        final Selector socketSelector = SelectorProvider.provider().openSelector();
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(config.getHost(), config.getPort()));
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
        return socketSelector;
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
        try {
            selector.close();
        } catch (IOException ioException) {
            ServerLogger.log(Level.WARNING, ioException.getMessage());
        }
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try {
                selector.select();
                final Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    final SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        final SocketChannel socketChannel = serverSocketChannel.accept();
                        //socketChannel.configureBlocking(false);
                        ServerLogger.log(Level.DEBUG, "Client is connected.");
                        serverThreadPool.execute(new WebServerWorker(config, socketChannel.socket()));
                    }
                }
            } catch (Exception e) {
                if (isRunning.get()) {
                    ServerLogger.log(Level.ERROR, e.getMessage());
                } else {
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
        }
    }
}
