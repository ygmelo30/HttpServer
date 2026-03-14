package org.example.httpserver.server;

import org.example.request.Request;

import java.io.OutputStream;

@FunctionalInterface
public interface Handler {
    HandlerError handle (OutputStream out, Request request);
}
