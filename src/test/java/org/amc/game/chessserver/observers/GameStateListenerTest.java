package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.amc.game.chess.AbstractChessGame.GameState.*;

import java.text.ParseException;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.GameStateListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameStateListenerTest {

    private GameStateListener listener;
    private ServerChessGame serverGame;
    
    private static final long GAME_UID = 1234l;
    private ArgumentCaptor<String> destinationArgument;
    private ArgumentCaptor<Object> messageArgument;
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;
    
    @Mock
    private SimpMessagingTemplate template;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        messageArgument = ArgumentCaptor.forClass(Object.class);
        setUpServerChessGame();
        
        setUpListener();
    }

    private void setUpServerChessGame() {
    	ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
    	ChessGameFactory chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        
        serverGame = new TwoViewServerChessGame(GAME_UID, whitePlayer);
        serverGame.setChessGameFactory(chessGameFactory);
        serverGame.addOpponent(blackPlayer);
    }
    
    private void setUpListener() {
    	listener = new GameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);
        listener.setServerChessGameDAO(serverChessGameDAO);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws DAOException {
        listener.update(serverGame, RUNNING);
        verify(serverChessGameDAO, never()).saveServerChessGame(eq(serverGame));
        verify(template).convertAndSend(destinationArgument.capture(),messageArgument.capture(),anyMap());
        assertEquals(String.format(GameStateListener.MESSAGE_DESTINATION + "/%d",serverGame.getUid()), 
                        destinationArgument.getValue());
        
        assertEquals(RUNNING.toString(), messageArgument.getValue());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSendCheckMateMessage() throws DAOException {
        listener.update(serverGame, ServerChessGame.ServerGameStatus.FINISHED);
        verify(template, times(1)).convertAndSend(anyString(),anyObject(),anyMap());
        verify(serverChessGameDAO, times(1)).saveServerChessGame(eq(serverGame));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testMessageIsIgnored() throws DAOException {
        listener.update(serverGame, null);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
        
        listener.update(null, RUNNING);
        verify(template, never()).convertAndSend(anyString(),anyObject(),anyMap());
        verify(serverChessGameDAO, never()).saveServerChessGame(eq(serverGame));
    }

    @Test
    public void promotionTest() throws DAOException, ParseException {
    	ChessBoard board = mock(ChessBoard.class);
    	ChessPieceLocation cpl = new ChessPieceLocation(PawnPiece.getPawnPiece(Colour.WHITE), new Location("A8"));
    	
    	when(board.getPawnToBePromoted(Colour.WHITE)).thenReturn(cpl);
    	serverGame.getChessGame().setChessBoard(board);
    	
    	listener.update(serverGame, PAWN_PROMOTION);
    	Player player = serverGame.getChessGame().getCurrentPlayer();
    	verify(template, times(1)).convertAndSendToUser(eq(player.getUserName()), 
    			eq(GameStateListener.MESSAGE_USER_DESTINATION), eq("PAWN_PROMOTION (A,8)"), anyMap());
    }
}
