package org.example.httpserver;

import org.example.request.Request;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface Handler {
    HandlerError handle (OutputStream out, Request request);
}
