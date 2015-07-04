package org.amc.game.chessserver;


import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"/SpringTestConfig.xml","/GameServerSecurity.xml",  "/GameServerWebSockets.xml"})
public class SignUpControllerIntegrationTest {

    private static final String NAME = "adrian mclaughlin";
    private static final String USER_NAME = "adrian";
    private static final String PASSWORD = "password";
    private static final String EMAIL_ADDRESS = "adrian@adrianmclaughlin.ie";
    
    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;
    
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.signUpfixture.setUp();
        
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testAddUser() throws Exception {
        
       this.mockMvc.perform(post("/signup").param("fullName", NAME)
                        .param("userName", USER_NAME)
                        .param("password", PASSWORD)
                        .param("emailAddress", EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
       this.mockMvc.perform(post("/signup").param("fullName", "Sarah O'Neill")
                       .param("userName", "sarah")
                       .param("password", "lll")
                       .param("emailAddress", EMAIL_ADDRESS))
                       .andExpect(status().isOk())
                       .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
    }
    
    @Test
    public void testAddSameUser() throws Exception {
        
       this.mockMvc.perform(post("/signup").param("fullName", NAME)
                        .param("userName", USER_NAME)
                        .param("password", PASSWORD)
                        .param("emailAddress", EMAIL_ADDRESS))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
       this.mockMvc.perform(post("/signup").param("fullName", NAME)
                       .param("userName", USER_NAME)
                       .param("password", PASSWORD)
                       .param("emailAddress", EMAIL_ADDRESS))
                       .andExpect(status().isOk())
                       .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR));
       
    }
    
    @Test
    public void testInvalidEmail() throws Exception {
        
       this.mockMvc.perform(post("/signup").param("fullName", NAME)
                        .param("userName", USER_NAME)
                        .param("password", PASSWORD)
                        .param("emailAddress", "eoeoeo"))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(SignUpController.ERRORS_MODEL_ATTR));
    }
}
