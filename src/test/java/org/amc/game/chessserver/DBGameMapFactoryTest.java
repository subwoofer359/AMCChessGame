package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.dao.ServerChessGameTestDatabaseEntity;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" })
public class DBGameMapFactoryTest {

    private DBGameMapFactoryBean gameMapFactory;
    private ServerChessGameTestDatabaseEntity scgtbe;
    private DatabaseSignUpFixture dbFixture = new DatabaseSignUpFixture();
    
    @Autowired
    private WebApplicationContext wac;
    
    @Before
    public void setUp() throws Exception {
        dbFixture.setUp();
        scgtbe = new ServerChessGameTestDatabaseEntity();
        gameMapFactory = (DBGameMapFactoryBean) wac.getBean("gameMapFactory");
    }

    @After
    public void tearDown() throws Exception {
        dbFixture.tearDown();
    }

    /**
     * Test that changes to ServerChessGame have saved
     */
    @Test
    public void test() {
         Map<Long, ServerChessGame> gameMap = gameMapFactory.getGameMap();
         ServerChessGame game = gameMap.get(scgtbe.getUID());
         assertEquals(ServerGameStatus.IN_PROGRESS, game.getCurrentStatus());
         
         game.setCurrentStatus(ServerGameStatus.FINISHED);
         gameMapFactory.destroyGameMap();
         ServerChessGame retrievedGame = gameMap.get(scgtbe.getUID());
         assertEquals(ServerGameStatus.FINISHED, retrievedGame.getCurrentStatus());
         
    }

}
