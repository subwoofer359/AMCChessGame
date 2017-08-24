package org.amc.game.chessserver.observers;

import static org.amc.game.chess.AbstractChessGame.GameState.PAWN_PROMOTION;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chessserver.OneViewServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.ParseException;

class GameStateListenerTestForOneView {
	
	private static final long GAME_UID = 1234l;
	
	private static final String MSG_A1 = 'PAWN_PROMOTION (A,1)';
	private static final String MSG_A8 = 'PAWN_PROMOTION (A,8)';
	
	private final String destination = "${GameStateListener.MESSAGE_DESTINATION}/${GAME_UID}";
	
	private static final ChessGamePlayer whitePlayer = new RealChessGamePlayer(
		new HumanPlayer('White Player'), Colour.WHITE);
	
	private static final ChessGamePlayer blackPlayer = new RealChessGamePlayer(
		new HumanPlayer('Black Player'), Colour.BLACK);
	
	private static final ChessGameFactory chessGameFactory = new ChessGameFactory() {
		@Override
		public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
						ChessGamePlayer playerBlack) {
			return new ChessGame(board, playerWhite, playerBlack);
		}
	};
    
    private GameStateListener listener;
	
    private ServerChessGame serverGame;

    @Mock
    private SCGDAOInterface serverChessGameDAO;
    
    @Mock
    private SimpMessagingTemplate template;
	
	@Mock
	private ChessBoard board;
	
    @Before
	void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpServerChessGame();        
        setUpListener();
    }
    
    private void setUpServerChessGame() {
        serverGame = new OneViewServerChessGame(GAME_UID, whitePlayer);
        serverGame.chessGameFactory = chessGameFactory;
        serverGame.addOpponent(blackPlayer);
    }
    
    private void setUpListener() {
        listener = new OneViewGameStateListener();
        listener.setGameToObserver(serverGame);
        listener.simpMessagingTemplate = template;
        listener.serverChessGameDAO = serverChessGameDAO;
    }

    @Test
    void promotionWhiteTest() {
		ChessPieceLocation cpl = new ChessPieceLocation(PawnPiece.getPiece(Colour.WHITE), new Location('A8'));
        when(board.getPawnToBePromoted(Colour.WHITE)).thenReturn(cpl);
        serverGame.chessGame.chessBoard = board;
        
        listener.update(serverGame, PAWN_PROMOTION);
        
        verify(template, times(1)).convertAndSend(eq(destination), eq(MSG_A8), anyMap());
    }
    
    @Test
    void promotionBlackTest() {
		ChessPieceLocation cpl = new ChessPieceLocation(PawnPiece.getPiece(Colour.BLACK), new Location('A1')); 
        when(board.getPawnToBePromoted(Colour.BLACK)).thenReturn(cpl);
        serverGame.chessGame.chessBoard = board;
        serverGame.chessGame.changePlayer();
        
        listener.update(serverGame, PAWN_PROMOTION);
        
        verify(template, times(1)).convertAndSend(eq(destination), eq(MSG_A1), anyMap());
    }

}
