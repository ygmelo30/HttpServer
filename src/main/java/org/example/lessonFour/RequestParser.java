package org.example.lessonFour;

import org.example.httpserver.BadRequestException;

import java.io.IOException;
import java.io.InputStream;

public class RequestParser {

    public static void main (String[] args) throws IOException, BadRequestException {
        String raww = "    Host:   localhost\r\nUser-Agent: test\r\nHello: yasser\r\n\r\n";
        String raw1 = "GET / HTTP/1.1\r\nHost: localhost:42069\\r\\nUser-Agent: curl/7.81.0\\r\\nAccept: */*\\r\\n\\r\\n\",\n";
        String raw = "GET /coffee HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n";
        String raw2 = "POST /submit HTTP/1.1\r\nHost: localhost:42069\r\nContent-Length: 13\r\n \r\nhello world!\n";
//        Request req = RequestParser.requestFromInputStream(
//                new ByteArrayInputStream(raw.getBytes())
//        );
//        System.out.println(req.getRequestLine().getMethod());
//        System.out.println(req.getRequestLine().getRequestTarget());
//        System.out.println(req.getRequestLine().getHttpVersion());


        ChunkInputStream reader = new ChunkInputStream(raw2, 4);
        Request req = RequestParser.requestFromInputStream(reader);
                System.out.println(req.getRequestLine().getMethod());
        System.out.println(req.getRequestLine().getRequestTarget());
        System.out.println(req.getRequestLine().getHttpVersion());
        System.out.println(req.getHeaders().getHeader("host"));
        System.out.println(req.getHeaders().getHeader("content-length"));
        System.out.println("size: " + req.getBody().size());
        System.out.println("body:" + req.getBody().toString());

    }
    public static Request requestFromInputStream(InputStream input) throws IOException, BadRequestException {
        Request request = new Request();
        byte[] buffer = new byte[8];
        //request.setState(ParseState.INITIALIZED);
        int n;
        while((n = input.read(buffer)) != -1) {
            request.parseRequest(buffer, n);

            if(request.isDone()) {
                break; // Exit immediately when done
            }
        }

//        while(!request.isDone()) {
//            System.out.println("inside here for noww");
//            int n = input.read(buffer);
//
//            if(n == -1 && !request.isDone()) {
//                System.out.println("hello");
//                request.parseRequest(new byte[0], 0);
//                break;
//            } else if(request.isDone()) {
//                System.out.println("helloo");
//                break;
//            }
//            System.out.println("hellooo");
//            request.parseRequest(buffer, n);
//        }
        return request;
    }

    public static boolean validateRequestLine(String method, String requestTarget, String httpVersion, String version) {
        if(!method.matches("[A-Z]+")) {
            System.out.println("method is: " + method);
            throw new IllegalArgumentException("Invalid Method.");
        }

        if(!httpVersion.startsWith("HTTP/")) {
            System.out.println("http is: " + httpVersion);
            throw new IllegalArgumentException("Invalid Http Version.");
        }

        if(!version.equals("1.1")) {
            throw new IllegalArgumentException("Only Http1.1 is supported.");
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
