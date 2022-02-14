package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler {

    private final Socket clientSocket;
    private final WebServerConfig webServerConfig;

    public RequestHandler(final Socket clientSocket, final WebServerConfig webServerConfig) {
        this.clientSocket = clientSocket;
        this.webServerConfig = webServerConfig;
    }

    public void handle() {
        try {
            final InputStream inputStream = clientSocket.getInputStream();
            final OutputStream outputStream = clientSocket.getOutputStream();

            byte[] messageByte = new byte[10];
            String dataString = "";

            int bytesRead = 0;
            int totalBytesRead = 0;
            do {
                bytesRead = inputStream.read(messageByte);
                totalBytesRead += bytesRead;
                dataString += new String(messageByte, 0, bytesRead);
            } while (totalBytesRead <= webServerConfig.getMaxReqBytes() && inputStream.available() != 0);

            if (totalBytesRead > webServerConfig.getMaxReqBytes()) {
                outputStream.write(("HTTP/1.1 400 Bad Request\n\n").getBytes());
                return;
            }

            System.out.println("MESSAGE: " + dataString);

            outputStream.write(("HTTP/1.1 200 OK\n\n").getBytes());
            outputStream.write(("This Is A Test.").getBytes());

            outputStream.close();
            System.out.println("Request processed: " + System.currentTimeMillis());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
