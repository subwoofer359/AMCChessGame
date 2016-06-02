package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.dao.DAOInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Callable;



public class UserNameAvailableTest {

    private DAOInterface<User> userDAO;
    private UserNameAvailable controller;
    private static final String USERNAME = "adrian";
    private static final String USERNAME_NOT_VALID = "ed";
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        userDAO = mock(DAO.class);
        controller = new UserNameAvailable();
        controller.setUserDAO(userDAO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAvailable() throws Exception {
        Callable<Boolean> result = controller.isUserNameAvailable(USERNAME);
        assertTrue(result.call());
    }
    
    @Test
    public void testNotAvailable() throws Exception {
        when(userDAO.findEntities(eq("userName"), eq(USERNAME))).thenReturn(Arrays.asList(new User()));
        Callable<Boolean> result = controller.isUserNameAvailable(USERNAME);
        assertFalse(result.call());
    }
    
    @Test
    public void testNotValid() throws Exception {
        Callable<Boolean> result = controller.isUserNameAvailable(USERNAME_NOT_VALID);
        assertFalse(result.call());
    }

}
