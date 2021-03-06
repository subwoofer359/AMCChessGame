package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chessserver.DatabaseFixture;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/EntityManagerFactory.groovy", "/SpringTestConfig.xml", "/EmailServiceContext.xml", "/GameServerSecurity.xml", "/GameServerWebSockets.xml", "/GameObservers.xml"})
public class ServerChessGameDAOIT {

    @Autowired
    private WebApplicationContext wac;
    
    private static DatabaseFixture signUpfixture = new DatabaseFixture();
    private static ServerChessGameDatabaseEntityFixture serverChessGamesfixture;
    private SCGDAOInterface dao;
    private EntityManagerCache emCache;
    private ServerChessGameFactory scgFactory;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        signUpfixture.setUp();
        serverChessGamesfixture = new ServerChessGameDatabaseEntityFixture(signUpfixture.getNewEntityManager());
    }
    
    @Before
    public void setUp() throws Exception {
        
        dao = (SCGDAOInterface) wac.getBean("myServerChessGameDAO");
        ObserverFactoryChain chain = (ObserverFactoryChain) wac.getBean("defaultObserverFactoryChain");
        emCache = (EntityManagerCache) wac.getBean("myEntityManagerCache");
        dao.setObserverFactoryChain(chain);
        
        
        scgFactory = (ServerChessGameFactory) wac.getBean("serverChessGameFactory");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        signUpfixture.tearDown();
    }

    @Test
    public void getServerChessGameTest() throws DAOException {
        AbstractServerChessGame scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
        scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
    }
    
    @Test
    public void getServerChessGameEntityManagerClosedTest() throws DAOException {
        AbstractServerChessGame scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
        
        emCache.getEntityManager(scgGame.getUid()).close();
        scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
    }
    
    @Test
    public void testSaveServerChessGame() throws Exception {
        final long GAME_UID = 12344L;
        AbstractServerChessGame scgGame = scgFactory.getServerChessGame(GameType.NETWORK_GAME, GAME_UID , serverChessGamesfixture.getWhitePlayer());
        
        dao.saveServerChessGame(scgGame);
        
        emCache.getEntityManager(GAME_UID).close();
        
        AbstractServerChessGame retrievedGame = dao.getServerChessGame(GAME_UID);
        
        assertNotNull(retrievedGame);
        
        assertEquals(scgGame.getCurrentStatus(), retrievedGame.getCurrentStatus());
        assertEquals(scgGame.getChessGame(), retrievedGame.getChessGame());
        assertTrue(ComparePlayers.isSamePlayer(scgGame.getPlayer(),retrievedGame.getPlayer()));
        assertEquals(scgGame.getNoOfObservers(), retrievedGame.getNoOfObservers());
        assertEquals(scgGame.getOpponent(), retrievedGame.getOpponent());
    }
}
