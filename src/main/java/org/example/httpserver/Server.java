package org.example.httpserver;

import org.example.request.Request;
import org.example.request.RequestParser;

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
    private RouterMap routerMap;
    private ResponseHandler responseHandler;

    public Server(int port) {
        this.port = port;
    }

    public void start (RouterMap routerMap) {
        running.set(true);
        this.routerMap = routerMap;
        responseHandler = new ResponseHandler();
        responseHandler.buildSpecialEndpointRegistry();
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
                        responseHandler.resolve(request, socket, outputStream, routerMap);
                    } catch (BadRequestException e) {
                        if (running.get()) e.printStackTrace();
                        responseHandler.writeHandlerError(outputStream, new HandlerError(400, e.getMessage()));
                    } catch (IOException e) {
                        responseHandler.writeHandlerError(outputStream, new HandlerError(500, e.getMessage()));
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("If you're reading this, something has gone very wrong.");
            throw new RuntimeException(e.getMessage());
        }
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
