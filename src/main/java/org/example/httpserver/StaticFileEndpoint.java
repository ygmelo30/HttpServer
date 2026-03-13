package org.example.httpserver;

import org.example.request.Request;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class StaticFileEndpoint implements SpecialEndpoint{
    private static final Map<String, String> MIME_TYPES = Map.of(
            "html", "text/html",
            "css", "text/css",
            "js", "application/javascript",
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "gif", "image/gif",
            "mp4", "video/mp4"
    );
    private ResponseHandler responseHandler = new ResponseHandler();

    @Override
    public boolean matches(String path) {
        if(path.startsWith("/static/")) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Request request, Socket socket, OutputStream out) {
        try {
            String path = request.getRequestLine().getRequestTarget();
            String pathResource = path.substring(path.lastIndexOf("/"));
            String extension = path.substring(path.lastIndexOf('.') + 1);
            String contentType = MIME_TYPES.getOrDefault(extension, "application/octet-stream");
            FileReader fileReader = new FileReader();
            fileReader.sendFileResponse(out, pathResource, contentType);
        } catch (Exception e){
            responseHandler.writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }
}
