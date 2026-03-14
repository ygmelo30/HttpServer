package org.example.httpserver.specialendpoint;

import org.example.request.Request;

import java.io.OutputStream;
import java.net.Socket;

public interface SpecialEndpoint {
    boolean matches(String path);
    void handle(Request request, Socket socket, OutputStream out);
}