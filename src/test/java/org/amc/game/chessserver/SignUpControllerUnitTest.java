package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.SignUpController.UserForm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

public class SignUpControllerUnitTest {

    private DAO<User> myUserDAO;
    private SignUpController controller;
    private BCryptPasswordEncoder passwordEncoder;
    private static final String PASSWORD = "1234";
    private static final String EMAIL_ADDRESS = "adrian@adrianmclaughlin.ie";
    private static final String INVALID_EMAIL_ADDRESS = "adrian.adrianmclaughlin.ie";
    private static final String NAME = "Adrian McLaughlin";
    private static final String USER_NAME = "adrian";
    private static final String  VIEW = "forward:/Login.jsp";
    private static final UserForm userForm = new UserForm();
    private BindingResult bindingResult;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        myUserDAO = mock(DAO.class);
        controller =new SignUpController();
        controller.setUserDAO(myUserDAO);
        
        bindingResult = mock(BindingResult.class);
        passwordEncoder = new BCryptPasswordEncoder();
        controller.setPasswordEncoder(passwordEncoder);
        
        userForm.setFullName("adrian mclaughlin");
        userForm.setUserName(USER_NAME);
        userForm.setPassword(PASSWORD);
        userForm.setEmailAddress(EMAIL_ADDRESS);
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
        when(myUserDAO.findEntities("userName", userForm.getUserName())).thenReturn(new ArrayList<User>());
        
        ModelAndView mav = controller.signUp(userForm, bindingResult);
        verify(myUserDAO).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.MESSAGE_MODEL_ATTR,
                        SignUpController.SUCCESS_MSG);
    }

    @Test
    public void testSignUpTakenUserName() throws DAOException{
        List<User> users = new ArrayList<>();
        users.add(new User());
        
        when(myUserDAO.findEntities("userName", userForm.getUserName())).thenReturn(users);
        when(bindingResult.hasErrors()).thenReturn(true);
        
        ModelAndView mav = controller.signUp(userForm, bindingResult);
        verify(myUserDAO, never()).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        bindingResult);
        ModelAndViewAssert.assertModelAttributeValue(mav, "userForm",
                        userForm);
    }
    
    @Test
    public void testInValidEmailAddress() throws DAOException{
        when(myUserDAO.findEntities("userName", userForm.getUserName())).thenReturn(new ArrayList<User>());
        when(bindingResult.hasErrors()).thenReturn(true);
        
        userForm.setEmailAddress(INVALID_EMAIL_ADDRESS);
        
        ModelAndView mav = controller.signUp(userForm, bindingResult);
        verify(myUserDAO, never()).addEntity(any(User.class));
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.ERRORS_MODEL_ATTR,
                        bindingResult);
        ModelAndViewAssert.assertModelAttributeValue(mav, "userForm",
                        userForm);
    }
    
    @Test
    public void testSignUpDAOException() throws DAOException{
        when(myUserDAO.findEntities("userName", userForm.getUserName())).thenReturn(new ArrayList<User>());
        doThrow(new DAOException("Database error")).when(myUserDAO).addEntity(any(User.class));
        
        ModelAndView mav = controller.signUp(userForm, bindingResult);
        ModelAndViewAssert.assertViewName(mav, VIEW);
        ModelAndViewAssert.assertModelAttributeValue(mav, SignUpController.MESSAGE_MODEL_ATTR,
                        SignUpController.ERROR_MSG);
    }
}
