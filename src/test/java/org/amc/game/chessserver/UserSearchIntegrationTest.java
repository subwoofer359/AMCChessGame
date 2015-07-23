package org.amc.game.chessserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

import org.amc.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/GameServerSecurity.xml" })

public class UserSearchIntegrationTest {
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void test() throws Exception {
        
        MvcResult result = this.mockMvc
                        .perform(post("/searchForUsers").param("searchTerm", "nobby"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(request().asyncStarted())
                        .andReturn();
        MvcResult result2 = this.mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk())
                        .andReturn();
        List<User> userList = (List<User>) result2.getAsyncResult(500000);
        assertTrue(userList.size() == 1);
    }

}
