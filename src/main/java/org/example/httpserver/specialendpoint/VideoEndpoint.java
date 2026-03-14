package org.example.httpserver.specialendpoint;

import org.example.httpserver.server.HandlerError;
import org.example.httpserver.specialendpoint.reader.VideoReader;
import org.example.httpserver.response.ResponseHandler;
import org.example.request.Request;

import java.io.OutputStream;
import java.net.Socket;

public class VideoEndpoint implements SpecialEndpoint {
    private ResponseHandler responseHandler = new ResponseHandler();

    @Override
    public boolean matches(String path) {
        if(path.startsWith("/video")) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Request request, Socket socket, OutputStream out) {
        try {
            VideoReader videoReader = new VideoReader();
            String path = request.getRequestLine().getRequestTarget();
            String videoPath = path.substring(path.lastIndexOf("/"));
            videoReader.sendVideoResponse(out, videoPath, "video/mp4");
        } catch (Exception e){
            responseHandler.writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }
}
