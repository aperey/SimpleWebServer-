package com.example.miniserver.middleware;

import com.example.miniserver.*;

public interface Middleware {
    HttpResponse handle(HttpContext ctx) throws Exception;
    void setNext(Middleware next);
}
