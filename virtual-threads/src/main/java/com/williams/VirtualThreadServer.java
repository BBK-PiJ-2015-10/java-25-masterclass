package com.williams;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualThreadServer {

    private final int PORT = 8080;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void run() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.log(Level.INFO, "Server started on port {0}", String.valueOf(PORT));

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> handleRequest(socket));
            }

        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Unable to start due to {0}", ioException);
        }

    }

    public void handleRequest(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String line = in.readLine();
            if (line != null) {
                String.format("Received request %s on thread with id %d", line, Thread.currentThread().getId());
                logger.log(Level.INFO, String.format("Received request %s on thread with id %d", line, Thread.currentThread().getId()));
                String responseBody = "Request processed successfully";
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                        "Content-Type: text/plain\r\n\r\n" +
                        responseBody;
                out.write(response);
                out.flush();
            }
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Unable to process request due to {}", exception);
        }
    }

}
