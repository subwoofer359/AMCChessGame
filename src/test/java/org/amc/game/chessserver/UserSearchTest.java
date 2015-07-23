package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.User;
import org.amc.dao.DAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;


public class UserSearchTest {

    private UserSearchController controller;
    private String searchTerm = "adrian";
    private User user;
    private DAO<User> userDAO;
    
    @Before
    public void setUp() throws Exception {
        controller = new UserSearchController();
        userDAO = mock(DAO.class);
        controller.setUserDAO(userDAO);
        user = new User();
        user.setName("Adrian McLaughlin");
        user.setUserName("adrian");
        user.setEmailAddress("adrian@adrianmclaughlin.ie");
        
        when(userDAO.findEntities(eq("userName"), eq(searchTerm))).thenReturn(Arrays.asList(user));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception{
        Callable<List<User>> userList = controller.searchForUsers(searchTerm);
        List<User> list = userList.call(); 
        assertNotNull(list);
        assertEquals(1, list.size());
    }

}
