package com.example.miniserver;

public final class ServerConfig {
    private static final ServerConfig INSTANCE = new ServerConfig();

    private int port = 8080;
    private int threadPoolSize = 16;
    private String staticRoot = "www";

    private ServerConfig() { }

    public static ServerConfig get() { return INSTANCE; }

    public int port() { return port; }
    public ServerConfig port(int p) { this.port = p; return this; }

    public int threadPoolSize() { return threadPoolSize; }
    public ServerConfig threadPoolSize(int n) { this.threadPoolSize = n; return this; }

    public String staticRoot() { return staticRoot; }
    public ServerConfig staticRoot(String root) { this.staticRoot = root; return this; }
}
