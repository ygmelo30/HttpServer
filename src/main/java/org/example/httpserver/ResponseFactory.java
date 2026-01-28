package org.example.httpserver;

import java.nio.charset.StandardCharsets;

public class ResponseFactory {
    public static byte[] generateGoodResponse (String body) {
       byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);


        String headers =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, response, headerBytes.length, bodyBytes.length);

        return response;

    }
    public static byte[] generateProxyResponse () {

        String headers =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Transfer-Encoding: chunked\r\n" +
                        "Trailer: X-Content-SHA256, X-Content-Size\r\n" +
                        "\r\n";

        byte[] response = headers.getBytes();
        return response;
    }
    public static byte[] generateVideoResponse (byte[] body) {

        String headers =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: video/mp4\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(body, 0, response, headerBytes.length, body.length);
        return response;
    }
    public static byte[] generateClientError (String body) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);


        String headers =
                "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, response, headerBytes.length, bodyBytes.length);

        return response;

    }
    public static byte[] generateServerError (String body) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String headers =
                "HTTP/1.1 500 Internal Server Error\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, response, headerBytes.length, bodyBytes.length);

        return response;

    }
}
