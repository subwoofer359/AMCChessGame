package org.amc.game.chessserver.observers

import org.amc.EntityManagerThreadLocal;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(["/GameServerWebSockets.xml", "/SpringTestConfig.xml", "/GameServerSecurity.xml", "/EmailServiceContext.xml"])
class DatabaseUpdateListenerIntegrationTest {

    DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    ServerChessGameDAO scgDAO;
    ServerChessGameFactory scgFactory;
    def gameMap;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Before
    void setUp() {
        fixture.setUp();
        
        scgDAO = (ServerChessGameDAO) wac.getBean("myServerChessGameDAO");
        scgFactory = (ServerChessGameFactory) wac.getBean("serverChessGameFactory");
        gameMap = wac.getBean("gameMap");
    }
    
    @After
    void tearDown() {
        fixture.tearDown();
    }
    
    @Test
    void test() {
        final Long GAME_UID = 1234L;
        Player player = new HumanPlayer("paul");
        Player opponent = new HumanPlayer("sarah");
        AbstractServerChessGame game = scgFactory.getServerChessGame(GameType.NETWORK_GAME, GAME_UID, player);
        
        game.addOpponent(opponent); 
        game.setCurrentStatus(ServerGameStatus.AWAITING_PLAYER);
        
        game = gameMap.get(GAME_UID);
        game.setCurrentStatus(ServerGameStatus.FINISHED);
        
        EntityManagerThreadLocal.closeEntityManager();
        
        def retrievedGame = scgDAO.getServerChessGame(GAME_UID);
        
        assert retrievedGame.getCurrentStatus() == ServerGameStatus.FINISHED;
    }
}
