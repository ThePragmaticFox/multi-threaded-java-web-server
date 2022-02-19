package com.server.HTTP;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.server.ServerLogger;
import com.server.WebServerConfig;
import com.server.HTTP.Literals.Version;

public class RequestHandler {

    private static List<String> readHeader(final InputStreamWrapper inputStream) throws IOException {
        final List<String> headerLines = new ArrayList<>();
        String inputLine = inputStream.readLine();
        while (inputLine != null && inputLine.length() > 0) {
            headerLines.add(inputLine);
            inputLine = inputStream.readLine();
        }
        return headerLines;
    }

    public static void handle(final Socket clientSocket, final WebServerConfig webServerConfig) {
        try (final InputStreamWrapper inputStream = new InputStreamWrapper(clientSocket.getInputStream());
                final OutputStreamWrapper outputStream = new OutputStreamWrapper(clientSocket.getOutputStream())) {
            // Only header is relevant, the body is ignored.
            final List<String> headerLines = readHeader(inputStream);
            final Optional<Header> header = parseHeader(webServerConfig, headerLines, outputStream);
            if (header.isPresent()) {
                ResponseHandler.getResponse(header.get(), outputStream);
            } else {
                ResponseWriter.header400(Version.HTTP_1_1, outputStream);
            }
            //ServerLogger.printDebug(headerLines, outputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static Optional<Header> parseHeader(final WebServerConfig webServerConfig, final List<String> headerLines,
            final OutputStreamWrapper outputStream) throws IOException {
        if (headerLines.size() < 1) {
            ResponseWriter.header400(Version.HTTP_1_1, outputStream);
            return Optional.empty();
        }
        // Only interested in the first line for the time being; containing method, path and version
        final String[] contents =
                Arrays.stream(headerLines.get(0).split(" ")).map(content -> content.trim()).toArray(String[]::new);
        if (contents.length != 3) {
            return Optional.empty();
        }
        final Method method = Method.getMethod(contents[0]);
        final String filePath =
                contents[1].contains(".") ? contents[1] : (contents[1] + "/index.html").replace("//", "/");
        final Path path = Paths.get(webServerConfig.getRoot() + filePath);
        final Optional<Version> version = Version.getVersion(contents[2]);
        if (version.isEmpty()) {
            ResponseWriter.header505(Version.HTTP_1_1, outputStream);
            return Optional.empty();
        }
        final Header header = new Header(path, method, version.get());
        return Optional.of(header);
    }
}
