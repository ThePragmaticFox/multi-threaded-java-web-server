package com.server.HTTP;

public enum FileExtension {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    TXT("text/plain"), 
    JSON("application/json"),
    XML("application/xml"),
    WASM("application/wasm"),
    WEBMANIFEST("application/manifest+json"),
    WEBAPP("application/x-web-app-manifest+json"),
    APPCACHE("text/cache-manifest"),
    BMP("image/bmp"),
    GIF("image/gif"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml"),
    UNKNOWN("");

    public static FileExtension get(final String extension) {
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

    private FileExtension(final String literal) {
        this.literal = literal;
    }

    public boolean equalsName(final String otherLiteral) {
        return literal.equals(otherLiteral);
    }

    public byte[] getBytes() {
        return switch(this) {
            case UNKNOWN -> Literals.EMPTY.getBytes();
            default -> (Options.CONTENT_TYPE.getString() + Literals.SPACE.toString() + literal).getBytes();
        };
    }

    public String getString() {
        return literal;
    }
}
