package org.example;

import org.example.httpserver.HandlerError;
import org.example.httpserver.RouterMap;
import org.example.httpserver.Server;


public class Main {
    private final static int PORT_NUMBER = 5000;

    public static void main(String[] args) {

        Server server = new Server(PORT_NUMBER);
        RouterMap router = initRouterMap();
        server.start(router);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.close();
            System.out.println("Cleanup finished. Exiting.");
        }));
    }
    private static RouterMap initRouterMap()     {
        RouterMap router = new RouterMap();
        router.addRoute("/yourproblem", (body, request) ->
                new HandlerError(400, "Your problem\n"));

        router.addRoute("/myproblem", (body, request) ->
                new HandlerError(500, "Woopsie, my bad\n"));

        return router;
    }
}
