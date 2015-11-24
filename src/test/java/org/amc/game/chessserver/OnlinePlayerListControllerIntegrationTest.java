package org.amc.game.chessserver;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

/**
 * Not working correctly. WebListener HttpSessionEventPublisher
 * Not registry so User list can't checked
 * @author Adrian Mclaughlin
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"/SpringTestConfig.xml", "/UserLogging.xml", "/GameServerSecurity.xml", "/GameServerWebSockets.xml"})
@WithUserDetails("adrian")
public class OnlinePlayerListControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;
 
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                        .apply(springSecurity())
                        .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    

    @Test
    public void test() throws Exception{
        MvcResult result = this.mockMvc.perform(get("/onlinePlayerList"))
                        .andDo(print()).andExpect(status().isOk())
                        .andExpect(request().asyncStarted()).andReturn();

        String userListString = String.valueOf(result.getAsyncResult(5000));
        assertNotNull(userListString);
        assertTrue(userListString.contains("nobby"));
        
        
    }
    
    @Test
    public void UserLogsInOutTest() throws Exception {
        this.mockMvc.perform(formLogin("/login").user("nobby").password("cr2032ux")).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(logout());
        
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn("1");
        
        wac.publishEvent(new HttpSessionCreatedEvent(session));
        wac.publishEvent(new HttpSessionDestroyedEvent(session));
        
        MockPlayerListMessager messager = (MockPlayerListMessager)wac.getBean("mockPlayerListMessager");
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(messager.getSimpMessagingTemplate(),times(2)).convertAndSend(destinationCaptor.capture(),messageCaptor.capture());
        assertEquals(destinationCaptor.getValue(), OnlinePlayerListMessager.MESSAGE_DESTINATION);
    }
}
