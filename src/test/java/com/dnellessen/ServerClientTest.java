package com.dnellessen;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerClientTest {
    private Server server;

    @Before
    public void setUp() throws IOException, InterruptedException {
        server = new Server();
        new Thread(new Runnable() {
            @Override
            public void run() {                
                server = new Server();
                try {
                    server.start(8000);
                } catch (IOException e) {}
            }
        }).start();

        while (!server.isRunning) {
            Thread.sleep(1000);
        }
    }
    
    @Test
    public void whenClientSendsMessage_expectEchoResponse() throws IOException {
        Client client = new Client();
        client.start("127.0.0.1", 8000);
        String response = client.sendMessage("hello server", true);
        assertEquals("You said 'hello server'", response);

        client.exit();
    }

    @Test
    public void whenMultipleClientsConnect_expectCountIncreaseDecrease() throws IOException, InterruptedException {
        Client client1 = new Client();
        Client client2 = new Client();

        assertEquals(0, server.clientsCount);

        client1.start("127.0.0.1", 8000);
        Thread.sleep(1);
        assertEquals(1, server.clientsCount);
        
        client2.start("127.0.0.1", 8000);
        Thread.sleep(1);
        assertEquals(2, server.clientsCount);
        
        client1.sendMessage("exit");
        Thread.sleep(1);
        assertEquals(1, server.clientsCount);
    }

    @After
    public void tearDown() throws IOException {
        server.exit();
    }
}
