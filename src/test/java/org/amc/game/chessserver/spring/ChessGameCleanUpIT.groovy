package org.amc.game.chessserver.spring

import org.amc.dao.DAO
import org.amc.dao.DAOInterface
import org.amc.dao.ManagedDAOFactory
import org.amc.dao.ServerChessGameDatabaseEntityFixture
import org.amc.game.chessserver.AbstractServerChessGame
import org.amc.game.chessserver.DatabaseFixture
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext

import javax.persistence.EntityManager


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(['/EntityManagerFactory.groovy', '/SpringTestConfig.xml', '/GameServerSecurity.xml',
    '/GameServerWebSockets.xml', '/EmailServiceContext.xml' ])
class ChessGameCleanUpIT {
    @Autowired
    WebApplicationContext wac;
    
    static DatabaseFixture fixture = new DatabaseFixture();

    static ServerChessGameDatabaseEntityFixture scgFixture;
    
    DAOInterface<AbstractServerChessGame> serverChessGameDAO;

    @BeforeClass
    static void setUpBeforeClass() {
        fixture.setUp();
        scgFixture = new ServerChessGameDatabaseEntityFixture(fixture.getNewEntityManager(), 5);
        EntityManager em = fixture.getNewEntityManager();
        DAO<AbstractServerChessGame> scgDAO = new DAO<>(AbstractServerChessGame);
        scgDAO.setEntityManager(em);
        List<AbstractServerChessGame> allGames = scgDAO.findEntities();
        allGames.each {
            it.removeAllObservers();
            it.currentStatus = ServerGameStatus.FINISHED;
        };
    }
    
    @AfterClass
    static void tearDownAfterClass() {
        fixture.tearDown();
    }
    
    @Before
    void setup() {
        ManagedDAOFactory factory = wac.getBean('managedDAOFactory');
        serverChessGameDAO = factory.getServerChessGameDAO(); 
    }
    
    @Test
    void test() {
        List games = serverChessGameDAO.findEntities('currentStatus', ServerGameStatus.FINISHED);
        assert games.isEmpty();
    }
}
