package org.example.lessontwo.udpimplementation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    DatagramSocket socket;
    byte[] BUFFER = new byte[1024];

    public Server (int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void run () {
        DatagramPacket receivePacket = new DatagramPacket(BUFFER, BUFFER.length);
        while (true) {
            try {
                socket.receive(receivePacket);
                String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server Received: " + received);
                receivePacket.setLength(BUFFER.length);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
