package org.example.httpserver;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
