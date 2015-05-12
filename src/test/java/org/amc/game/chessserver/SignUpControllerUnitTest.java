package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpControllerUnitTest {

    private DAO<User> myUserDAO;
    private SignUpController controller;
    private static final String PASSWORD = "1234";
    private static final String NAME = "Adrian McLaughlin";
    private static final String USER_NAME = "adrian";
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        myUserDAO = mock(DAO.class);
        controller =new SignUpController();
        controller.setUserDAO(myUserDAO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateEntryInUserTable() throws DAOException {
        ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
        Player player = new HumanPlayer(NAME);
        player.setUserName(USER_NAME);
        String password = PASSWORD;
        controller.createEntryInUserTable(player, password);
        verify(myUserDAO).addEntity(userArgument.capture());
        assertEquals(player.getName(), userArgument.getValue().getName());
        assertEquals(player.getUserName(), userArgument.getValue().getUserName());
        assertTrue(Arrays.equals(password.toCharArray(), userArgument.getValue().getPassword()));
        assertEquals(player, userArgument.getValue().getPlayer());
        
    }
    
    @Test
    public void testIsUserNameFree() throws DAOException {
        String freeUserName = USER_NAME;
        when(myUserDAO.findEntities("userName", freeUserName)).thenReturn(new ArrayList<User>());
        assertTrue(controller.isUserNameFree(freeUserName));
        verify(myUserDAO).findEntities("userName", freeUserName);
        
        String takenUserName = "sarah";
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(myUserDAO.findEntities("userName", takenUserName)).thenReturn(users);
        assertFalse(controller.isUserNameFree(takenUserName));
        verify(myUserDAO).findEntities("userName", takenUserName);
    }
    
    @Test
    public void testSignUpFreeUserName() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        when(myUserDAO.findEntities("userName", userName)).thenReturn(new ArrayList<User>());
        
        ModelAndView mav = controller.signUp(name, userName, password);
        verify(myUserDAO).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, "/Login");
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.MESSAGE_MODEL_ATTR,
                        SignUpController.SUCCESS_MSG);
    }

    @Test
    public void testSignUpTakenUserName() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        List<User> users = new ArrayList<>();
        users.add(new User());
        
        when(myUserDAO.findEntities("userName", userName)).thenReturn(users);
        
        ModelAndView mav = controller.signUp(name, userName, password);
        verify(myUserDAO, never()).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, "/Login");
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        SignUpController.USERTAKEN_MSG);
    }
    
    @Test
    public void testSignUpDAOException() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        
        when(myUserDAO.findEntities("userName", userName)).thenReturn(new ArrayList<User>());
        doThrow(new DAOException("Database error")).when(myUserDAO).addEntity(any(User.class));
        
        ModelAndView mav = controller.signUp(name, userName, password);
        ModelAndViewAssert.assertViewName(mav, "/Login");
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        SignUpController.ERROR_MSG);
    }
}
