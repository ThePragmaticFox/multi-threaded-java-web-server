package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.System.Logger.Level;
import com.server.HTTP.RequestHandler;

public class WebServerWorker implements Runnable {

    private final WebServerConfig config;
    private final AtomicBoolean isRunning;
    private final ServerSocket serverSocket;

    public WebServerWorker(final WebServerConfig config, final AtomicBoolean isRunning,
            final ServerSocket serverSocket) {
        this.config = config;
        this.isRunning = isRunning;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try (Socket clientSocket = serverSocket.accept()) {
                RequestHandler.handle(clientSocket, config);
            } catch (IOException ioException) {
                if (!isRunning.get()) {
                    final long threadId = Thread.currentThread().getId();
                    final String threadName = Thread.currentThread().getName();
                    ServerLogger.log(Level.INFO, "Server worker <<" + threadId + ", " + threadName + ">> shutdown.");
                }
                ServerLogger.log(Level.DEBUG, ioException.getMessage());
            }
        }
    }
}
