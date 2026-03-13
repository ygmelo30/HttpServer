package org.example.httpserver;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoReader {
        public void sendVideoResponse (OutputStream out, String path, String contentType) throws IOException, URISyntaxException {
        byte[] body = readVideoBytes(path);
        ResponseWriter.writeGoodSpecialEndpointResponse(out, body, contentType);
    }
    private byte[] readVideoBytes(String path) throws IOException, URISyntaxException {
        URL resourceUrl = this.getClass().getResource(path + ".mp4");
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found!");
        }
        Path videoPath = Paths.get(resourceUrl.toURI());
        byte[] data = Files.readAllBytes(videoPath);
        return data;
    }
}
