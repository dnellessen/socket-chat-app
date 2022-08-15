package com.dnellessen;

import java.net.*;
import java.util.Scanner;
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
    
    public void sendMessage(String message) throws IOException {
        out.println(message);
        System.out.println("[Client] Sent message :: " + message);
    }
    
    public String sendMessage(String message, boolean getResponse) throws IOException {
        out.println(message);
        System.out.println("[Client] Sent message :: " + message);
        return recieveMessage();
    }

    public String recieveMessage() throws IOException {
        String serverResponse = in.readLine();
        System.out.println("[Client] Recieved message :: " + serverResponse);
        return serverResponse;
    }

    public void run() throws IOException {
        Scanner userInput = new Scanner(System.in);
        String msg;

        while (true) {
            System.out.print("[User] >> ");
            msg = userInput.nextLine();   
            
            sendMessage(msg);
            if (msg.equals("exit")) break;
            
            recieveMessage();
        }

        userInput.close();

    }
    
    public void exit() throws IOException {
        clientSocket.close();
        out.close();
        in.close();
        System.out.println("[Client] Stopped");
    }
    
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start("127.0.0.1", 8000);
        client.run();
        client.exit();
    }
}
