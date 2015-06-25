package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameStateListenerTest {

    private GameStateListener listener;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ServerChessGame serverGame;
    private SimpMessagingTemplate template;
    private static final long GAME_UID = 1234l;
    private ArgumentCaptor<String> destinationArgument;
    private ArgumentCaptor<Object> messageArgument;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        template = mock(SimpMessagingTemplate.class);
        serverGame = new ServerChessGame(GAME_UID, whitePlayer);
        serverGame.addOpponent(blackPlayer);
        
        destinationArgument = ArgumentCaptor.forClass(String.class);
        messageArgument = ArgumentCaptor.forClass(Object.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        listener = new GameStateListener(serverGame, template);
        listener.update(serverGame, ChessGame.GameState.RUNNING);
        verify(template).convertAndSend(destinationArgument.capture(),messageArgument.capture(),anyMap());
        assertEquals(String.format(GameStateListener.MESSAGE_DESTINATION + "/%d",serverGame.getUid()), 
                        destinationArgument.getValue());
        
        assertEquals(ChessGame.GameState.RUNNING.toString(), messageArgument.getValue());
    }
    
    @Test
    public void testMessageIsIgnored() {
        listener = new GameStateListener(serverGame, template);
        listener.update(serverGame, null);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
        
        listener.update(null, ChessGame.GameState.RUNNING);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
    }

}
