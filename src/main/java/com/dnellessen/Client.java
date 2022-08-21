package com.dnellessen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.Scanner;

public class Client {
    String username;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    
    public void transmitMessage(String msg) {
        out.println(msg);
    }
    
    public String recieveMessage() throws IOException {
        return in.readLine();
    }
    
    public void recieveMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        while (!in.ready());
                        String msg = recieveMessage();
                        System.out.println(msg);
                    } catch (IOException e) {
                        break;
                    }
                }
                exit();
            }

        }).start();
    }
 
    private void setUsername(Scanner userInput) {
        System.out.print("Username  ");
        username = userInput.nextLine();
        out.println(username);
        System.out.println();
    }


    public void start(String host, int port) throws IOException {        
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() {
        Scanner userInput = new Scanner(System.in);

        setUsername(userInput);
        recieveMessages();
        
        String msg;
        while (clientSocket.isConnected()) {
            msg = userInput.nextLine();
            transmitMessage(msg);
            if (msg.equals("!exit")) 
                break;
        }

        userInput.close();
        exit();
    }
    
    public void exit(){
        try {
            clientSocket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) throws IOException {
        System.out.println("………………………………………… USER …………………………………………");
        Client client = new Client();
        client.start("127.0.0.1", 8000);
        client.run();
    }
}
