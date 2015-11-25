package org.amc.util;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletContext;

public class SpringHostInfoTest {

    private SpringHostInfo springHostInfo;
    private static final int PORT = 2345;
    private static final String HOSTNAME = "23.2.3.2";
    private static final String CONTEXTPATH = "/app/chessgame";
    
    @Mock
    private ServletContext servletContext;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getHostUrlTest() {
        when(servletContext.getContextPath()).thenReturn(CONTEXTPATH);
        
        springHostInfo = new SpringHostInfo(servletContext, HOSTNAME, PORT);
        String expectedString = SpringHostInfo.HTTP + HOSTNAME + ":" + PORT + CONTEXTPATH;
        System.out.println(springHostInfo.getHostUrl());
        assertEquals(expectedString, springHostInfo.getHostUrl());
    }
    
}
