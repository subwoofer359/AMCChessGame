package org.amc.game.chessserver.spring

import org.amc.dao.ServerChessGameDAO;
import org.amc.dao.ServerChessGameDatabaseEntityFixture;
import org.amc.game.chessserver.DatabaseFixture;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(['/EntityManagerFactory.groovy', "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" ])
class ChessGameCleanUpIT {
    @Autowired
    WebApplicationContext wac;
    
    DatabaseFixture fixture = new DatabaseFixture();
    ServerChessGameDatabaseEntityFixture scgFixture;
    ServerChessGameDAO serverChessGameDAO;
    

    @Before
    public void setup() throws Exception {
        fixture.setUp();
        scgFixture = new ServerChessGameDatabaseEntityFixture(5);
        serverChessGameDAO = (ServerChessGameDAO) wac.getBean("myServerChessGameDAO");
        serverChessGameDAO.getServerChessGame(1).setCurrentStatus(ServerGameStatus.FINISHED);
        List games = serverChessGameDAO.findEntities("currentStatus", ServerGameStatus.FINISHED);
        assert !games.isEmpty();
    }
    
    @After
    public void tearDown() throws Exception {
       fixture.tearDown();
    }
    
    @Test
    public void test() {
        List games = serverChessGameDAO.findEntities("currentStatus", ServerGameStatus.FINISHED);
        assert games.isEmpty();
    }
}
