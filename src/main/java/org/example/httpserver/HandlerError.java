package org.example.httpserver;

public class HandlerError {
    private final int statusCode;
    private final String message;

    HandlerError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
