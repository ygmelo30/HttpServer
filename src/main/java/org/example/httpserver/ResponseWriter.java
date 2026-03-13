package org.example.httpserver;

import org.example.request.Request;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    public static void writeGoodResponse (Request request, OutputStream outputStream) throws IOException {
        StringBuilder printedRequest = request.printRequest();
        byte[] response = ResponseFactory.generateGoodResponse(printedRequest.toString());

        outputStream.write(response);
        outputStream.flush();
    }
    public static void writeGoodSpecialEndpointResponse (OutputStream outputStream, byte[] body, String contentType) throws IOException {
        byte[] response = ResponseFactory.generateSpecialEndpointResponse(body, contentType);
        outputStream.write(response);
        outputStream.flush();
    }
    public static void writeChunkedBody (OutputStream outputStream, byte[] response, int n) throws IOException {
        outputStream.write(Integer.toHexString(n).getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(response, 0, n);
        outputStream.write("\r\n".getBytes());
    }
    public static void writeChunkedBodyDone (OutputStream outputStream) throws IOException {
        String n = Integer.toHexString(0);
        outputStream.write(n.getBytes());
        outputStream.write("\r\n\r\n".getBytes());
        outputStream.flush();
    }
    public static void writeTrailingHeaders (OutputStream out, byte[] hashedBytes, int size) throws IOException {
        out.write("X-Content-SHA256:".getBytes());
        out.write(hashedBytes);
        out.write("\r\n".getBytes());
        out.write("X-Content-Size:".getBytes());
        out.write("\r\n".getBytes());
        out.write(Integer.toHexString(size).getBytes());
        out.write("\r\n".getBytes());
    }
    public static void writeBadResponse (HandlerError error, OutputStream outputStream) throws IOException {
        byte[] response;
        if(error.getStatusCode() == 400) {
            response = ResponseFactory.generateClientError(error.getMessage().toString());
        } else {
            response = ResponseFactory.generateServerError(error.getMessage().toString());
        }

        outputStream.write(response);
        outputStream.flush();
    }
}
