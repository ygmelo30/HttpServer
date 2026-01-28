package org.example.lessontwo.udpimplementation;

public class ServerMain {
        public static void main(String[] args) {
            System.out.println("hellooo again");
            Server server = new Server(42069);
            server.run();
        }
    }

