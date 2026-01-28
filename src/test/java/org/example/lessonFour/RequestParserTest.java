package org.example.lessonFour;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    @Test
    void goodGetRequestWithoutPathTest() throws Exception {
        String raw= ("GET / HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n");

        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(raw.getBytes())
        );

        assertNotNull(req);
        assertEquals("GET", req.getRequestLine().getMethod());
        assertEquals("/", req.getRequestLine().getRequestTarget());
        assertEquals("1.1", req.getRequestLine().getHttpVersion());
    }
    @Test
    void goodGetRequestWithPathTest() throws Exception {
        String raw= ("GET /coffee HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(raw.getBytes())
        );

        assertNotNull(req);
        assertEquals("GET", req.getRequestLine().getMethod());
        assertEquals("/coffee",req.getRequestLine().getRequestTarget());
        assertEquals("1.1", req.getRequestLine().getHttpVersion());
    }

    @Test
    void goodGetRequestWithOnlyRequestLineTest () throws IOException{
        String raw = ("GET / HTTP/1.1\r\nHost:");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(raw.getBytes())
        );
        assertEquals("GET", req.getRequestLine().getMethod());
        assertEquals( "1.1", req.getRequestLine().getHttpVersion());
        assertEquals( "/", req.getRequestLine().getRequestTarget());


    }


    @Test
    void invalidNumberOfPartsGetReqeustTest () throws IOException {
        String raw = ("/coffee HTTP/1.1\r\nHost: localhost:42069\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\n");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Request req = RequestParser.requestFromInputStream(
                    new ByteArrayInputStream(raw.getBytes())
            );
        });
        String expectedMessage = "Invalid Request Line";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}