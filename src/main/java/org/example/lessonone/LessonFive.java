package org.example.lessonone;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LessonFive {

    private static final String POISON_PILL = "__END__";
    public BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
    ExecutorService threads = Executors.newFixedThreadPool(2);

    public Iterable<String> getLinesChannel (InputStream in) {

        threads.execute(() -> {
            try {
                produceLines(in, queue);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return () -> new Iterator<>() {
            String nextLine;

            @Override
            public boolean hasNext() {
                try {
                    nextLine = queue.take();
                    return !nextLine.equals(POISON_PILL);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            @Override
            public String next() {
                return nextLine;
            }
        };
    }

    public void produceLines (InputStream in, BlockingQueue<String> queue) throws InterruptedException {

        byte[] buffer = new byte[8];
        StringBuilder currentLine = new StringBuilder();

        while(true) {
            try {
                int bytesRead = in.read(buffer);
                if(bytesRead == -1) {
                    queue.put(currentLine.toString());
                    queue.put(POISON_PILL);
                    threads.shutdown();
                    break;
                }

                for (int i = 0; i < bytesRead; i++) {
                    if((char)buffer[i] != '\n') {
                        currentLine.append((char)buffer[i]);
                    } else {
                        queue.put(currentLine.toString());
                        currentLine.setLength(0);
                    }
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
