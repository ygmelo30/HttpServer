package org.example.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {
    public void sendFileResponse (OutputStream out, String path, String contentType) throws IOException, URISyntaxException {
        byte[] body = readFile(path);
        ResponseWriter.writeGoodSpecialEndpointResponse(out, body, contentType);
    }
    private byte[] readFile(String path) throws IOException, URISyntaxException {
        URL resourceUrl = this.getClass().getResource(path);
        System.out.println("hii:" + path + "- loll:"+resourceUrl);
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found!");
        }
        Path filePath = Paths.get(resourceUrl.toURI());
        byte[] data = Files.readAllBytes(filePath);
        return data;
    }
}
