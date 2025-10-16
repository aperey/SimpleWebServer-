package com.example.miniserver;

import com.example.miniserver.middleware.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class HttpServer {
    private final ServerConfig cfg = ServerConfig.get();
    private final ExecutorService pool;
    private final Router router;
    private final Middleware pipeline;

    public HttpServer() {
        this.pool = Executors.newFixedThreadPool(cfg.threadPoolSize());
        this.router = new Router(cfg.staticRoot());

        // Будуємо ланцюжок middleware (Chain of Responsibility)
        Middleware logging = new LoggingMiddleware();
        Middleware headers = new HeaderMiddleware();
        logging.setNext(headers);
        // Кінцевий елемент — роутер як адаптований middleware:
        headers.setNext(new Middleware() {
            private Middleware next; // not used
            @Override public HttpResponse handle(HttpContext ctx) throws Exception { return router.route(ctx); }
            @Override public void setNext(Middleware next) { this.next = next; }
        });
        this.pipeline = logging;
    }

    public void start() throws IOException {
        try (ServerSocket ss = new ServerSocket(cfg.port())) {
            System.out.println("Listening on http://localhost:" + cfg.port());
            while (true) {
                Socket s = ss.accept();
                s.setSoTimeout(10_000);
                pool.submit(() -> handleClient(s));
            }
        }
    }

    private void handleClient(Socket s) {
        try (s; InputStream in = s.getInputStream(); OutputStream out = s.getOutputStream()) {
            HttpRequest req = HttpRequest.parse(in);
            if (!"GET".equalsIgnoreCase(req.method)) {
                new HttpResponse.Builder().status(405, "Method Not Allowed")
                        .bodyText("Only GET supported", "text/plain").build().writeTo(out);
                return;
            }
            HttpContext ctx = new HttpContext(s, req);
            HttpResponse resp = pipeline.handle(ctx);
            if (resp == null) resp = HttpResponse.internalError("No response");
            resp.writeTo(out);
        } catch (Exception e) {
            try {
                HttpResponse.internalError(e.getMessage() == null ? e.toString() : e.getMessage())
                        .writeTo(s.getOutputStream());
            } catch (Exception ignore) {}
        }
    }

    public static void main(String[] args) throws Exception {
        // Приклад налаштувань (можна змінити аргументами/ENV)
        ServerConfig.get().port(8080).threadPoolSize(16).staticRoot("www");
        new HttpServer().start();
    }
}
