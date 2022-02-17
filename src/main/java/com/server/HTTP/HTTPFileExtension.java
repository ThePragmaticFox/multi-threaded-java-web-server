package com.server.HTTP;

public enum HTTPFileExtension {
    HTML("Content-Type: text/html"),
    CSS("Content-Type: text/css"),
    JS("Content-Type: text/javascript"),
    TXT("Content-Type: text/plain"), 
    JSON("Content-Type: application/json"),
    XML("Content-Type: application/xml"),
    WASM("Content-Type: application/wasm"),
    WEBMANIFEST("Content-Type: application/manifest+json"),
    WEBAPP("Content-Type: application/x-web-app-manifest+json"),
    APPCACHE("Content-Type: text/cache-manifest"),
    BMP("Content-Type: image/bmp"),
    GIF("Content-Type: image/gif"),
    JPG("Content-Type: image/jpg"),
    JPEG("Content-Type: image/jpeg"),
    PNG("Content-Type: image/png"),
    SVG("Content-Type: image/svg+xml"),
    UNKNOWN("");

    public static HTTPFileExtension get(final String extension) {
        final String standardizedExt = extension.toLowerCase();
        return switch (standardizedExt) {
            case "html" -> HTML;
            case "css" -> CSS;
            case "js" -> JS;
            case "txt" -> TXT;
            case "json" -> JSON;
            case "xml" -> XML;
            case "wasm" -> WASM;
            case "webmanifest" -> WEBMANIFEST;
            case "webapp" -> WEBAPP;
            case "appcache" -> APPCACHE;
            case "bmp" -> BMP;
            case "gif" -> GIF;
            case "jpg" -> JPG;
            case "jpeg" -> JPEG;
            case "png" -> PNG;
            case "svg" -> SVG;
            default -> UNKNOWN;
        };
    }

    private final String literal;

    private HTTPFileExtension(final String literal) {
        this.literal = literal;
    }

    public boolean equalsName(final String otherLiteral) {
        return literal.equals(otherLiteral);
    }

    public byte[] getBytes() {
        return this.literal.getBytes();
    }
}
