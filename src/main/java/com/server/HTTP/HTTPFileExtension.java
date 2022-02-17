package com.server.HTTP;

public enum HTTPFileExtension {
    HTML, CSS, JS, TS, TXT, JSON, JPG, JPEG, PNG, SVG, UNKNOWN;

    public static HTTPFileExtension get(final String extension) {
        final String standardizedExt = extension.toLowerCase();
        switch (standardizedExt) {
            case "txt":
                return TXT;
            case "html":
                return HTML;
            case "css":
                return CSS;
            case "js":
                return JS;
            case "ts":
                return TS;
            case "json":
                return JSON;
            case "jpg":
                return JPG;
            case "jpeg":
                return JPEG;
            case "png":
                return PNG;
            case "svg":
                return SVG;
            default:
                return UNKNOWN;
        }
    }
}
