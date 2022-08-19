package com.dnellessen;

import java.util.Scanner;

import java.net.*;
import java.io.*;

public class Client {
    public String username;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public void sendMessage(String message) throws IOException {
        out.println(message);
    }
    
    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void run() throws IOException {
        System.out.println("………………………………………… USER …………………………………………");
        Scanner userInput = new Scanner(System.in);
        System.out.print("Username  ");
        username = userInput.nextLine();
        out.println(username);

        recieveMessages();
        
        String msg;
        while (clientSocket.isConnected()) {
            msg = userInput.nextLine();
            sendMessage(msg);
            if (msg.equals("exit")) break;
        }

        userInput.close();
    }

    public void recieveMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        while (!in.ready());
                        String msg = readMessage();
                        System.out.println(msg);
                    } catch (IOException e) {
                        break;
                    }
                }
            }

        }).start();
    }
    
    public void exit() throws IOException {
        clientSocket.close();
        out.close();
        in.close();
    }
    
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start("127.0.0.1", 8000);
        client.run();
        client.exit();
    }
}
