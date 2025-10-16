package com.example.miniserver.middleware;

import com.example.miniserver.*;

public abstract class AbstractMiddleware implements Middleware {
    protected Middleware next;

    @Override public void setNext(Middleware next) { this.next = next; }

    @Override public HttpResponse handle(HttpContext ctx) throws Exception {
        // Шаблон: before → next → after
        before(ctx);
        HttpResponse resp = (next != null) ? next.handle(ctx) : null;
        return after(ctx, resp);
    }

    protected void before(HttpContext ctx) throws Exception { }
    protected HttpResponse after(HttpContext ctx, HttpResponse resp) throws Exception { return resp; }
}
