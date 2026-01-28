package org.example.lessontwo;
import org.example.httpserver.BadRequestException;
import org.example.lessonFour.Request;
import org.example.lessonFour.RequestParser;
import org.example.lessonone.LessonFive;

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
                //var clientInput = new LessonFive();
                //clientInput.getLinesChannel(client.getInputStream());
                Request req = RequestParser.requestFromInputStream(client.getInputStream());

//                for (String line : clientInput.getLinesChannel(client.getInputStream())) {
//                    if(line != null) {
//                        System.out.println("Got: " + line);
//                    }
//                }
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
            System.out.println("eek " + e.getMessage());
            throw new RuntimeException();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

}
