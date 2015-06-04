package org.amc.game.chessserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;

import org.amc.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OnlinePlayerListControllerTest {

    private OnlinePlayerListController controller;
    private SessionRegistry registry;
    private List<Object> listOfUsers;
    private User userOne = new User();
    private User userTwo = new User();
    @Before
    public void setUp() throws Exception {
        listOfUsers = new ArrayList<>();
        userOne.setUserName("adrian");
        userTwo.setUserName("Sarah");
        listOfUsers.add(userOne);
        listOfUsers.add(userTwo);
        registry = mock(SessionRegistry.class);
        when(registry.getAllPrincipals()).thenReturn(listOfUsers);
        controller = new OnlinePlayerListController();
        controller.setSessionRegistry(registry);
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        Callable<String> callableResult = controller.getOnlinePlayerList();
        String result = callableResult.call();
        Gson gson = new Gson();
        assertEquals(gson.toJson(listOfUsers), result);
    }

}
