package com.server.HTTP;

public enum Method {
    GET, UNKNOWN;

    public static Method getMethod(final String method) {
        switch (method) {
            case "GET":
                return GET;
            default:
                return UNKNOWN;
        }
    }
}
