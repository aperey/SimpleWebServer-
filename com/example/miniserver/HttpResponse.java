package com.example.miniserver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponse {
    public final int status;
    public final String reason;
    public final Map<String,String> headers;
    public final byte[] body;

    private HttpResponse(int status, String reason, Map<String,String> headers, byte[] body) {
        this.status = status; this.reason = reason; this.headers = headers; this.body = body;
    }

    public void writeTo(OutputStream out) throws IOException {
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.US_ASCII));
        w.write("HTTP/1.1 " + status + " " + reason + "\r\n");
        for (var e : headers.entrySet()) {
            w.write(e.getKey() + ": " + e.getValue() + "\r\n");
        }
        w.write("\r\n");
        w.flush();
        if (body != null && body.length > 0) out.write(body);
        out.flush();
    }

    public static class Builder {
        private int status = 200;
        private String reason = "OK";
        private final Map<String,String> headers = new LinkedHashMap<>();
        private byte[] body = new byte[0];

        public Builder status(int s, String r) { this.status = s; this.reason = r; return this; }
        public Builder header(String k, String v) { headers.put(k, v); return this; }
        public Builder body(byte[] b) { this.body = b; return this; }
        public Builder bodyText(String text, String mime) {
            byte[] b = text.getBytes(StandardCharsets.UTF_8);
            header("Content-Type", mime + "; charset=utf-8");
            header("Content-Length", String.valueOf(b.length));
            return body(b);
        }
        public HttpResponse build() { return new HttpResponse(status, reason, headers, body); }
    }

    public static HttpResponse notFound(String msg) {
        return new Builder().status(404, "Not Found")
                .bodyText("<h1>404</h1><p>" + msg + "</p>", "text/html").build();
    }

    public static HttpResponse internalError(String msg) {
        return new Builder().status(500, "Internal Server Error")
                .bodyText("<h1>500</h1><pre>" + msg + "</pre>", "text/html").build();
    }
}
