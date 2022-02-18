package com.server.HTTP;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class ResponseHandler {

    public static boolean getResponse(final Header header, final OutputStreamWrapper outputStream) {
        return switch (header.getMethod()) {
            case GET -> handleGET(header, outputStream);
            case UNKNOWN -> handleUNKNOWN(header, outputStream);
            default -> throw new IllegalStateException("The HTTPMethod <<" + header.getMethod() + ">> hasn't been implemented.");
        };
    }

    private static String getContentType(final FileExtension ext) {
        if (ext.equals(FileExtension.UNKNOWN)) {
            return Literals.EMPTY.getString();
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(ext.getString());
        builder.append(Literals.NEWLINE.getString());
        // recommended security header if file extension is known
        builder.append(Options.NO_SNIFF.getString());
        builder.append(Literals.NEWLINE.getString());
        return builder.toString();
    }

    private static boolean handleGET(final Header requestHeader, final OutputStreamWrapper outputStream) {
        final File file = requestHeader.getPath().toFile();
        if (!file.exists()) {
            try {
                outputStream.write(StatusCodes.NOT_FOUND.getBytes(requestHeader.getVersion()));
                outputStream.write(Literals.NEWLINE.getBytes());
                outputStream.write(Options.CONNECTION_CLOSE.getBytes());
                outputStream.write(Literals.NEW_EMPTYLINE.getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
            return true;
        }
        if (!file.canRead()) {
            return false;
        }
        try {
            final String responseHeader = buildHeader(requestHeader);
            outputStream.write(responseHeader.getBytes());
            Files.copy(requestHeader.getPath(), outputStream.get());
            final String debugString = outputStream.toString();
            System.out.println(debugString.substring(0, Math.min(250, debugString.length())));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
        return true;
    }

    private static String buildHeader(final Header requestHeader) {
        final StringBuilder builder = new StringBuilder();
        builder.append(StatusCodes.OK.getString());
        builder.append(Literals.NEWLINE.getString());
        final String extensionStr = FilenameUtils.getExtension(requestHeader.getPath().toString());
        final FileExtension extension = FileExtension.get(extensionStr);
        builder.append(getContentType(extension));
        builder.append(Options.CONTENT_LENGTH.getString());
        builder.append(Literals.SPACE.getString());
        builder.append(requestHeader.getPath().toFile().length());
        builder.append(Literals.NEWLINE.getString());
        builder.append(Options.CONNECTION_CLOSE.getString());
        builder.append(Literals.NEW_EMPTYLINE.getString());
        return builder.toString();
    }

    private static boolean handleUNKNOWN(final Header header, final OutputStreamWrapper outputStream) {
        try {
            outputStream.write(StatusCodes.NOT_IMPLEMENTED.getBytes(header.getVersion()));
            outputStream.write(Literals.NEWLINE.getBytes());
            outputStream.write(Options.CONNECTION_CLOSE.getBytes());
            outputStream.write(Literals.NEW_EMPTYLINE.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
        return true;
    }
}
