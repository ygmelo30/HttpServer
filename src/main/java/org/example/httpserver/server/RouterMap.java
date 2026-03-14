package org.example.httpserver.server;

import org.example.httpserver.server.Handler;
import org.example.httpserver.server.HandlerError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RouterMap {
    private final Map<String, Handler> routes = new HashMap<>();

    public void addRoute(String path, Handler handler) {
        routes.put(path, handler);
    }
    public Handler resolve(String path) {
        if (routes.get(path) == null) {
            Handler handler = (body, request) -> {
                try {
                    body.write("All good\n".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    return new HandlerError(500, "Failed to write response\n");
                }
                return null;
            };

            return handler;
        }
        return routes.get(path);
    }
}
