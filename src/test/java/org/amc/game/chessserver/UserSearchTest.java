package org.amc.game.chessserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.UserDetails;
import org.amc.dao.UserSearchDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;


public class UserSearchTest {

    private UserSearchController controller;
    private String searchTerm = "adrian";
    private UserDetails user;
    private UserSearchDAO userDAO;
    private static final String FULLNAME = "adrian mclaughlin";
    private static final String USERNAME = "adrian";

    
    @Before
    public void setUp() throws Exception {
        controller = new UserSearchController();
        userDAO = mock(UserSearchDAO.class);
        controller.setUserDAO(userDAO);
        user = new UserDetails(USERNAME, FULLNAME);
        
        when(userDAO.findUsers(eq(searchTerm))).thenReturn(Arrays.asList(user));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception{
        Gson gson = new Gson();
    
        Callable<String> userList = controller.searchForUsers(searchTerm);
        String jsonString = userList.call(); 
        assertNotNull(jsonString);
        assertNotEquals("", jsonString);
        Type userType = new TypeToken<List<UserDetails>>(){}.getType();
        List<UserDetails> u = gson.fromJson(jsonString, userType);
        assertTrue(u.size() == 1);
        UserDetails jsonUser = (UserDetails)u.get(0);
        assertEquals(USERNAME, jsonUser.getUserName());
        assertEquals(FULLNAME, jsonUser.getFullName());
    }

}
