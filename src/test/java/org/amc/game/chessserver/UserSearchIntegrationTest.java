package org.amc.game.chessserver;

import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.amc.User;
import org.amc.dao.UserDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
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
        MvcResult result2 = this.mockMvc.perform(asyncDispatch(result))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
        String jsonString = (String) result2.getAsyncResult(500000);
        
        Gson gson = new Gson();
        Type userType = new TypeToken<List<UserDetails>>(){}.getType();
        List<User> u = gson.fromJson(jsonString, userType);
        assertTrue(u.size() == 1);
    }

}
