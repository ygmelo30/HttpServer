package org.example.httpserver.specialendpoint.reader;
import org.example.httpserver.response.ResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

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
