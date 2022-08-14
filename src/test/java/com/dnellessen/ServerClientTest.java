package com.dnellessen;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
    public void whenClientSendsMessage_expectEchoResponse() throws IOException{
        String response = client.sendMessage("hello server", true);
        assertEquals("You said 'hello server'", response);
    }

    @After
    public void tearDown() throws IOException {
        client.stop();
    }
}
