package org.example.request;


import org.example.httpserver.BadRequestException;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Request {
    private RequestLine requestLine;
    private Headers headers;
    private ByteArrayOutputStream body;
    private ParseState parseState = ParseState.INITIALIZED;
    private StringBuilder buffer = new StringBuilder();
    private int contentLength = -1;

    private int consumedBytes = 0;
    public int parseRequest(byte[] data, int length) throws BadRequestException {
        switch (parseState) {
            case INITIALIZED -> {
                return parseRequestLine(data, length);
            }
            case HEADER -> {
                return parseHeaders(data, length);
            }
            case BODY -> {
                return parseBody(data, length);
            }
        }
        return -1;
    }

    public int parseRequestLine(byte[] data, int length) throws BadRequestException {
        buffer.append(new String(data), 0, length);
        int index = buffer.indexOf("\r\n");
        // if using ncat/telnet then this is fine int index = buffer.indexOf("\n");

        if(index == -1) {
            return 0;
        }
        String[] lines = buffer.substring(0, index).split("\r\n");

        if (lines.length == 0 || lines[0].isEmpty()) {
            throw new BadRequestException("Empty request");
        }

        String currentRequestLine = lines[0];
        String[] parts = currentRequestLine.split(" ");

        if(parts.length != 3) {
            throw new BadRequestException("Invalid Request Line.");
        }

        String method = parts[0];
        String requestTarget = parts[1];
        String httpVersion = parts[2];
        String version = httpVersion.substring("HTTP/".length());

        if(RequestParser.validateRequestLine(method, httpVersion, version)) {
            requestLine.setMethod(method);
            requestLine.setRequestTarget(requestTarget);
            requestLine.setHttpVersion(version);
        }  else {
            throw new BadRequestException("Invalid Request Line.");
        }
        int consumed = index + 2;
        consumedBytes += consumed;
        buffer.delete(0, consumed);
        parseState = ParseState.HEADER;
        if(buffer.indexOf("\r\n") != -1) {
            parseRequest(new byte[0], 0);
        }
        return consumed;
    }
    public int parseHeaders(byte[] data, int length) throws BadRequestException {
        buffer.append(new String(data, 0, length, StandardCharsets.UTF_8));
        int consumed = 0;
        while(buffer.indexOf(":") != -1) {
            int index = buffer.indexOf("\r\n");
            // if using ncat/telnet then this is fine int index = buffer.indexOf("\n");

            if (index == -1) {
                return 0;
            }
            if (index == 0) {
                transitionAfterHeaders();
                return index + 4;
            }

            int delimiter = buffer.toString().indexOf(':');
            if (delimiter == -1) {
                throw new BadRequestException("Invalid Header.");
            }

            if (buffer.toString().charAt(delimiter - 1) == ' ') {
                throw new BadRequestException("WhiteSpace in header");
            }

            String key = RequestParser.normalizeKey(buffer.substring(0, delimiter).trim());
            String value = buffer.substring(delimiter + 1, index).trim();


            if (headers.hasHeader(key)) {
                String valueToSet = headers.getHeader(key) + ", " + value;
                headers.setValues(key, valueToSet);
            } else {
                headers.setValues(key, value);
            }

            consumed = index + 2;
            consumedBytes += consumed;
            buffer.delete(0, index + 2);
            if(buffer.indexOf("\r\n" ) == 1) {
                buffer.delete(0, 3);
                transitionAfterHeaders();
            }
        }

        if((buffer.indexOf("\r\n" ) == 1) || (buffer.indexOf("\r\n" ) == 0)) {
            buffer.delete(0, 3);
            transitionAfterHeaders();
        }

        return consumed;
    }
    public int parseBody(byte[] data, int length) throws BadRequestException {
        int consumed = length;
        consumedBytes += length;
        if((body.size() + length) > contentLength) {
            throw new BadRequestException("Content length is greater then content.");
        }
        body.write(data, 0, length);
        if (body.size() == contentLength) {
            parseState = ParseState.DONE;
            return consumed;
        }
        return consumed;
    }
    private void transitionAfterHeaders () {
        if(headers.hasHeader("content-length")) {
            appendBufferLeftoverToBody();
            setContentLength();
            parseState = ParseState.BODY;
        } else {
            parseState = ParseState.DONE;
        }
    }
    private void setContentLength () {
        String contentLengthHeader = headers.getHeader("content-length");
        try {
            contentLength = Integer.parseInt(contentLengthHeader);
        } catch (NumberFormatException e) {
            System.out.println("Invalid content length.");
        }
    }
    private void appendBufferLeftoverToBody () {
        body.write(buffer.toString().getBytes(), 0, buffer.length());
        buffer.delete(0, buffer.length());
    }

    public Request () {
        requestLine = new RequestLine();
        headers = new Headers();
        body = new ByteArrayOutputStream();
    }
    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public ParseState getParseState() {
        return parseState;
    }

    public void setParseState(ParseState parseState) {
        this.parseState = parseState;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public boolean isDone() {
        return parseState == ParseState.DONE;
    }
    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
    public ByteArrayOutputStream getBody() {
        return body;
    }

    public void setBody(ByteArrayOutputStream body) {
        this.body = body;
    }

    public StringBuilder printRequest () {
        StringBuilder formattedRequest = new StringBuilder();
        formattedRequest.append("REQUEST LINE:\n");
        formattedRequest.append("Request Target: " + this.requestLine.getRequestTarget() +"\r\n");
        formattedRequest.append("Http Version: " + this.requestLine.getHttpVersion() + "\r\n");
        formattedRequest.append("Method: " + this.requestLine.getMethod() + "\r\n");
        formattedRequest.append("HEADERS: \r\n");
        formattedRequest.append(this.headers.getAllHeaders());
        formattedRequest.append("BODY: \r\n");
        formattedRequest.append(this.body);

        return formattedRequest;
    }
}
