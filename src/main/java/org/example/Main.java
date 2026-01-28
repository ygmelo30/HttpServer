package org.example;

import org.example.lessontwo.LessonOne;


import java.io.FileNotFoundException;



public class Main {
    public static void main(String[] args) throws FileNotFoundException {

         // run tcp server
        var server = new LessonOne();
        server.run();

        // run udp server
//        var client = new Client();
//        var server  = new Server(42069);
//        client.run();
//        server.run();


//        LessonFive lessonFive = new LessonFive();
//        String filePath = "C:\\Users\\yghul\\OneDrive\\Desktop\\code\\HttpServer\\" +
//                "src\\main\\java\\org\\example\\message.txt";
//
//        FileInputStream fileInputStream = new FileInputStream(filePath);
//
//        for (String line : lessonFive.getLinesChannel(fileInputStream)) {
//            System.out.println("Got: " + line);
//        }

    }
}