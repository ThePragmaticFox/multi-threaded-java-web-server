package com.server;

import java.util.logging.Level;
import java.net.Socket;
import com.server.HTTP.RequestHandler;

public class WebServerWorker implements Runnable {

    private final Socket clientSocket;
    private final WebServerConfig config;

    public WebServerWorker(final WebServerConfig config, final Socket clientSocket) {
        this.config = config;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        ServerLogger.logThreadStart(Level.FINER);
        RequestHandler.handle(clientSocket, config);
        ServerLogger.logThreadStop(Level.FINER);
    }
}
