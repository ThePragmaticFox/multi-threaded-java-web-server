package com.server;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;

public class WebServerConfig {

    private final String root;
    private final String host;
    private final int port;
    private final long queueSize;
    private final long nbPoolThreads;

    public WebServerConfig(final String root, final String host, final int port,
            final long queueSize, final long nbPoolThreads) {
        this.root = root;
        this.host = host;
        this.port = port;
        this.queueSize = queueSize;
        this.nbPoolThreads = nbPoolThreads;
    }

    public long getNbPoolThreads() {
        return nbPoolThreads;
    }

    public long getQueueSize() {
        return queueSize;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getRoot() {
        return root;
    }

    public static WebServerConfig importConfig(final String configPath) throws IOException {
        final Gson gson = new Gson();
        final Reader reader = Files.newBufferedReader(Paths.get(configPath));
        final WebServerConfig webServerConfig = gson.fromJson(reader, WebServerConfig.class);
        return webServerConfig;
    }
}
