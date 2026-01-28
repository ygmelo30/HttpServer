package org.example.httpserver;

import org.example.lessonFour.Request;

import java.io.OutputStream;

@FunctionalInterface
public interface Handler {
    HandlerError handle (OutputStream out, Request request);
}
