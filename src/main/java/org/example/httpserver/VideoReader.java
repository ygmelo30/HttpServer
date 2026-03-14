package org.example.httpserver;
import java.io.IOException;
import java.io.InputStream;
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
    private byte[] readVideoBytes(String path) throws IOException{
        InputStream stream = getClass().getResourceAsStream(path + ".mp4");
        if (stream == null) {
            throw new RuntimeException("Resource not found!");
        }
        byte[] data = stream.readAllBytes();
        return data;
    }
}
