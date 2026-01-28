package org.example.lessonFour;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderRequestLineTest {
    @Test
    void goodGetRequestWithOnlyRequestLineTest () throws IOException {
        String raw = ("GET / HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(raw.getBytes())
        );
        assertEquals("localhost:42069", req.getHeaders().getHeader("host"));
        assertEquals("curl/7.81.0", req.getHeaders().getHeader("user-agent"));
        assertEquals("*/*", req.getHeaders().getHeader("accept"));
        assertEquals( "1.1", req.getRequestLine().getHttpVersion());
        assertEquals( "/", req.getRequestLine().getRequestTarget());
        assertEquals("GET", req.getRequestLine().getMethod());


    }
    @Test
    void goodBody () throws IOException {
        String raw = "POST /submit HTTP/1.1\r\nHost: localhost:42069\r\nContent-Length: 13\r\n \r\nhello world!\n";
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(raw.getBytes())
        );
        for (int i = 0; i < req.getBody().size(); i++) {
            System.out.print((int) req.getBody().toString().charAt(i));
        }
        for (int i = 0; i < "hello world!\r\n".length(); i++) {
            System.out.print((int) "hello world!\r\n".charAt(i));
        }
        assertEquals("localhost:42069", req.getHeaders().getHeader("host"));
        assertEquals( "1.1", req.getRequestLine().getHttpVersion());
        assertEquals( "/submit", req.getRequestLine().getRequestTarget());
        assertEquals("POST", req.getRequestLine().getMethod());
        //assertEquals("hello world!\n", req.getBody());
        assertEquals(
                Arrays.toString("hello world!\n".getBytes(StandardCharsets.UTF_8)),
                Arrays.toString(req.getBody().toByteArray())
        );
    }
    @Test
    void badContentLength () throws IOException {
        String raw = "POST /submit HTTP/1.1\r\nHost: localhost:42069\r\nContent-Length: 14\r\n \r\nhello world!\n";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Request req = RequestParser.requestFromInputStream(
                    new ByteArrayInputStream(raw.getBytes())
            );
            System.out.println("grrr" +req.getBody().size() +  req.getBody());
        });
        String expectedMessage = "Content length is greater then content.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
