package com.dnellessen;

import java.net.*;
import java.io.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        System.out.println("[Client] Created stream socket on port :: " + port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public String sendMessage(String message) throws IOException {
        out.println(message);
        System.out.println("[Client] Sent message :: " + message);
        String serverResponse = in.readLine();
        System.out.println("[Client] Recieved message :: " + serverResponse);
        return serverResponse;
    }
    
    public void stop() throws IOException {
        clientSocket.close();
        out.close();
        in.close();
        System.out.println("[Client] Stopped");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start("127.0.0.1", 8000);
        client.sendMessage("hello server");
        client.stop();
    }
}
