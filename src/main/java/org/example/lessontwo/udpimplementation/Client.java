package org.example.lessontwo.udpimplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Client {
    DatagramSocket socket;
    byte[] BUFFER = new byte[1024];
    public Client () {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
           throw new RuntimeException(e.getMessage());
        }
    }
    public void run () {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket packetToSend = new DatagramPacket(BUFFER, 0, BUFFER.length);



        while (true) {
            try {

                BUFFER = reader.readLine().getBytes();
                String input = new String(BUFFER);
                packetToSend.setData(BUFFER);

                System.out.println("Client Input: " + input);
                InetAddress address = InetAddress.getByName("localhost");
                packetToSend.setAddress(address);
                packetToSend.setPort(42069);
                socket.send(packetToSend);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
