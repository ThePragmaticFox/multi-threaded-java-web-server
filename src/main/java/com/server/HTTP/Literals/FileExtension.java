package com.server.HTTP.Literals;

/**
 * https://github.com/h5bp/server-configs-nginx/blob/main/mime.types
 */

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
    CUR("image/x-icon"),
    ICO("image/x-icon"),
    BMP("image/bmp"),
    GIF("image/gif"),
    JPG("image/jpeg"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml"),
    TIF("image/tiff"),
    TIFF("image/tiff"),
    WEBP("image/webp"),
    AVIF("image/avif"),
    AVIFS("image/avif-sequence"),
    MID("audio/midi"),
    MIDI("audio/midi"),
    KAR("audio/midi"),
    AAC("audio/mp4"),
    F4A("audio/mp4"),
    M4A("audio/mp4"),
    MP3("audio/mpeg"),
    OGA("audio/ogg"),
    OGG("audio/ogg"),
    OPUS("audio/opus"),
    MP4("video/mp4"),
    MPEG("video/mpeg"),
    MPG("video/mpeg"),
    OGV("video/ogg"),
    MOV("video/quicktime"),
    WEBM("video/webm"),
    FLV("video/x-flv"),
    MNG("video/x-mng"),
    ASF("video/x-ms-asf"),
    ASX("video/x-ms-asf"),
    AVI("video/x-msvideo"),
    DOC("application/msword"),
    XLS("application/vnd.ms-excel"),
    PPT("application/vnd.ms-powerpoint"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    WOFF("font/woff"),
    WOFF2("font/woff2"),
    EOT("application/vnd.ms-fontobject"),
    TTF("font/ttf"),
    TTC("font/collection"),
    OTF("font/otf"),
    PDF("application/pdf"),
    AI("application/postscript"),
    EPS("application/postscript"),
    PS("application/postscript"),
    RTF("application/rtf"),
    S7Z("application/x-7z-compressed"),
    RAR("application/x-rar-compressed"),
    ZIP("application/zip"),
    ICS("text/calendar"),
    CSV("text/csv"),
    HTM("text/html"),
    SHTML("text/html"),
    MD("text/markdown"),
    MARKDOWN("text/markdown"),
    MML("text/mathml"),
    VCARD("text/vcard"),
    VCF("text/vcard"),
    UNKNOWN("");

    public static FileExtension get(final String extension) {
        final String standardizedExt = extension.toUpperCase();
        return switch (standardizedExt) {
            case "HTML" -> HTML;
            case "CSS" -> CSS;
            case "JS" -> JS;
            case "TXT" -> TXT;
            case "JSON" -> JSON;
            case "XML" -> XML;
            case "WASM" -> WASM;
            case "WEBMANIFEST" -> WEBMANIFEST;
            case "WEBAPP" -> WEBAPP;
            case "APPCACHE" -> APPCACHE;
            case "CUR" -> CUR;
            case "ICO" -> ICO;
            case "BMP" -> BMP;
            case "GIF" -> GIF;
            case "JPG" -> JPG;
            case "JPEG" -> JPEG;
            case "PNG" -> PNG;
            case "SVG" -> SVG;
            case "TIF" -> TIF;
            case "TIFF" -> TIFF;
            case "WEBP" -> WEBP;
            case "AVIF" -> AVIF;
            case "AVIFS" -> AVIFS;
            case "MID" -> MID;
            case "MIDI" -> MIDI;
            case "KAR" -> KAR;
            case "AAC" -> AAC;
            case "F4A" -> F4A;
            case "M4A" -> M4A;
            case "MP3" -> MP3;
            case "OGA" -> OGA;
            case "OGG" -> OGG;
            case "OPUS" -> OPUS;
            case "MP4" -> MP4;
            case "MPEG" -> MPEG;
            case "MPG" -> MPG;
            case "OGV" -> OGV;
            case "MOV" -> MOV;
            case "WEBM" -> WEBM;
            case "FLV" -> FLV;
            case "MNG" -> MNG;
            case "ASF" -> ASF;
            case "ASX" -> ASX;
            case "AVI" -> AVI;
            case "DOC" -> DOC;
            case "XLS" -> XLS;
            case "PPT" -> PPT;
            case "DOCX" -> DOCX;
            case "XLSX" -> XLSX;
            case "PPTX" -> PPTX;
            case "WOFF" -> WOFF;
            case "WOFF2" -> WOFF2;
            case "EOT" -> EOT;
            case "TTF" -> TTF;
            case "TTC" -> TTC;
            case "OTF" -> OTF;
            case "PDF" -> PDF;
            case "AI" -> AI;
            case "EPS" -> EPS;
            case "PS" -> PS;
            case "RTF" -> RTF;
            case "7Z" -> S7Z;
            case "RAR" -> RAR;
            case "ZIP" -> ZIP;
            case "ICS" -> ICS;
            case "CSV" -> CSV;
            case "HTM" -> HTM;
            case "SHTML" -> SHTML;
            case "MD" -> MD;
            case "MARKDOWN" -> MARKDOWN;
            case "MML" -> MML;
            case "VCARD" -> VCARD;
            case "VCF" -> VCF;
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
            case UNKNOWN -> Other.EMPTY.getBytes();
            default -> (Options.CONTENT_TYPE.getString() + Other.SPACE.getString() + literal).getBytes();
        };
    }

    public String getString() {
        return switch(this) {
         case UNKNOWN -> Other.EMPTY.getString();
         default -> Options.CONTENT_TYPE.getString() + Other.SPACE.getString() + literal;
        };
    }
}
