package com.dnellessen;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        System.out.println("[Server] Create socket on port :: " + port);
        serverSocket = new ServerSocket(port);

        System.out.println("[Server] Listening for connection...");
        clientSocket = serverSocket.accept();
        System.out.println("[Server] Connection accepted");

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        String clientMessage = in.readLine();
        System.out.println("[Server] Recieved message :: " + clientMessage);
        
        String serverResponse;
        
        if (clientMessage.equals("hello server"))
        serverResponse = "hello client";
        else serverResponse = "invalid message, but hello";
        
        out.println(serverResponse);
        System.out.println("[Server] Sent message :: " + serverResponse);
    }
    
    public void stop() throws IOException {
        serverSocket.close();
        clientSocket.close();
        out.close();
        in.close();
        System.out.println("[Server] Stopped");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8000);
        server.stop();
    }
}
