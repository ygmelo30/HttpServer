package org.example.request;

import org.example.httpserver.exception.BadRequestException;

import java.io.IOException;
import java.io.InputStream;

public class RequestParser {
    public static Request requestFromInputStream(InputStream input) throws IOException, BadRequestException {
        Request request = new Request();
        byte[] buffer = new byte[1024];
        int n;
        while((n = input.read(buffer)) != -1) {
            request.parseRequest(buffer, n);
            if(request.isDone()) {
                break;
            }
        }
        return request;
    }

    public static boolean validateRequestLine(String method, String httpVersion, String version) {
        if(!method.matches("[A-Z]+")) {
            return false;
        }
        if(!httpVersion.startsWith("HTTP/")) {
            return false;
        }
        if(!version.equals("1.1")) {
            return false;
        }
        return true;
    }

    public static String normalizeKey (String key) {
        String pattern = "^[A-Za-z0-9!#$%&'*+.^_`|~-]+$";
        if(!key.matches(pattern)) {
            throw new IllegalArgumentException("Invalid Header.");
        }

        return key.toLowerCase();
    }

}
