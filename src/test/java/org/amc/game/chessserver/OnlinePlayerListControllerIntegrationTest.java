package org.amc.game.chessserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Not working correctly. WebListener HttpSessionEventPublisher
 * Not registry so User list can't checked
 * @author Adrian Mclaughlin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"/SpringTestConfig.xml","/GameServerSecurity.xml"})
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
        MvcResult result = this.mockMvc.perform(get("/onlinePlayerList")).andExpect(status().isOk())
                        .andExpect(request().asyncStarted()).andReturn();
        
        MvcResult result2 = this.mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        System.out.println("--------------->" + result2.getAsyncResult(500000));
        
    }
}
