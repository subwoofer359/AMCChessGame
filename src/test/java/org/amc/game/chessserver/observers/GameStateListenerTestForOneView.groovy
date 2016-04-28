package org.amc.game.chessserver.observers;

import static org.amc.game.chess.AbstractChessGame.GameState.PAWN_PROMOTION;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

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
import org.amc.game.chessserver.OneViewServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.ParseException;

class GameStateListenerTestForOneView {
    
    private GameStateListener listener;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ServerChessGame serverGame;
    
    private static final long GAME_UID = 1234l;
    private ArgumentCaptor<String> destinationArgument;
    private ArgumentCaptor<Object> messageArgument;
    private ChessGameFactory chessGameFactory;
    
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
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        chessGameFactory = new ChessGameFactory() {
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
        listener = new GameStateListener();
        listener.setGameToObserver(serverGame);
        listener.setSimpMessagingTemplate(template);
        listener.setServerChessGameDAO(serverChessGameDAO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void promotionWhiteTest() throws DAOException, ParseException {
        ChessBoard board = mock(ChessBoard.class);
        ChessPieceLocation cpl = new ChessPieceLocation(new PawnPiece(Colour.WHITE), new Location("A8"));
        
        when(board.getPawnToBePromoted(Colour.WHITE)).thenReturn(cpl);
        serverGame.getChessGame().setChessBoard(board);
        
        listener.update(serverGame, PAWN_PROMOTION);
        Player player = serverGame.getChessGame().getCurrentPlayer();
        verify(template, times(1)).convertAndSendToUser(eq(player.getUserName()), 
                eq(GameStateListener.MESSAGE_USER_DESTINATION), eq("PAWN_PROMOTION (A,8)"), anyMap());
    }
    
    @Test
    public void promotionBlackTest() throws DAOException, ParseException {
        ChessBoard board = mock(ChessBoard.class);
        ChessPieceLocation cpl = new ChessPieceLocation(new PawnPiece(Colour.BLACK), new Location("A1"));
        
        when(board.getPawnToBePromoted(Colour.BLACK)).thenReturn(cpl);
        serverGame.getChessGame().setChessBoard(board);
        serverGame.getChessGame().changePlayer();
        
        listener.update(serverGame, PAWN_PROMOTION);
        Player player = serverGame.getChessGame().getCurrentPlayer();
        verify(template, times(1)).convertAndSendToUser(eq(player.getUserName()),
                eq(GameStateListener.MESSAGE_USER_DESTINATION), eq("PAWN_PROMOTION (A,1)"), anyMap());
    }

}
