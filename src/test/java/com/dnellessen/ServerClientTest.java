package com.dnellessen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerClientTest {
    @Before
    public void setUp() {
    }
    
    @Test
    public void whenTrue_expectTrue() {
        assertTrue(true);
    }
    
    @Test
    public void whenOne_expectOne() {
        assertEquals("1", "1");
    }

    @After
    public void tearDown() {
    }
}
