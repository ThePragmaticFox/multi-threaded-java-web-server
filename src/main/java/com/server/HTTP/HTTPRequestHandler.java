package com.server.HTTP;

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

    private List<String> readHeader(final HTTPInputStream inputStream) throws IOException {
        final List<String> headerLines = new ArrayList<>();
        String inputLine = inputStream.readLine();
        while (inputLine != null && inputLine.length() > 0) {
            headerLines.add(inputLine);
            inputLine = inputStream.readLine();
        }
        return headerLines;
    }

    public void handle() {
        try {
            final HTTPInputStream inputStream = new HTTPInputStream(clientSocket.getInputStream());
            final HTTPOutputStream outputStream = new HTTPOutputStream(clientSocket.getOutputStream());

            // Only header is relevant, the body is ignored.
            final List<String> headerLines = readHeader(inputStream);

            parseHeader(headerLines).ifPresentOrElse(header -> {
                try {
                    if (!handleResponse(header, outputStream)) {
                        outputStream.write(HTTPConstants.INTERNAL_SERVER_ERROR.getBytes());
                        outputStream.write(HTTPConstants.NEWLINE.getBytes());
                        outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
                        outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }, () -> {
                try {
                    outputStream.write(HTTPConstants.BAD_REQUEST.getBytes());
                    outputStream.write(HTTPConstants.NEWLINE.getBytes());
                    outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
                    outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            // outputStream.flush();
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private boolean handleResponse(final HTTPHeader header, final HTTPOutputStream outputStream) throws IOException {
        switch (header.getVersion()) {
            case HTTP_1_1:
                return HTTPResponseHandler.getResponse(header, outputStream);
            case UNKNOWN:
                outputStream.write(HTTPConstants.HTTP_VERSION_NOT_SUPPORTED.getBytes());
                outputStream.write(HTTPConstants.NEWLINE.getBytes());
                outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
                outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
                return true;
            default:
                throw new IllegalStateException(
                        "The HTTPVersion <<" + header.getVersion() + ">> has not been implented");
        }
    }

    private Optional<HTTPHeader> parseHeader(final List<String> headerLines) {
        if (headerLines.size() < 1) {
            return Optional.empty();
        }
        // Only interested in the first line, containing method, path and version
        final String[] contents =
                Arrays.stream(headerLines.get(0).split(" ")).map(content -> content.trim()).toArray(String[]::new);
        if (contents.length != 3) {
            return Optional.empty();
        }
        final HTTPMethod method = HTTPMethod.getHTTPMethod(contents[0]);
        final String filePath =
                contents[1].contains(".") ? contents[1] : (contents[1] + "/index.html").replace("//", "/");
        final Path path = Paths.get(webServerConfig.getRoot() + filePath);
        final HTTPVersion version = HTTPVersion.getHTTPVersion(contents[2]);
        final HTTPHeader header = new HTTPHeader(path, method, version);
        return Optional.of(header);
    }
}
