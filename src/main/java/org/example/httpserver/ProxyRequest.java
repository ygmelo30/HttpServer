package org.example.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProxyRequest {
    public static byte[] generateProxyRequest(String path, String clientIp) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://httpbin.org" + path)).
                    version(HttpClient.Version.HTTP_1_1).
                    header("X-Forwarded-For", clientIp).
                    GET().
                    build();

            //HttpClient client = HttpClient.newHttpClient();

            HttpResponse<byte[]> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());

            return response.body();

        } catch (Exception e) {

        }

        return new byte[0];
    }
    public void sendProxyResponse (OutputStream out, String path, String clientIp) throws IOException, NoSuchAlgorithmException {

        out.write(ResponseFactory.generateProxyResponse());

        byte[] response = ProxyRequest.generateProxyRequest(path, clientIp);
        ByteBuffer stream = ByteBuffer.wrap(response);
        ByteArrayOutputStream fullResponse = new ByteArrayOutputStream();
        fullResponse.write(response);

        int n = 8;
        byte[] byteArray = new byte[n];
        stream.get(byteArray);

        while (stream.hasRemaining()) {
            if(stream.limit() - stream.position() < n) {
                n = stream.limit() - stream.position();
            }
            stream.get(byteArray, 0, n);
            System.out.println("sending: "+ new String(byteArray) + " of size: " + n);
            ResponseWriter.writeChunkedBody(out, byteArray, n);
            byteArray = new byte[n];
        }
        ResponseWriter.writeChunkedBodyDone(out);
        handleTrailingHeadersResponse(out, fullResponse);
    }

    public void handleTrailingHeadersResponse (OutputStream out, ByteArrayOutputStream fullResponse) throws NoSuchAlgorithmException, IOException {
        int size = fullResponse.size();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(fullResponse.toByteArray());
        ResponseWriter.writeTrailingHeaders(out, hashedBytes, size);


    }
}
