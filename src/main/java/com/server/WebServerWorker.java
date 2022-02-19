package com.server;

import java.io.IOException;
import java.net.Socket;
import com.server.HTTP.RequestHandler;

public class WebServerWorker implements Runnable {

    private final Socket clientSocket;
    private final WebServerConfig config;

    public WebServerWorker(final Socket clientSocket, final WebServerConfig config) {
        this.clientSocket = clientSocket;
        this.config = config;
    }

    @Override
    public void run() {
        RequestHandler.handle(clientSocket, config);
        try {
            clientSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
