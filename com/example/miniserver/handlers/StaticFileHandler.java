package com.example.miniserver.handlers;

import com.example.miniserver.*;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class StaticFileHandler implements HttpHandler {
    private final Path root;

    public StaticFileHandler(String rootDir) {
        this.root = Paths.get(rootDir).toAbsolutePath().normalize();
    }

    @Override public HttpResponse handle(HttpContext ctx) throws IOException {
        String rel = ctx.request.path.equals("/") ? "/index.html" : ctx.request.path;
        Path p = root.resolve(rel.substring(1)).normalize();
        if (!p.startsWith(root) || !Files.exists(p) || Files.isDirectory(p)) {
            return HttpResponse.notFound("No such file");
        }
        byte[] bytes = Files.readAllBytes(p);
        String mime = mimeFor(p.getFileName().toString());
        return new HttpResponse.Builder()
                .header("Content-Type", mime)
                .header("Content-Length", String.valueOf(bytes.length))
                .body(bytes).build();
    }

    private static final Map<String,String> MIME = Map.ofEntries(
            Map.entry("html","text/html; charset=utf-8"),
            Map.entry("css","text/css"),
            Map.entry("js","application/javascript"),
            Map.entry("png","image/png"),
            Map.entry("jpg","image/jpeg"),
            Map.entry("jpeg","image/jpeg"),
            Map.entry("gif","image/gif"),
            Map.entry("txt","text/plain; charset=utf-8")
    );
    private static String mimeFor(String name){
        int i = name.lastIndexOf('.');
        String ext = (i>0) ? name.substring(i+1).toLowerCase() : "";
        return MIME.getOrDefault(ext, "application/octet-stream");
    }
}
