package org.example.lessonone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LessonOne {

    public static void main (String[] args) {

        String filePath = "C:\\Users\\yghul\\OneDrive\\Desktop\\code\\HttpServer\\" +
                "src\\main\\java\\org\\example\\message.txt";
        FileInputStream fileInputStream = null;

        try{
            fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[8];

            while(true) {
                try {
                    int bytesRead = fileInputStream.read(buffer);
                    if(bytesRead == -1) {
                        break;
                    }

                    for (int i = 0; i < bytesRead; i++) {
                        System.out.print((char) buffer[i]);
                    }
                    System.out.println();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
