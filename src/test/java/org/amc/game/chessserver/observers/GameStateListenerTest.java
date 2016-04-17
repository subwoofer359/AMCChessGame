package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.GameStateListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    private ChessGameFactory chessGameFactory;
    
    @Mock
    ServerChessGameDAO serverChessGameDAO;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        template = mock(SimpMessagingTemplate.class);
        
        chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        
        serverGame = new TwoViewServerChessGame(GAME_UID, whitePlayer);
        serverGame.setChessGameFactory(chessGameFactory);
        serverGame.addOpponent(blackPlayer);
        
        
        destinationArgument = ArgumentCaptor.forClass(String.class);
        messageArgument = ArgumentCaptor.forClass(Object.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws DAOException {
        listener = new GameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);

        listener.update(serverGame, ChessGame.GameState.RUNNING);
        verify(serverChessGameDAO, never()).saveServerChessGame(eq(serverGame));
        verify(template).convertAndSend(destinationArgument.capture(),messageArgument.capture(),anyMap());
        assertEquals(String.format(GameStateListener.MESSAGE_DESTINATION + "/%d",serverGame.getUid()), 
                        destinationArgument.getValue());
        
        assertEquals(ChessGame.GameState.RUNNING.toString(), messageArgument.getValue());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSendCheckMateMessage() throws DAOException {
        listener = new GameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);
        listener.setServerChessGameDAO(serverChessGameDAO);
        
        listener.update(serverGame, ServerChessGame.ServerGameStatus.FINISHED);
        verify(template, times(1)).convertAndSend(anyString(),anyObject(),anyMap());
        verify(serverChessGameDAO, times(1)).saveServerChessGame(eq(serverGame));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testMessageIsIgnored() throws DAOException {
        listener = new GameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);
        listener.update(serverGame, null);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
        
        listener.update(null, ChessGame.GameState.RUNNING);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
        verify(serverChessGameDAO, never()).saveServerChessGame(eq(serverGame));
    }

}
