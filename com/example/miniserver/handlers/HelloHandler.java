package com.example.miniserver.handlers;

import com.example.miniserver.*;

public class HelloHandler implements HttpHandler {
    @Override public HttpResponse handle(HttpContext ctx) {
        String name = ctx.request.query.getOrDefault("name", "World");
        String html = "<!doctype html><meta charset='utf-8'>" +
                "<h1>Hello, " + escape(name) + "!</h1>";
        return new HttpResponse.Builder()
                .bodyText(html, "text/html").build();
    }
    private static String escape(String s){
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
