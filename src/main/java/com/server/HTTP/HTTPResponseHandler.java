package com.server.HTTP;

import java.io.File;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class HTTPResponseHandler {

    public static boolean getResponse(final HTTPHeader header,
            final HTTPOutputStream outputStream) {
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

    private static void processContentType(final HTTPFileExtension ext, final HTTPOutputStream out)
            throws IOException {
        switch (ext) {
            case TXT:
                out.write("Content-Type: text/plain".getBytes());
            case CSS:
                out.write("Content-Type: text/css".getBytes());
                break;
            case HTML:
                out.write("Content-Type: text/html".getBytes());
                break;
            case JPEG:
                out.write("Content-Type: image/jpeg".getBytes());
                break;
            case JPG:
                out.write("Content-Type: image/jpg".getBytes());
                break;
            case JS:
                out.write("Content-Type: text/javascript".getBytes());
                break;
            case PNG:
                out.write("Content-Type: image/png".getBytes());
                break;
            case SVG:
                out.write("Content-Type: image/svg".getBytes());
                break;
            case TS:
                out.write("Content-Type: text/typescript".getBytes());
                break;
            case JSON:
                out.write("Content-Type: text/json".getBytes());
                break;
            case UNKNOWN:
                return;
            default:
                throw new IllegalStateException("The File Extension <<" + ext + ">> hasn't been implemented.");
        }
        out.write("\n".getBytes());
    }

    private static boolean handleGET(final HTTPHeader header, final HTTPOutputStream outputStream) {
        final File file = header.getPath().toFile();
        if (!file.exists()) {
            try {
                outputStream.write("HTTP/1.1 404 Not Found".getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write("Connection: close".getBytes());
                outputStream.write("\r\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (!file.canRead()) {
            return false;
        }
        try {
            outputStream.write("HTTP/1.1 200 OK".getBytes());
            outputStream.write("\n".getBytes());
            processContentType(
                    HTTPFileExtension.get(FilenameUtils.getExtension(header.getPath().toString())),
                    outputStream);
            outputStream.write(String
                    .format("Content-Length: " + header.getPath().toFile().length()).getBytes());
            outputStream.write("\n".getBytes());
            // recommended security header
            outputStream.write("X-Content-Type-Options: nosniff".getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write("Connection: close".getBytes());
            outputStream.write("\r\n\r\n".getBytes());
            Files.copy(header.getPath(), outputStream.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean handleUNKNOWN(final HTTPHeader header,
            final HTTPOutputStream outputStream) {
        try {
            outputStream.write("HTTP/1.1 501 Not Implemented".getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write("Connection: close".getBytes());
            outputStream.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
