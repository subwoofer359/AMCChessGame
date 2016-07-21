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
    
    GameStateListener listener;
    private ServerChessGame serverGame;
    
    private static final long GAME_UID = 1234l;
    
    @Mock
    private SCGDAOInterface serverChessGameDAO;
    
    @Mock
    SimpMessagingTemplate template;

     @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        
        serverGame = new OneViewServerChessGame(GAME_UID, whitePlayer);
        serverGame.setChessGameFactory(chessGameFactory);
        serverGame.addOpponent(blackPlayer);
    }
    
    private void setUpListener() {
        listener = new OneViewGameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);
        listener.setServerChessGameDAO(serverChessGameDAO);
    }

    @Test
    public void promotionWhiteTest() throws DAOException, ParseException {
        ChessBoard board = mock(ChessBoard.class);
        ChessPieceLocation cpl = new ChessPieceLocation(PawnPiece.getPawnPiece(Colour.WHITE), new Location("A8"));
        
        when(board.getPawnToBePromoted(Colour.WHITE)).thenReturn(cpl);
        serverGame.getChessGame().setChessBoard(board);
        
        listener.update(serverGame, PAWN_PROMOTION);
        def destination = GameStateListener.MESSAGE_DESTINATION + "/" +serverGame.uid;
        verify(template, times(1)).convertAndSend(eq(destination), eq("PAWN_PROMOTION (A,8)"), anyMap());
    }
    
    @Test
    public void promotionBlackTest() throws DAOException, ParseException {
        ChessBoard board = mock(ChessBoard.class);
        ChessPieceLocation cpl = new ChessPieceLocation(PawnPiece.getPawnPiece(Colour.BLACK), new Location("A1"));
        
        when(board.getPawnToBePromoted(Colour.BLACK)).thenReturn(cpl);
        serverGame.getChessGame().setChessBoard(board);
        serverGame.getChessGame().changePlayer();
        
        listener.update(serverGame, PAWN_PROMOTION);
        def destination = GameStateListener.MESSAGE_DESTINATION + "/" + serverGame.uid;
        verify(template, times(1)).convertAndSend(eq(destination), eq("PAWN_PROMOTION (A,1)"), anyMap());
    }

}
