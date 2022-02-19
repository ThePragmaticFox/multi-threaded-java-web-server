package com.server;

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
        RequestHandler.handle(clientSocket, config);
    }
}
