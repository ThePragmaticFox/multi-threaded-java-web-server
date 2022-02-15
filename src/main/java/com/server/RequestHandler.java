package com.server;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RequestHandler {

    private final Socket clientSocket;
    private final WebServerConfig webServerConfig;
    private final HTTPResponseHandler responseHandler;

    public RequestHandler(final Socket clientSocket, final WebServerConfig webServerConfig,
            final HTTPResponseHandler responseHandler) {
        this.clientSocket = clientSocket;
        this.webServerConfig = webServerConfig;
        this.responseHandler = responseHandler;
    }

    public void handle() {
        try {
            final BufferedReader inputStream =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter outputStream = new PrintWriter(stringWriter);

            final List<String> headerLines = new ArrayList<>();
            String inputLine = inputStream.readLine();
            while (inputLine.length() > 0) {
                System.out.println(inputLine);
                headerLines.add(inputLine);
                inputLine = inputStream.readLine();
            }

            // We only care about the header, the body is simply ignored

            parseHeader(headerLines).ifPresentOrElse(header -> {
                if (!handleResponse(header, outputStream)) {
                    outputStream.print("HTTP/1.1 500 Internal Server Error" + "\n");
                    outputStream.print("Connection: close" + "\r\n");
                }
            }, () -> {
                outputStream.print("HTTP/1.1 400 Bad Request" + "\n");
                outputStream.print("Connection: close" + "\r\n");
            });

            final String outputString = stringWriter.toString();

            clientSocket.getOutputStream().write(outputString.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            clientSocket.getOutputStream().close();
            inputStream.close();
            clientSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private boolean handleResponse(final HTTPHeader header, final PrintWriter outputStream) {
        switch (header.getVersion()) {
            case HTTP_1_1:
                return responseHandler.getResponse(HTTPMethod.parseMethod(header, outputStream));
            case UNKNOWN:
                outputStream.print("HTTP/1.1 505 HTTP Version Not Supported" + "\n");
                outputStream.print("Connection: close" + "\r\n");
                return true;
            default:
                throw new IllegalStateException(header.getVersion() + " has not been implented");
        }
    }

    private Optional<HTTPHeader> parseHeader(final List<String> headerLines) {
        if (headerLines.size() < 1) {
            return Optional.empty();
        }
        // Only interested in the first line, containing method, path and version
        final String[] contents = Arrays.stream(headerLines.get(0).split(" "))
                .map(content -> content.trim()).toArray(String[]::new);
        if (contents.length != 3) {
            return Optional.empty();
        }
        final String method = contents[0];
        final String filePath = contents[1].contains(".") ? contents[1]
                : (contents[1] + "/index.html").replace("//", "/");
        final Path path = Paths.get(webServerConfig.getRoot() + filePath);
        final HTTPVersion version =
                "HTTP/1.1".equals(contents[2]) ? HTTPVersion.HTTP_1_1 : HTTPVersion.UNKNOWN;
        final HTTPHeader header = new HTTPHeader(path, method, version);
        return Optional.of(header);
    }
}
