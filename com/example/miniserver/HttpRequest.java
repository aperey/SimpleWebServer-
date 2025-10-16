package com.example.miniserver;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequest {
    public final String method;
    public final String path;
    public final String httpVersion;
    public final Map<String,String> headers;
    public final Map<String,String> query;

    private HttpRequest(String method, String path, String httpVersion,
                        Map<String,String> headers, Map<String,String> query) {
        this.method = method; this.path = path; this.httpVersion = httpVersion;
        this.headers = headers; this.query = query;
    }

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.US_ASCII));
        String start = r.readLine();
        if (start == null || start.isEmpty()) throw new IOException("Empty request");

        String[] parts = start.split(" ");
        String method = parts[0];
        String target = parts[1];
        String version = parts.length > 2 ? parts[2] : "HTTP/1.1";

        Map<String,String> headers = new LinkedHashMap<>();
        for (String line; (line = r.readLine()) != null && !line.isEmpty();) {
            int idx = line.indexOf(':');
            if (idx > 0) headers.put(line.substring(0, idx).trim(),
                                     line.substring(idx + 1).trim());
        }

        String path = target;
        Map<String,String> query = new HashMap<>();
        int qi = target.indexOf('?');
        if (qi >= 0) {
            path = target.substring(0, qi);
            String qs = target.substring(qi + 1);
            for (String kv : qs.split("&")) {
                if (kv.isEmpty()) continue;
                String[] kvp = kv.split("=", 2);
                String k = URLDecoder.decode(kvp[0], StandardCharsets.UTF_8);
                String v = kvp.length > 1 ? URLDecoder.decode(kvp[1], StandardCharsets.UTF_8) : "";
                query.put(k, v);
            }
        }
        return new HttpRequest(method, path, version, headers, query);
    }
}
