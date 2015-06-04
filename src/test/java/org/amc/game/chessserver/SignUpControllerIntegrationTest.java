package org.amc.game.chessserver;


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
@ContextConfiguration(locations={"/SpringTestConfig.xml","/GameServerSecurity.xml"})
public class SignUpControllerIntegrationTest {

    private static final String NAME = "adrian mclaughlin";
    private static final String USER_NAME = "adrian";
    private static final String PASSWORD = "password";
    
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
        
       this.mockMvc.perform(post("/signup").param("name", NAME)
                        .param("userName", USER_NAME)
                        .param("password", PASSWORD))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
       this.mockMvc.perform(post("/signup").param("name", "Sarah O'Neill")
                       .param("userName", "sarah")
                       .param("password", "lll"))
                       .andExpect(status().is3xxRedirection())
                       .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
    }
    
    @Test
    public void testAddSameUser() throws Exception {
        
       this.mockMvc.perform(post("/signup").param("name", NAME)
                        .param("userName", USER_NAME)
                        .param("password", PASSWORD))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(model().attributeExists(SignUpController.MESSAGE_MODEL_ATTR));
       this.mockMvc.perform(post("/signup").param("name", NAME)
                       .param("userName", USER_NAME)
                       .param("password", PASSWORD))
                       .andExpect(status().is3xxRedirection())
                       .andExpect(model().attribute(SignUpController.ERRORS_MODEL_ATTR,SignUpController.USERTAKEN_MSG));
       
    }
}
