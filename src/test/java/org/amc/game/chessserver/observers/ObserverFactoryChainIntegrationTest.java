package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.game.GameObserver;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/GameServerWebSockets.xml", "/SpringTestConfig.xml", "/GameObservers.xml", "/GameServerSecurity.xml"})
public class ObserverFactoryChainIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    
    private ObserverFactoryChain chain;
    
    private String observerStr;
    
    private ServerChessGame serverChessGame;
    
    @Before
    public void setUp() throws Exception {
        observerStr = JsonChessGameView.class.getSimpleName() + GameFinishedListener.class.getSimpleName() 
                        + GameStateListener.class.getSimpleName();
        chain = (ObserverFactoryChain) wac.getBean("defaultObserverFactoryChain");
        
        serverChessGame = mock(ServerChessGame.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        chain.addObserver(observerStr, serverChessGame);
        verify(serverChessGame, times(3)).attachObserver(any(GameObserver.class));
    }
}
