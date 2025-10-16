package com.example.miniserver;

import com.example.miniserver.handlers.*;

import java.util.function.Supplier;

public class HandlerFactory {
    public static Supplier<HttpHandler> hello() { return HelloHandler::new; }
    public static Supplier<HttpHandler> staticFiles(String root) { return () -> new StaticFileHandler(root); }
}
