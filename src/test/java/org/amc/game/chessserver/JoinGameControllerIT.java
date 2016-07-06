package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.amc.dao.SCGDAOInterface;
import org.amc.dao.ServerChessGameDatabaseEntityFixture;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
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

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/EntityManagerFactory.groovy", "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" })

public class JoinGameControllerIT {
	
	private static final String PLAYER_SESSION_ATTR = "PLAYER";
	
	private DatabaseFixture signUpfixture = new DatabaseFixture();
	
	private ServerChessGameDatabaseEntityFixture entity;
	
	@Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;
    
    @Resource(name="myServerChessGameDAO")
    private SCGDAOInterface serverChessGameDAO;
    
	@Before
	public void setUp() throws Exception {
		signUpfixture.setUp();
		entity = new ServerChessGameDatabaseEntityFixture(signUpfixture.getNewEntityManager());
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

        this.mockMvc.perform(
        		post("/joinGame")
        			.sessionAttr(PLAYER_SESSION_ATTR, entity.getBlackPlayer())
        			.param("gameUUID", String.valueOf(gameUUID)))
        		.andDo(print())
        		.andExpect(model().attributeExists(ServerConstants.GAME))
        		.andExpect(model().attributeExists(ServerConstants.CHESSPLAYER))
        		.andExpect(view().name(ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE));
        
        AbstractServerChessGame gameInGameMap = serverChessGameDAO.getServerChessGame(gameUUID);
        assertNotNull(gameInGameMap.getChessGame());
        assertEquals(ServerGameStatus.IN_PROGRESS, gameInGameMap.getCurrentStatus());
        assertEquals(gameUUID, gameInGameMap.getUid());
    }

}
