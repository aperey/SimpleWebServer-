package com.example.miniserver.middleware;

import com.example.miniserver.*;

public class LoggingMiddleware extends AbstractMiddleware {
    @Override protected void before(HttpContext ctx) {
        System.out.printf("[REQ] %s %s%n", ctx.request.method, ctx.request.path);
    }
    @Override protected HttpResponse after(HttpContext ctx, HttpResponse resp) {
        if (resp != null) {
            System.out.printf("[RES] %d %s%n", resp.status, resp.reason);
        }
        return resp;
    }
}
