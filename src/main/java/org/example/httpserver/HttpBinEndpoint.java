package org.example.httpserver;

import org.example.request.Request;

import java.io.OutputStream;
import java.net.Socket;

public class HttpBinEndpoint implements SpecialEndpoint {
    private ResponseHandler responseHandler = new ResponseHandler();
    @Override
    public boolean matches(String path) {
        if(path.startsWith("/httpbin")) {
            return true;
        }
        return false;
    }
    @Override
    public void handle(Request request, Socket socket, OutputStream out) {
        String path = request.getRequestLine().getRequestTarget().substring(8);
        String clientIp = socket.getInetAddress().getHostAddress();
        responseHandler.makeProxyResponse(out, path, clientIp);
    }
}
