package com.server.HTTP;

import java.io.File;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class HTTPResponseHandler {

    public static boolean getResponse(final HTTPHeader header, final HTTPOutputStream outputStream) {
        switch (header.getMethod()) {
            case GET:
                return handleGET(header, outputStream);
            case UNKNOWN:
                return handleUNKNOWN(header, outputStream);
            default:
                throw new IllegalStateException(
                        "The HTTPMethod <<" + header.getMethod() + ">> hasn't been implemented.");
        }
    }

    private static void processContentType(final HTTPFileExtension ext, final HTTPOutputStream outputStream)
            throws IOException {
        if (ext.equals(HTTPFileExtension.UNKNOWN)) {
            return;
        }
        outputStream.write(ext.getBytes());
        outputStream.write(HTTPConstants.NEWLINE.getBytes());
        // recommended security header if file extension is known
        outputStream.write(HTTPConstants.NO_SNIFF.getBytes());
        outputStream.write(HTTPConstants.NEWLINE.getBytes());
    }

    private static boolean handleGET(final HTTPHeader header, final HTTPOutputStream outputStream) {
        final File file = header.getPath().toFile();
        if (!file.exists()) {
            try {
                outputStream.write(HTTPConstants.NOT_FOUND.getBytes());
                outputStream.write(HTTPConstants.NEWLINE.getBytes());
                outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
                outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return true;
        }
        if (!file.canRead()) {
            return false;
        }
        try {
            outputStream.write(HTTPConstants.OK.getBytes());
            outputStream.write(HTTPConstants.NEWLINE.getBytes());
            processContentType(HTTPFileExtension.get(FilenameUtils.getExtension(header.getPath().toString())),
                    outputStream);
            outputStream.write(HTTPConstants.CONTENT_LENGTH.getBytes());
            outputStream.write(Long.toString(header.getPath().toFile().length()).getBytes());
            outputStream.write(HTTPConstants.NEWLINE.getBytes());
            outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
            outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
            Files.copy(header.getPath(), outputStream.get());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return true;
    }

    private static boolean handleUNKNOWN(final HTTPHeader header, final HTTPOutputStream outputStream) {
        try {
            outputStream.write(HTTPConstants.NOT_IMPLEMENTED.getBytes());
            outputStream.write(HTTPConstants.NEWLINE.getBytes());
            outputStream.write(HTTPConstants.CONNECTION_CLOSE.getBytes());
            outputStream.write(HTTPConstants.NEW_EMPTYLINE.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return true;
    }
}
