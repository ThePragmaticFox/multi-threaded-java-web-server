package com.server;

public class WebServerConfig {

    private final String root;
    private final String localIp;
    private final int port;
    private final long queueSize;
    private final long nbPoolThreads;

    public WebServerConfig(final String root, final String localIp, final int port,
            final long queueSize, final long nbPoolThreads) {
        this.root = root;
        this.localIp = localIp;
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

    public String getLocalIp() {
        return localIp;
    }

    public String getRoot() {
        return root;
    }
}
