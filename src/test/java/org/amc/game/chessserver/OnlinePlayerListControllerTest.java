package org.amc.game.chessserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;

import org.amc.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.session.SessionRegistry;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OnlinePlayerListControllerTest {

    private OnlinePlayerListController controller;
    private List<Object> listOfUsers;
    private User userOne = new User();
    private User userTwo = new User();
    private Principal principal = new Principal() {
        
        @Override
        public String getName() {
            return "User";
        }
    };
    
    @Before
    public void setUp() throws Exception {
        listOfUsers = new ArrayList<>();
        userOne.setUserName("adrian");
        userTwo.setUserName("Sarah");
        listOfUsers.add(userOne);
        listOfUsers.add(userTwo);
        SessionRegistry registry = mock(SessionRegistry.class);
        when(registry.getAllPrincipals()).thenReturn(listOfUsers);
        controller = new OnlinePlayerListController();
        controller.setSessionRegistry(registry);
        
    }

    @Test
    public void test() throws Exception {
        Callable<String> callableResult = controller.getOnlinePlayerList();
        String result = callableResult.call();
        Gson gson = new Gson();
        assertEquals(gson.toJson(listOfUsers), result);
    }
    
    @Test
    public void getOnlinePlayerListViaSTOMP() {
        OnlinePlayerListMessager messager =mock(OnlinePlayerListMessager.class);
        controller.messager = messager;
        
        controller.getOnlinePlayerListViaSTOMP(principal);
        
        verify(messager, times(1)).sendPlayerList();
    }

}
