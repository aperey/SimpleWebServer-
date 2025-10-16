package com.example.miniserver;

import java.net.Socket;

public class HttpContext {
    public final Socket socket;
    public final HttpRequest request;

    public HttpContext(Socket socket, HttpRequest request) {
        this.socket = socket; this.request = request;
    }
}
