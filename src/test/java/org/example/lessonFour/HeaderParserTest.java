package org.example.lessonFour;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HeaderParserTest {

    @Test
    void validHeader() throws Exception{
        String str = new String ("   Host:    localhost:42069\r\n\r\n");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(str.getBytes())
        );
        assertNotNull(req.getHeaders());
        assertEquals("localhost:42069", req.getHeaders().getHeader("host"));

    }
    @Test
    void invalidWhiteSpaceHeader () throws IOException {
        String str = new String ("       Host : localhost:42069       \r\n\r\n");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Request req = RequestParser.requestFromInputStream(
                    new ByteArrayInputStream(str.getBytes())
            );
        });
        String expectedMessage = "WhiteSpace in header";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void validDone () throws IOException {
        String str = new String ("\r\n");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(str.getBytes())
        );
    }
    @Test
    void validMultipleHeaders () throws IOException {
        String str = new String ("     Host:   localhost\r\nUser-Agent: test\r\nHello: yasser\r\n\r\n");
        Request req = RequestParser.requestFromInputStream(
                new ByteArrayInputStream(str.getBytes())
        );

         assertEquals("localhost", req.getHeaders().getHeader("host"));
         assertEquals("test", req.getHeaders().getHeader("user-agent"));
         assertEquals("yasser", req.getHeaders().getHeader("hello"));


    }
    @Test
    void invalidHeaderWithSpecialCharacter () throws IOException {
        String str = new String ("H_)st: localhost:42069\r\n");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Request req = RequestParser.requestFromInputStream(
                    new ByteArrayInputStream(str.getBytes())
            );
        });
        String expectedMessage = "Invalid Header.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void mutipleValuesForSingleHeader () throws IOException {
        String str = new String ("Host: localhost:42069\r\nHost: yasser\r\n\r\n");
            Request req = RequestParser.requestFromInputStream(
                    new ByteArrayInputStream(str.getBytes())
            );

        assertEquals("localhost:42069, yasser", req.getHeaders().getHeader("host"));
    }
}