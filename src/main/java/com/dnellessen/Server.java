package com.dnellessen;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;
    public int clientsCount;
    public boolean isRunning = false;

    public class ServerHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        ServerHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    
                String clientMessage;
                String serverResponse;
        
                while (true) {
                    clientMessage = in.readLine();
                    System.out.println("[Server] Recieved message :: " + clientMessage);
        
                    if (clientMessage.equals("exit")) {
                        System.out.println("[Server] User disconnected");
                        clientsCount--;
                        System.out.println("[Server] " + clientsCount + " connected user(s)");
                        break;
                    }
                    
                    serverResponse = "You said '" + clientMessage + "'";
                    out.println(serverResponse);
                    System.out.println("[Server] Sent message :: " + serverResponse);
                }
                clientSocket.close();
                out.close();
                in.close();
                if (clientsCount == 0) exit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("[Server] Created socket on port :: " + port);
        isRunning = true;

        System.out.println("[Server] Listening for connections...");
        while (true) {
            try { new ServerHandler(serverSocket.accept()).start(); }
            catch (SocketException e) { break; }
            
            System.out.println("[Server] Connection accepted");
            clientsCount++;
            System.out.println("[Server] " + clientsCount + " connected user(s)");
        }
    }
    
    public void exit() throws IOException {
        serverSocket.close();
        isRunning = false;
        System.out.println("[Server] Stopped");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8000);
    }
}
