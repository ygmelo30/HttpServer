package org.example;

import org.example.httpserver.LessonOne;


import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var server = new LessonOne();
        server.run();
    }
}