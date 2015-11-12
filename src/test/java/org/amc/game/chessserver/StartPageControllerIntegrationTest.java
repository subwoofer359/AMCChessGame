package org.amc.game.chessserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/GameServerSecurity.xml", "/EmailServiceContext.xml" })

public class StartPageControllerIntegrationTest {
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private static final String PLAYER_SESSION_ATTR = "PLAYER";
    private static final String FULLNAME = "adrian mclaughlin";
    private static final String USERNAME = "adrian";
    private static final String OPPONENT = "Kate Bush";
    
    private Player player;
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer(FULLNAME);
        player.setUserName(USERNAME);
        this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }
    
    
    @Test
    public void testCreateLocalGame() throws Exception {

        this.mockMvc.perform(
                        post("/createGame/")
                                        .sessionAttr(PLAYER_SESSION_ATTR, player)
                                        .param("gameType", GameType.LOCAL_GAME.name())
                                        .param("playersName", OPPONENT))
                         .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(ServerConstants.GAME_UUID))
                        .andExpect(view().name(StartPageController.ONE_VIEW_CHESS_PAGE))
                        .andReturn();

    }

    @Test
    public void testCreateNetworkGame() throws Exception {

        this.mockMvc.perform(
                        post("/createGame/")
                                        .sessionAttr(PLAYER_SESSION_ATTR, player)
                                        .param("gameType", GameType.NETWORK_GAME.name()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(ServerConstants.GAME_UUID))
                        .andExpect(view().name(StartPageController.CHESS_APPLICATION_PAGE))
                        .andReturn();

    }

}
