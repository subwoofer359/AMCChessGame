package org.amc.game.chessserver;

import com.google.gson.Gson;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.User;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayerListMessagerTest {

    private List<Object> listOfUsers;
    private User userOne = new User();
    private User userTwo = new User();
    private SimpMessagingTemplate template;
    private ArgumentCaptor<String> destinationCaptor;
    private ArgumentCaptor<String> messageCaptor;
    
    private OnlinePlayerListMessager messager;
    
    @Before
    public void setUp() throws Exception {
        destinationCaptor = ArgumentCaptor.forClass(String.class);
        messageCaptor = ArgumentCaptor.forClass(String.class);
        
        listOfUsers = new ArrayList<>();
        userOne.setUserName("adrian");
        userTwo.setUserName("Sarah");
        listOfUsers.add(userOne);
        listOfUsers.add(userTwo);
        
        SessionRegistry registry = mock(SessionRegistry.class);
        template = mock(SimpMessagingTemplate.class);
        
        messager = new OnlinePlayerListMessager();
        messager.setSessionRegistry(registry);
        messager.setSimpMessagingTemplate(template);
        
        when(registry.getAllPrincipals()).thenReturn(listOfUsers);    
    }

    @Test
    public void test() {
        messager.sendPlayerList();
        
        verify(template).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());
        assertEquals(destinationCaptor.getValue(), OnlinePlayerListMessager.MESSAGE_DESTINATION);
        Gson gson = new Gson();
        assertEquals(gson.toJson(listOfUsers), messageCaptor.getValue());
    }

}
