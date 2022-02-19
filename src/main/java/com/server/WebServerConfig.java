package com.server;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class WebServerConfig {

    private static final String ROOT = "www";
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 3000;
    private static final int NB_POOL_THREADS = 24;
    private static final int BACKLOG_SIZE = 100;

    private final String root;
    private final String host;
    private final int port;
    private final int nbPoolThreads;
    private final int backlogSize;

    public WebServerConfig(final String root, final String host, final int port, final int nbPoolThreads,
            final int backlogSize) {
        this.root = root;
        this.host = host;
        this.port = port;
        this.nbPoolThreads = nbPoolThreads;
        this.backlogSize = backlogSize;
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

    public static WebServerConfig importConfig(final String configPath) {
        final Gson gson = new Gson();
        WebServerConfig webServerConfig = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(configPath))) {
            webServerConfig = gson.fromJson(reader, WebServerConfig.class);
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            ServerLogger.log(Level.WARNING, e.getMessage());
        }
        if (webServerConfig != null) {
            return webServerConfig;
        }
        ServerLogger.log(Level.WARNING, "Couldn't read config from path: " + configPath);
        ServerLogger.log(Level.WARNING, String.format(
                "Using default config: root = %s, host = %s, port = %s, nb_pool_threads = %s, backlog_size = %s", ROOT,
                HOST, PORT, NB_POOL_THREADS, BACKLOG_SIZE));
        return new WebServerConfig(ROOT, HOST, PORT, NB_POOL_THREADS, BACKLOG_SIZE);
    }
}
