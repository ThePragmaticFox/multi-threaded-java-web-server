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
import com.server.HTTP.Literals.Other;
import com.server.HTTP.Literals.Options;
import com.server.HTTP.Literals.StatusCodes;
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
            Optional<Header> header = parseHeader(webServerConfig, readHeader(inputStream));
            if (header.isPresent() && !handleResponse(header.get(), outputStream)) {
                outputStream.write(StatusCodes.INTERNAL_SERVER_ERROR.getBytes(header.get().getVersion()));
                outputStream.write(Other.NEWLINE.getBytes());
                outputStream.write(Options.CONNECTION_CLOSE.getBytes());
                outputStream.write(Other.NEW_EMPTYLINE.getBytes());
            } else {
                outputStream.write(StatusCodes.BAD_REQUEST.getBytes(Version.HTTP_1_1));
                outputStream.write(Other.NEWLINE.getBytes());
                outputStream.write(Options.CONNECTION_CLOSE.getBytes());
                outputStream.write(Other.NEW_EMPTYLINE.getBytes());
            }
            printDebug(outputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static boolean handleResponse(final Header header, final OutputStreamWrapper outputStream)
            throws IOException {
        switch (header.getVersion()) {
            case HTTP_1_1:
                return ResponseHandler.getResponse(header, outputStream);
            case UNKNOWN:
                outputStream.write(StatusCodes.HTTP_VERSION_NOT_SUPPORTED.getBytes(header.getVersion()));
                outputStream.write(Other.NEWLINE.getBytes());
                outputStream.write(Options.CONNECTION_CLOSE.getBytes());
                outputStream.write(Other.NEW_EMPTYLINE.getBytes());
                return true;
            default:
                throw new IllegalStateException(
                        "The HTTPVersion <<" + header.getVersion() + ">> has not been implented");
        }
    }

    private static Optional<Header> parseHeader(final WebServerConfig webServerConfig, final List<String> headerLines) {
        if (headerLines.size() < 1) {
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
        final Version version = Version.getVersion(contents[2]);
        final Header header = new Header(path, method, version);
        return Optional.of(header);
    }

    private static void printDebug(final OutputStreamWrapper outputStream) {
        final String debugString = outputStream.toString();
        System.out.println(debugString.substring(0, Math.min(500, debugString.length())) + "\n");
        System.out.println(new String(new char[79]).replace("\0", "-"));
        System.out.println();
    }
}
