package org.example.lessonone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LessonTwo {

    public static void main (String[] args) {

        String filePath = "C:\\Users\\yghul\\OneDrive\\Desktop\\code\\HttpServer\\" +
                "src\\main\\java\\org\\example\\message.txt";
        FileInputStream fileInputStream = null;

        try{

            fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[8];
            StringBuilder currentLine = new StringBuilder();

            while(true) {
                try {

                    int bytesRead = fileInputStream.read(buffer);
                    if(bytesRead == -1) {
                        break;
                    }

                    for (int i = 0; i < bytesRead; i++) {
                        if((char)buffer[i] != '\n') {
                            currentLine.append((char)buffer[i]);
                        } else {
                            System.out.print("read: ");
                            System.out.println(currentLine);
                            currentLine.setLength(0);
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!currentLine.isEmpty()) {
                System.out.println("read: " + currentLine);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
