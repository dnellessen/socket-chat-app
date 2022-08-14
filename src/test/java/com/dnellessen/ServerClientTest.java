package com.dnellessen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerClientTest {
    private Client client;

    @Before
    public void setUp() throws IOException {
        client = new Client();
        client.start("127.0.0.1", 8000);
    }

    @Test
    public void whenClientSendsExistingMessage_expectSpecificResponse() throws IOException{
        String response = client.sendMessage("hello server");
        assertEquals("hello client", response);
    }

    @Test
    public void whenClientSendsNonExistingMessage_expectDefaultResponse() throws IOException{
        String response = client.sendMessage("salve");
        assertEquals("invalid message, but hello", response);
    }

    @After
    public void tearDown() throws IOException {
        client.stop();
    }
}
