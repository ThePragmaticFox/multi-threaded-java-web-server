package com.server;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HTTPResponseHandler {

    public boolean getResponse(final HTTPMethod type) {
        return type.match(this::handleGET, this::handleHEAD, this::handleUNKNOWN);
    }

    private boolean handleGET(final HTTPMethod.GET get) {
        /**
         * TODO
         */
        final File file = get.getHeader().getPath().toFile();
        if (!file.exists()) {
            get.getOutputStream().print("HTTP/1.1 404 Not Found" + "\n");
            get.getOutputStream().print("Connection: close" + "\r\n");
            return true;
        }
        if (!file.canRead()) {
            return false;
        }
        try {
            final String body = Files.readString(get.getHeader().getPath(), StandardCharsets.UTF_8);
            get.getOutputStream().print("HTTP/1.1 200 OK" + "\n");
            get.getOutputStream().print("Content-Type: text/html" + "\n");
            get.getOutputStream()
                    .print("Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\n");
            get.getOutputStream().print("Connection: close" + "\r\n");
            get.getOutputStream().print("\n" + body + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean handleHEAD(final HTTPMethod.HEAD head) {
        /**
         * TODO
         */
        final File file = head.getHeader().getPath().toFile();
        if (!file.exists()) {
            head.getOutputStream().print("HTTP/1.1 404 Not Found" + "\n");
            head.getOutputStream().print("Connection: close" + "\r\n");
            return true;
        }
        if (!file.canRead()) {
            return false;
        }
        try {
            final String body =
                    Files.readString(head.getHeader().getPath(), StandardCharsets.UTF_8);
            head.getOutputStream().print("HTTP/1.1 200 OK" + "\n");
            head.getOutputStream().print("Content-Type: text/html" + "\n");
            head.getOutputStream()
                    .print("Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\n");
            head.getOutputStream().print("Connection: close" + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean handleUNKNOWN(final HTTPMethod.UNKNOWN unknown) {
        unknown.getOutputStream().print("HTTP/1.1 501 Not Implemented" + "\n");
        unknown.getOutputStream().print("Connection: close" + "\r\n");
        return true;
    }
}
