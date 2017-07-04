package org.amc.game.chessserver.observers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.GameObserver;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class JsonChessGameViewFactoryTest {
    
    private JsonChessGameViewFactory factory;
    private ServerChessGame chessGame;

    @Before
    public void setUp() throws Exception {
        chessGame = mock(ServerChessGame.class);
        final SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        factory = new JsonChessGameViewFactory() {
            
            @Override
            public GameObserver createObserver() {
                return new JsonChessGameView(template);
            }
        };
    }

    @Test
    public void test() {
        GameObserver ob = factory.createObserver();
        ob.setGameToObserver(chessGame);
        assertEquals(JsonChessGameView.class, ob.getClass());
        verify(chessGame,times(1)).attachObserver(eq(ob));
    }

}
