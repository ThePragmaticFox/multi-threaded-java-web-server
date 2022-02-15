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
    private final int nbPoolThreads;
    private final int backlogSize;
    private final int maxReqBytes;

    public WebServerConfig(final String root, final String host, final int port,
            final int nbPoolThreads, final int backlogSize, final int maxReqBytes) {
        this.root = root;
        this.host = host;
        this.port = port;
        this.nbPoolThreads = nbPoolThreads;
        this.backlogSize = backlogSize;
        this.maxReqBytes = maxReqBytes;
    }

    public int getBacklogSize() {
        return backlogSize;
    }

    public int getNbPoolThreads() {
        return nbPoolThreads;
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

    public int getMaxReqBytes() {
        return maxReqBytes;
    }

    public static WebServerConfig importConfig(final String configPath) throws IOException {
        final Gson gson = new Gson();
        final Reader reader = Files.newBufferedReader(Paths.get(configPath));
        final WebServerConfig webServerConfig = gson.fromJson(reader, WebServerConfig.class);
        return webServerConfig;
    }
}
