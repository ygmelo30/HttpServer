package org.example.httpserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class VideoReader {
    public void sendVideoResponse (OutputStream out) throws IOException, URISyntaxException {
        byte[] body = readVideoBytes();
        ResponseWriter.writeGoodVideoResponse(out, body);

    }
    private byte[] readVideoBytes() throws IOException, URISyntaxException {
        URL resourceUrl = this.getClass().getResource("/vim.mp4");
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found!");
        }
        Path videoPath = Paths.get(resourceUrl.toURI());
        byte[] data = Files.readAllBytes(videoPath);
        return data;
    }
}
