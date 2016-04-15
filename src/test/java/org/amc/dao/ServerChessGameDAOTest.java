package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/SpringTestConfig.xml", "/GameServerSecurity.xml", "/GameServerWebSockets.xml", "/GameObservers.xml"})
public class ServerChessGameDAOTest {

    @Autowired
    private WebApplicationContext wac;
    
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private ServerChessGameTestDatabaseEntity serverChessGamesfixture;
    private ServerChessGameDAO dao;
    private EntityManagerCache emCache;
    
    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        dao = (ServerChessGameDAO) wac.getBean("myServerChessGameDAO");
        ObserverFactoryChain chain = (ObserverFactoryChain) wac.getBean("defaultObserverFactoryChain");
        emCache = (EntityManagerCache) wac.getBean("myEntityManagerCache");
        dao.setObserverFactoryChain(chain);
        serverChessGamesfixture = new ServerChessGameTestDatabaseEntity();      
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void getGameUidsTest() {
        Set<Long> uids = dao.getGameUids();
        assertEquals(1, uids.size());
        assertTrue(uids.contains(serverChessGamesfixture.getUID()));
    }

    @Test
    public void getServerChessGameTest() throws DAOException {
        AbstractServerChessGame scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
        scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
    }
    
    @Test
    public void getServerChessGameEntityManagerClosed() throws DAOException {
        AbstractServerChessGame scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
        
        emCache.getEntityManager(scgGame.getUid()).close();
        scgGame = dao.getServerChessGame(serverChessGamesfixture.getUID());
        assertEquals(2, scgGame.getNoOfObservers());
    }
}
