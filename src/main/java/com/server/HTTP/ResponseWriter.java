package com.server.HTTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import com.server.HTTP.Literals.FileExtension;
import com.server.HTTP.Literals.Options;
import com.server.HTTP.Literals.Other;
import com.server.HTTP.Literals.StatusCodes;
import com.server.HTTP.Literals.Version;
import org.apache.commons.io.FilenameUtils;

public class ResponseWriter {

    private static final int PACKET_SIZE = 65536;

    public static void header200(final Version version, final Path path, final OutputStreamWrapper outputStream)
            throws IOException {
        final String extensionStr = FilenameUtils.getExtension(path.toString());
        final FileExtension extension = FileExtension.get(extensionStr);
        final StringBuilder builder = new StringBuilder();
        builder.append(StatusCodes.OK.getString(version)).append(Other.NEWLINE.getString())
                .append(getContentType(extension)).append(Options.CONTENT_LENGTH.getString())
                .append(Other.SPACE.getString()).append(path.toFile().length()).append(Other.NEWLINE.getString())
                .append(Options.CONNECTION_CLOSE.getString()).append(Other.NEW_EMPTYLINE.getString());
        outputStream.write(builder.toString().getBytes());
    }

    public static void body200(final File file, final OutputStreamWrapper outputStream) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final byte[] byteBuffer = new byte[PACKET_SIZE];
        int readBytes = fileInputStream.read(byteBuffer);
        while (readBytes > 0) {
            outputStream.write(byteBuffer, 0, readBytes);
            readBytes = fileInputStream.read(byteBuffer);
        }
        fileInputStream.close();
    }

    public static void header400(final Version version, final OutputStreamWrapper outputStream) throws IOException {
        headerErrorCodeHelper(StatusCodes.BAD_REQUEST.getString(version), outputStream);
    }

    public static void header404(final Version version, final OutputStreamWrapper outputStream) throws IOException {
        headerErrorCodeHelper(StatusCodes.NOT_FOUND.getString(version), outputStream);
    }

    public static void header500(final Version version, final OutputStreamWrapper outputStream) throws IOException {
        headerErrorCodeHelper(StatusCodes.INTERNAL_SERVER_ERROR.getString(version), outputStream);
    }

    public static void header501(final Version version, final OutputStreamWrapper outputStream) throws IOException {
        headerErrorCodeHelper(StatusCodes.NOT_IMPLEMENTED.getString(version), outputStream);
    }

    public static void header505(final Version version, final OutputStreamWrapper outputStream) throws IOException {
        headerErrorCodeHelper(StatusCodes.HTTP_VERSION_NOT_SUPPORTED.getString(version), outputStream);
    }

    private static String getContentType(final FileExtension ext) {
        if (ext.equals(FileExtension.UNKNOWN)) {
            return Other.EMPTY.getString();
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(ext.getString());
        builder.append(Other.NEWLINE.getString());
        // recommended security header if file extension is known
        builder.append(Options.NO_SNIFF.getString());
        builder.append(Other.NEWLINE.getString());
        return builder.toString();
    }

    private static void headerErrorCodeHelper(final String statusCode, final OutputStreamWrapper outputStream)
            throws IOException {
        final StringBuilder builder = new StringBuilder();
        builder.append(statusCode).append(Other.NEWLINE.getString()).append(Options.CONNECTION_CLOSE.getString())
                .append(Other.NEW_EMPTYLINE.getString());
        outputStream.write(builder.toString().getBytes());
    }
}
