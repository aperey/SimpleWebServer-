package com.example.miniserver.handlers;

import com.example.miniserver.*;

public interface HttpHandler {
    HttpResponse handle(HttpContext ctx) throws Exception;
}
