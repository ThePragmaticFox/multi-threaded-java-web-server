package com.server.HTTP;

import java.io.File;
import java.io.FileInputStream;
import com.server.HTTP.Literals.FileExtension;
import com.server.HTTP.Literals.Other;
import com.server.HTTP.Literals.Options;
import com.server.HTTP.Literals.StatusCodes;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class ResponseHandler {

    public static void getResponse(final Header header, final OutputStreamWrapper outputStream) throws IOException {
        switch (header.getMethod()) {
            case GET -> handleGET(header, outputStream);
            case UNKNOWN -> handleUNKNOWN(header, outputStream);
            default -> throw new IllegalStateException(
                    "The HTTPMethod <<" + header.getMethod() + ">> hasn't been implemented.");
        }
    }

    private static String getContentType(final FileExtension ext) {
        if (ext.equals(FileExtension.UNKNOWN)) {
            return Other.EMPTY.getString();
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(ext.getString());
        builder.append(Other.NEWLINE.getString());
        // recommended security header if file extension is known
        builder.append(Options.NO_SNIFF.getString());
        builder.append(Other.NEWLINE.getString());
        return builder.toString();
    }

    private static void handleGET(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        final File file = requestHeader.getPath().toFile();
        if (!file.exists()) {
            outputStream.write(StatusCodes.NOT_FOUND.getBytes(requestHeader.getVersion()));
            outputStream.write(Other.NEWLINE.getBytes());
            outputStream.write(Options.CONNECTION_CLOSE.getBytes());
            outputStream.write(Other.NEW_EMPTYLINE.getBytes());
        }
        if (!file.canRead()) {
            outputStream.write(StatusCodes.INTERNAL_SERVER_ERROR.getBytes(requestHeader.getVersion()));
            outputStream.write(Other.NEWLINE.getBytes());
            outputStream.write(Options.CONNECTION_CLOSE.getBytes());
            outputStream.write(Other.NEW_EMPTYLINE.getBytes());
        }
        writeHeader(requestHeader, outputStream);
        writeBody(requestHeader, outputStream);
    }

    private static void writeHeader(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        final String responseHeader = buildHeader(requestHeader);
        outputStream.write(responseHeader.getBytes());
    }

    private static void writeBody(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        final var file = new FileInputStream(requestHeader.getPath().toFile());
        final byte[] byteBuffer = new byte[16384];
        int readBytes = file.read(byteBuffer);
        while (readBytes > 0) {
            outputStream.write(byteBuffer, 0, readBytes);
            readBytes = file.read(byteBuffer);
        }
    }

    private static String buildHeader(final Header requestHeader) {
        final StringBuilder builder = new StringBuilder();
        builder.append(StatusCodes.OK.getString(requestHeader.getVersion()));
        builder.append(Other.NEWLINE.getString());
        final String extensionStr = FilenameUtils.getExtension(requestHeader.getPath().toString());
        final FileExtension extension = FileExtension.get(extensionStr);
        builder.append(getContentType(extension));
        builder.append(Options.CONTENT_LENGTH.getString());
        builder.append(Other.SPACE.getString());
        builder.append(requestHeader.getPath().toFile().length());
        builder.append(Other.NEWLINE.getString());
        builder.append(Options.CONNECTION_CLOSE.getString());
        builder.append(Other.NEW_EMPTYLINE.getString());
        return builder.toString();
    }

    private static void handleUNKNOWN(final Header header, final OutputStreamWrapper outputStream) throws IOException {
        outputStream.write(StatusCodes.NOT_IMPLEMENTED.getBytes(header.getVersion()));
        outputStream.write(Other.NEWLINE.getBytes());
        outputStream.write(Options.CONNECTION_CLOSE.getBytes());
        outputStream.write(Other.NEW_EMPTYLINE.getBytes());
    }
}
