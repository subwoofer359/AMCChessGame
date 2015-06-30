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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpControllerUnitTest {

    private DAO<User> myUserDAO;
    private SignUpController controller;
    private Errors errors;
    private BCryptPasswordEncoder passwordEncoder;
    private static final String PASSWORD = "1234";
    private static final String EMAIL_ADDRESS = "adrian@adrianmclaughlin.ie";
    private static final String INVALID_EMAIL_ADDRESS = "adrian.adrianmclaughlin.ie";
    private static final String NAME = "Adrian McLaughlin";
    private static final String USER_NAME = "adrian";
    private static final String  VIEW = "redirect:/Login.jsp";
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        myUserDAO = mock(DAO.class);
        controller =new SignUpController();
        controller.setUserDAO(myUserDAO);
        
        passwordEncoder = new BCryptPasswordEncoder();
        controller.setPasswordEncoder(passwordEncoder);
        
        Map<String, Object> errorMap = new HashMap<String, Object>();
		errors = new MapBindingResult(errorMap, "userName");
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
        controller.createEntryInUserTable(player, password, EMAIL_ADDRESS);
        verify(myUserDAO).addEntity(userArgument.capture());
        assertEquals(player.getName(), userArgument.getValue().getName());
        assertEquals(player.getUserName(), userArgument.getValue().getUserName());
        assertTrue(passwordEncoder.matches(String.valueOf(password.toCharArray()),
                        String.valueOf(userArgument.getValue().getPassword())));
        assertEquals(player, userArgument.getValue().getPlayer());
        
    }
   
    @Test
    public void testSignUpFreeUserName() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        when(myUserDAO.findEntities("userName", userName)).thenReturn(new ArrayList<User>());
        
        ModelAndView mav = controller.signUp(errors, name, userName, password, EMAIL_ADDRESS);
        verify(myUserDAO).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
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
        
        ModelAndView mav = controller.signUp(errors, name, userName, password, EMAIL_ADDRESS);
        verify(myUserDAO, never()).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        SignUpController.USERTAKEN_MSG);
    }
    
    @Test
    public void testInValidEmailAddress() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        when(myUserDAO.findEntities("userName", userName)).thenReturn(new ArrayList<User>());
        
        ModelAndView mav = controller.signUp(errors, name, userName, password, INVALID_EMAIL_ADDRESS);
        verify(myUserDAO, never()).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        SignUpController.INVALID_EMAIL_ADDRESS_MSG);
    }
    
    @Test
    public void testSignUpDAOException() throws DAOException{
        String name = "adrian mclaughlin";
        String userName = USER_NAME;
        String password = PASSWORD;
        
        when(myUserDAO.findEntities("userName", userName)).thenReturn(new ArrayList<User>());
        doThrow(new DAOException("Database error")).when(myUserDAO).addEntity(any(User.class));
        
        ModelAndView mav = controller.signUp(errors, name, userName, password, EMAIL_ADDRESS);
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        SignUpController.ERROR_MSG);
    }
}
