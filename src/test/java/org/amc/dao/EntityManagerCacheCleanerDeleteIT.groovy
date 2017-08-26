package org.amc.dao

import org.amc.dao.EntityManagerCacheCleaner.EmCacheCleanerThread;
import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.DatabaseFixture;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import groovy.transform.TypeChecked;

@TypeChecked
class EntityManagerCacheCleanerDeleteIT {

	private static final DatabaseFixture fixture = new DatabaseFixture();
	
    private EntityManagerCacheCleaner cleaner;
    private EntityManagerCache enCache;
    private ServerChessGameDAO serverChessGameDAO;
    private Long gameUid;
    
    @Mock
    private ObserverFactoryChain chain;
    
    private def threads = [];

    @BeforeClass
    static void setUpBeforeClass() {
        fixture.setUp();
    }

    @AfterClass
    static void setUpAfterClass() {
        fixture.tearDown();
    }

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
		
        ServerChessGameDatabaseEntityFixture scgdef =
                new ServerChessGameDatabaseEntityFixture(fixture.getNewEntityManager());

        gameUid = scgdef.UID;
                
        enCache = new EntityManagerCache();
        enCache.entityManagerFactory = fixture.entityManagerFactory;
        
        cleaner = new EntityManagerCacheCleaner(enCache, 1,1);
        
        serverChessGameDAO = new ServerChessGameDAO();
        
        serverChessGameDAO.entityManagerCache = enCache;
        serverChessGameDAO.entityManager = fixture.getNewEntityManager();
        serverChessGameDAO.observerFactoryChain = chain;
    }

    @Test
    void testChessGameDeleteBeforeCacheCleaned() {
        AbstractServerChessGame serverChessGame = serverChessGameDAO.getServerChessGame(gameUid);
        serverChessGameDAO.deleteEntity(serverChessGame);
        EmCacheCleanerThread cleanerThread = new EmCacheCleanerThread(enCache, 0);
        cleanerThread.run();
    }
}
