package org.example.httpserver.response;

import org.example.httpserver.server.HandlerError;
import org.example.httpserver.server.RouterMap;
import org.example.httpserver.specialendpoint.*;
import org.example.request.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ResponseHandler {
    private SpecialEndpointRegistry specialEndpointRegistry1;

    public void resolve(Request request, Socket socket, OutputStream out, RouterMap routerMap) {
        try  {
            SpecialEndpoint specialEndpoint = specialEndpointRegistry1.resolve(request.getRequestLine().getRequestTarget());
            if(specialEndpoint != null) {
                handleSpecialEndpoint(specialEndpoint, request, socket, out);
            } else {
                handleResponse(routerMap, out, request);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }

    public void handleResponse (RouterMap routerMap, OutputStream out, Request request) throws IOException {
        ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
        HandlerError handlerError = routerMap.resolve(request.getRequestLine().getRequestTarget()).handle(bodyBuffer, request);
        if(handlerError != null) {
            writeHandlerError(out, handlerError);
        }
        try {
            ResponseWriter.writeGoodResponse(request, out);
        } catch (Exception e) {
            System.err.println("Error writing good response." + e.getMessage());
        }
    }
    public void writeHandlerError (OutputStream out, HandlerError error)  {
        try {
            ResponseWriter.writeBadResponse(error, out);
        } catch (IOException e) {
            System.err.println("Error writing error response." + e.getMessage());
        }
    }
    public void makeProxyResponse (OutputStream out, String path, String clientIp)  {
        try {
            ProxyRequest proxyRequest = new ProxyRequest();
            proxyRequest.sendProxyResponse(out, path, clientIp);
        } catch (Exception e) {
            writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }
    public void handleSpecialEndpoint(SpecialEndpoint specialEndpoint, Request request, Socket socket, OutputStream out) {
        specialEndpoint.handle(request, socket, out);
    }
    public void buildSpecialEndpointRegistry() {
        specialEndpointRegistry1 = new SpecialEndpointRegistry();
        registerSpecialEndpoint(new HttpBinEndpoint());
        registerSpecialEndpoint(new VideoEndpoint());
        registerSpecialEndpoint(new StaticFileEndpoint());
    }
    public void registerSpecialEndpoint(SpecialEndpoint endpoint) {
        specialEndpointRegistry1.register(endpoint);
    }
}
