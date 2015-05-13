package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.User;
import org.amc.dao.DAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.concurrent.Callable;



public class UserNameAvailableTest {

    private DAO<User> userDAO;
    private UserNameAvailable controller;
    private static final String USERNAME = "adrian";
    private static final String USERNAME_NOT_VALID = "ed";
    
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
        Model model = new ExtendedModelMap();
        Callable<String> result = controller.isUserNameAvailable(model, USERNAME);
        assertEquals(UserNameAvailable.USERNAME_JSP, result.call());
        assertTrue((Boolean)model.asMap().get(UserNameAvailable.USERNAME_MODEL_ATTR));
    }
    
    @Test
    public void testNotAvailable() throws Exception {
        when(userDAO.findEntities(eq("userName"), eq(USERNAME))).thenReturn(Arrays.asList(new User()));
        Model model = new ExtendedModelMap();
        Callable<String> result = controller.isUserNameAvailable(model, USERNAME);
        assertEquals(UserNameAvailable.USERNAME_JSP, result.call());
        assertFalse((Boolean)model.asMap().get(UserNameAvailable.USERNAME_MODEL_ATTR));
    }
    
    @Test
    public void testNotValid() throws Exception {
        Model model = new ExtendedModelMap();
        Callable<String> result = controller.isUserNameAvailable(model, USERNAME_NOT_VALID);
        assertEquals(UserNameAvailable.USERNAME_JSP, result.call());
        assertFalse((Boolean)model.asMap().get(UserNameAvailable.USERNAME_MODEL_ATTR));
    }

}
