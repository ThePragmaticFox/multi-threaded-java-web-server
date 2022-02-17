package com.server.HTTP;

public enum HTTPMethod {
    GET, UNKNOWN;

    public static HTTPMethod getHTTPMethod(final String method) {
        switch (method) {
            case "GET":
                return GET;
            default:
                return UNKNOWN;
        }
    }
}
