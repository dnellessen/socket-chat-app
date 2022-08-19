package com.dnellessen;

import java.util.ArrayList;
import java.util.HashMap;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;

    int clientsCount;
    ArrayList<String> clientUsernames = new ArrayList<>();
    ArrayList<Socket> clientSockets = new ArrayList<>();
    HashMap<String, PrintWriter> clientOuts = new HashMap<>();

    public class ServerHandler extends Thread {
        String clientUsername;
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

                clientUsername = in.readLine();
                
                System.out.println(clientUsername + " connected");
                if (clientsCount == 1) {
                    System.out.println(clientsCount + " connected user");
                } else {
                    System.out.println(clientsCount + " connected users");
                }

                clientUsernames.add(clientUsername);
                clientSockets.add(clientSocket);
                clientOuts.put(clientUsername, out);

                String clientMsg = "";
                String serverMsg;

                while (clientSocket.isConnected()) {
                    clientMsg = in.readLine();

                    if (clientMsg.equals("exit") || clientMsg.equals("server.close")) {
                        break;
                    }

                    serverMsg = clientUsername + ": " + clientMsg;

                    for (int index = 0; index < clientUsernames.size(); index++) {
                        String username = clientUsernames.get(index);
                        if (!username.equals(clientUsername)) {
                            clientOuts.get(username).println(serverMsg);
                        }
                    }
                }
                if (clientMsg.equals("server.close")) exit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            disconnect(clientUsername, clientSocket);
            close();
        }
        
        void disconnect(String clientUsername, Socket clientSocket) {
            System.out.println(clientUsername + " disconnected");
            clientUsernames.remove(clientUsername);
            clientSockets.remove(clientSocket);
            clientOuts.remove(clientUsername);
            clientsCount--;
            if (clientsCount == 1) {
                System.out.println(clientsCount + " connected user");
            } else {
                System.out.println(clientsCount + " connected users");
            }
        }

        void close() {
            try {
                clientSocket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                exit();
            }
        }
    }

    public void start(int port) throws IOException {
        System.out.println("………………………………………… SERVER …………………………………………");
        serverSocket = new ServerSocket(port);
        System.out.println("Listening on port " + port);

        while (!serverSocket.isClosed()) {
            try {
                new ServerHandler(serverSocket.accept()).start();
            } catch (SocketException e) {
                exit();
            }

            clientsCount++;
        }
    }

    public void exit() {
        try {
            for (Socket socket : clientSockets) {
                socket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8000);
        server.exit();
    }
}
