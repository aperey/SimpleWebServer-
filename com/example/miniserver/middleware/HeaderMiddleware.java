package com.example.miniserver.middleware;

import com.example.miniserver.*;

public class HeaderMiddleware extends AbstractMiddleware {
    @Override protected HttpResponse after(HttpContext ctx, HttpResponse resp) {
        if (resp != null) {
            resp.headers.putIfAbsent("Connection", "close");
            resp.headers.putIfAbsent("Server", "MiniJava/1.0");
            resp.headers.putIfAbsent("Cache-Control", "no-store");
        }
        return resp;
    }
}
