package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogOutControllerTest {
    private LogOutController controller;
    private HttpServletRequest request;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        controller = new LogOutController();
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void test() throws ServletException {
        String returnString = controller.logOut(request, session);
        verify(session, times(1)).invalidate();
        verify(request, times(1)).logout();
        assertEquals("redirect:/" ,returnString);
    }

}
