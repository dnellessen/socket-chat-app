package com.dnellessen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    ArrayList<ClientHandler> clientHandlers = new ArrayList<>();


    public class ClientHandler extends Thread {
        String clientUsername;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    
                clientUsername = in.readLine();
                System.out.println(clientUsername + " connected");
                transmitMessage("[Server] " + clientUsername + " connected");
                printNumOfClients();
    
                String clientMsg, serverMsg;
                while (clientSocket.isConnected()) {
                    clientMsg = in.readLine();
    
                    if (clientMsg.equals("!exit")) {
                        break;
                    }
    
                    serverMsg = clientUsername + ": " + clientMsg;
                    transmitMessage(serverMsg);
                }
            } catch (IOException e) {
                exit();
            }

            close();
        }

        private void transmitMessage(String msg) {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler != this) {
                    clientHandler.out.println(msg);
                }
            }
        }

        private int printNumOfClients() {
            int numOfClients = clientHandlers.size();
            if (numOfClients == 1) {
                System.out.println(numOfClients + " connected user");
            } else {
                System.out.println(numOfClients + " connected users");
            }

            return numOfClients;
        }

        void close() {
            clientHandlers.remove(this);
            System.out.println(clientUsername + " disconnected");
            transmitMessage("[Server] " + clientUsername + " disconnected");
            int numOfClients = printNumOfClients();

            try {
                clientSocket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if (numOfClients == 0) {
                exit();
            }
            
        }
    }


    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Listening on port " + port);

        while (!serverSocket.isClosed()) {
            ClientHandler clientHandler;
            try {
                clientHandler = new ClientHandler(serverSocket.accept());
                clientHandlers.add(clientHandler);
                clientHandler.start();
            } catch (SocketException e) {
                exit();
            }
        }
    }

    public void exit() {
        if (serverSocket.isClosed())
            return;
            
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.close();
            }
            serverSocket.close();
            System.out.println("Server closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("………………………………………… SERVER …………………………………………");
        Server server = new Server();
        server.start(8000);
    }
}
