package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;
import org.amc.dao.ServerChessGameTestDatabaseEntity;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/GameServerSecurity.xml", "/EmailServiceContext.xml" })

public class JoinGameControllerTest {
	
	private static final String PLAYER_SESSION_ATTR = "PLAYER";
	
	private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
	
	private ServerChessGameTestDatabaseEntity entity;
	
	@Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private DatabaseGameMap gameMap;
    
    private MockMvc mockMvc;
    
    @Autowired
    private ServerChessGameDAO serverChessGameDAO;
    
	@Before
	public void setUp() throws Exception {
		signUpfixture.setUp();
		entity = new ServerChessGameTestDatabaseEntity();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@After
	public void tearDown() throws Exception {
		signUpfixture.tearDown();
	}

	@Test
    public void testCreateNetworkGameJoined() throws Exception {

		
        MvcResult result = this.mockMvc.perform(
                        post("/createGame/")
                                        .sessionAttr(PLAYER_SESSION_ATTR, entity.getWhitePlayer())
                                        .param("gameType", GameType.NETWORK_GAME.name()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists(ServerConstants.GAME_UUID))
                        .andExpect(view().name(StartPageController.CHESS_APPLICATION_PAGE))
                        .andReturn();
        
        long gameUUID = (long)result.getModelAndView().getModelMap().get(ServerConstants.GAME_UUID);
        
        EntityManagerThreadLocal.closeEntityManager();
        EntityManagerThreadLocal.getEntityManager();
        
        this.mockMvc.perform(
        		post("/joinGame")
        			.sessionAttr(PLAYER_SESSION_ATTR, entity.getBlackPlayer())
        			.param("gameUUID", String.valueOf(gameUUID)))
        		.andDo(print())
        		.andExpect(model().attributeExists(ServerConstants.GAME))
        		.andExpect(model().attributeExists(ServerConstants.CHESSPLAYER))
        		.andExpect(view().name(ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE));

        EntityManagerThreadLocal.closeEntityManager();
        EntityManagerThreadLocal.getEntityManager();
        
        ServerChessGame gameInDatabase = serverChessGameDAO.getServerChessGame(gameUUID);
        ServerChessGame gameInGameMap = gameMap.get(gameUUID);
        
        assertNotNull(gameInDatabase.getChessGame());
        assertNotNull(gameInGameMap.getChessGame());
    }

}
