package com.server.HTTP;

import java.io.File;
import java.io.IOException;

public class ResponseHandler {

    public static void getResponse(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        switch (requestHeader.getMethod()) {
            case GET -> handleGET(requestHeader, outputStream);
            case UNKNOWN -> handleUNKNOWN(requestHeader, outputStream);
            default -> throw new IllegalStateException(
                    "Method <<" + requestHeader.getMethod() + ">> hasn't been implemented.");
        }
    }

    private static void handleGET(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        final File file = requestHeader.getPath().toFile();
        if (!file.exists()) {
            ResponseWriter.header404(requestHeader.getVersion(), outputStream);
            return;
        }
        if (!file.canRead()) {
            ResponseWriter.header500(requestHeader.getVersion(), outputStream);
            return;
        }
        ResponseWriter.header200(requestHeader.getVersion(), requestHeader.getPath(), outputStream);
        ResponseWriter.body200(requestHeader.getPath().toFile(), outputStream);
    }

    private static void handleUNKNOWN(final Header requestHeader, final OutputStreamWrapper outputStream)
            throws IOException {
        ResponseWriter.header501(requestHeader.getVersion(), outputStream);
    }
}
