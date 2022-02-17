package com.server;

public enum HTTPVersion {
    HTTP_1_1, UNKNOWN;

    public static HTTPVersion getHTTPVersion(final String version) {
        switch (version) {
            case "HTTP/1.1":
                return HTTP_1_1;
            default:
                return UNKNOWN;
        }
    }
}
