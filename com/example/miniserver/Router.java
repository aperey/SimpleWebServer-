package com.example.miniserver;

import com.example.miniserver.handlers.*;

import java.util.*;
import java.util.function.Supplier;

public class Router {
    private final Map<String, Supplier<HttpHandler>> routes = new HashMap<>();
    private final HttpHandler staticHandler;

    public Router(String staticRoot) {
        this.staticHandler = new StaticFileHandler(staticRoot);
        // Реєструємо базові маршрути
        routes.put("/hello", HelloHandler::new);
    }

    public HttpResponse route(HttpContext ctx) throws Exception {
        Supplier<HttpHandler> sup = routes.get(ctx.request.path);
        if (sup != null) return sup.get().handle(ctx);
        // fallback на статичні файли
        return staticHandler.handle(ctx);
    }

    public Router get(String path, Supplier<HttpHandler> supplier){
        routes.put(path, supplier);
        return this;
    }
}
