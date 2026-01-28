package org.example.httpserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerImpl {
    private final static int PORT_NUMBER = 5000;


    public static void main(String[] args) {

        Server server = new Server(PORT_NUMBER);

        Handler handler = (body, request) -> {
            String target = request.getRequestLine().getRequestTarget();
            if(target.equals("/hi")) {
                return new HandlerError(400, "Your");
            }
            if ("/myproblem".equals(target)) {
                return new HandlerError(500, "Woopsie, my bad\n");
            }
            try {
                body.write("All good, frfr\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                return new HandlerError(500, "Failed to write response\n");
            }

            return null;
        };

        server.start((handler));


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.close();
            System.out.println("Cleanup finished. Exiting.");
        }));




    }
}
