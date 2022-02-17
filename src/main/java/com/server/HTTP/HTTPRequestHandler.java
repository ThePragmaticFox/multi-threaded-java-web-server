package com.server.HTTP;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.server.WebServerConfig;

public class HTTPRequestHandler {

    private final Socket clientSocket;
    private final WebServerConfig webServerConfig;

    public HTTPRequestHandler(final Socket clientSocket, final WebServerConfig webServerConfig) {
        this.clientSocket = clientSocket;
        this.webServerConfig = webServerConfig;
    }

    public void handle() {
        try {
            final BufferedReader inputStream =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final HTTPOutputStream outputStream =
                    new HTTPOutputStream(clientSocket.getOutputStream());

            final List<String> headerLines = new ArrayList<>();
            String inputLine = inputStream.readLine();
            while (inputLine.length() > 0) {
                headerLines.add(inputLine);
                inputLine = inputStream.readLine();
            }

            // We only care about the header, the body is simply ignored

            parseHeader(headerLines).ifPresentOrElse(header -> {
                try {
                    if (!handleResponse(header, outputStream)) {
                        outputStream.write("HTTP/1.1 500 Internal Server Error".getBytes());
                        outputStream.write("\n".getBytes());
                        outputStream.write("Connection: close".getBytes());
                        outputStream.write("\r\n".getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }, () -> {
                try {
                    outputStream.write("HTTP/1.1 400 Bad Request".getBytes());
                    outputStream.write("\n".getBytes());
                    outputStream.write("Connection: close".getBytes());
                    outputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private boolean handleResponse(final HTTPHeader header, final HTTPOutputStream outputStream)
            throws IOException {
        switch (header.getVersion()) {
            case HTTP_1_1:
                return HTTPResponseHandler.getResponse(header, outputStream);
            case UNKNOWN:
                outputStream.write("HTTP/1.1 505 HTTP Version Not Supported".getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write("Connection: close".getBytes());
                outputStream.write("\r\n".getBytes());
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
        final HTTPMethod method = HTTPMethod.getHTTPMethod(contents[0]);
        final String filePath = contents[1].contains(".") ? contents[1]
                : (contents[1] + "/index.html").replace("//", "/");
        final Path path = Paths.get(webServerConfig.getRoot() + filePath);
        final HTTPVersion version = HTTPVersion.getHTTPVersion(contents[2]);
        final HTTPHeader header = new HTTPHeader(path, method, version);
        return Optional.of(header);
    }
}
