package org.example.httpserver;
import org.example.request.Request;
import org.example.request.RequestParser;

import java.io.IOException;
import java.net.ServerSocket;

public class LessonOne {
    private final static int PORT_NUMBER = 5000;

    public void run () {
        try (var serverSocket = new ServerSocket(PORT_NUMBER)) {

            while(true) {
                System.out.println("Waiting for a client...");
                var client = serverSocket.accept();
                System.out.println("Server has started.");
                Request req = RequestParser.requestFromInputStream(client.getInputStream());

                System.out.println(req.getRequestLine().getRequestTarget());
                System.out.println(req.getRequestLine().getHttpVersion());
                System.out.println(req.getHeaders().getHeader("host"));
                System.out.println(req.getHeaders().getHeader("content-length"));
                System.out.println(req.getHeaders().getHeader("connection"));
                System.out.println("size: " + req.getBody().size());
                System.out.println("body:" + req.getBody().toString());
                System.out.println("Server has been closed.");
            }
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (BadRequestException e) {
            System.out.println("Bad request");
        }
    }

}
