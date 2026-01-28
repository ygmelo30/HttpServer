package org.example.httpserver;

import org.example.lessonFour.Request;
import org.example.lessonFour.RequestParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private final int port;
    private AtomicBoolean running = new AtomicBoolean(false);
    private ServerSocket serverSocket;
    private Socket client;
    private Thread listeningThread;
    private Handler handler;


    public Server(int port) {
        this.port = port;
    }


    public void start (Handler handler) {
        running.set(true);
        this.handler = handler;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server initialized.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        listeningThread = new Thread(() -> listen());
        listeningThread.start();

    }

    private void listen ()  {


        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            while (running.get() == true) {
                System.out.println("Waiting for a client...");
                client = serverSocket.accept();
                System.out.println("Client accepted.");
                OutputStream outputStream = client.getOutputStream();

                executorService.submit( () -> {
                    try (Socket socket = client) {
                        Request request = RequestParser.requestFromInputStream(socket.getInputStream());
                        if(checkForSpecialEndPoint(request)) {
                            handleSpecialEndPoint(request, socket, outputStream);
                        } else {
                            handleResponse(handler, outputStream, request);
                        }
                    } catch (BadRequestException e) {
                        if (running.get()) e.printStackTrace();
                        writeHandlerError(outputStream, new HandlerError(400, e.getMessage()));
                    } catch (IOException e) {
                        writeHandlerError(outputStream, new HandlerError(500, e.getMessage()));
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Something has gone very wrong.");
            throw new RuntimeException(e.getMessage());
        }
    }


    private void handleResponse (Handler handler, OutputStream out, Request request) {
        ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
        HandlerError handlerError = handler.handle(bodyBuffer, request);

        if(handlerError != null) {
            writeHandlerError(out, handlerError);
        }
        try {
            ResponseWriter.writeGoodResponse(request, out);
        } catch (Exception e) {

        }
    }

    private void writeHandlerError (OutputStream out, HandlerError error)  {
        try {
            ResponseWriter.writeBadResponse(error, out);
        } catch (IOException e) {
            writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }
    private void makeProxyResponse (OutputStream out, Handler handler, Request request, String path, String clientIp)  {
        ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
        HandlerError handlerError = handler.handle(bodyBuffer, request);

        if(handlerError != null) {
            writeHandlerError(out, handlerError);
        }
        try {
            ProxyRequest proxyRequest = new ProxyRequest();
            proxyRequest.sendProxyResponse(out, path, clientIp);
        } catch (Exception e) {
            writeHandlerError(out, new HandlerError(500, e.getMessage()));
        }
    }

    private void handleSpecialEndPoint (Request request, Socket socket, OutputStream outputStream) {
        if(request.getRequestLine().getRequestTarget().startsWith("/httpbin")) {
            String path = request.getRequestLine().getRequestTarget().substring(8);
            String clientIp = socket.getInetAddress().getHostAddress();
            makeProxyResponse(outputStream, handler, request, path, clientIp);
        } else if (request.getRequestLine().getRequestTarget().equals("/video")) {
            try {
                VideoReader videoReader = new VideoReader();
                videoReader.sendVideoResponse(outputStream);
            } catch (Exception e){
                writeHandlerError(outputStream, new HandlerError(500, e.getMessage()));
            }

        }
    }
    private boolean checkForSpecialEndPoint (Request request) {
        if(request.getRequestLine().getRequestTarget().startsWith("/httpbin")
        || request.getRequestLine().getRequestTarget().equals("/video")) {
            return true;
        }
        return false;
    }


    public void close() {
        running.set(false);
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if(listeningThread.isAlive()){
                listeningThread.interrupt();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server shutdown complete.");
    }
}
