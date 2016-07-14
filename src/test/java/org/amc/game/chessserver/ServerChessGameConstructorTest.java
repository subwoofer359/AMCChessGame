package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.Before;
import org.junit.Test;

public class ServerChessGameConstructorTest {
    private ChessGameFactory chessGameFactory;
    private ChessGameFixture fixture;
    private ChessGame mockChessGame;
    
    private static final long UID = 120l;
    private static final long INVALID_UID = 233l;

    @Before
    public void setUp() throws Exception {
        chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        
        fixture = new ChessGameFixture();
        fixture.initialise();
        mockChessGame = mock(ChessGame.class);
        
    }
        
    private ServerChessGame createServerChessGame(long UID, Player player) {
        ServerChessGame game = new ServerChessGame(UID, player) {

            private static final long serialVersionUID = 1L;

            @Override
            public void addOpponent(Player opponent) {
                // Do nothing
                
            }
        };
        game.setChessGameFactory(chessGameFactory);
        return game;
    }
    
    private ServerChessGame createServerChessGame(long UID, ChessGame chessGame) {
        ServerChessGame game = new ServerChessGame(UID, chessGame) {

            private static final long serialVersionUID = 1L;

            @Override
            public void addOpponent(Player opponent) {
                // Do nothing
                
            }
        };
        game.setChessGameFactory(chessGameFactory);
        return game;
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerPassedToConstructor() {
    	Player p = null;
    	createServerChessGame(INVALID_UID, p);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullChessGamePassedToConstructor() {
    	ChessGame c = null;
    	createServerChessGame(INVALID_UID, c);
    }
    
    @Test
    public void testMove() throws IllegalMoveException {
        
        ServerChessGame scgGame = createServerChessGame(UID, fixture.getChessGame());
        scgGame.setChessGame(mockChessGame);
        when(mockChessGame.getGameState()).thenReturn(GameState.RUNNING);
        
        Move move = new Move("A2-A3");
        scgGame.move(fixture.getCurrentPlayer(), move);
        verify(mockChessGame, times(1)).move(eq(fixture.getWhitePlayer()), eq(move));
        verify(mockChessGame, times(1)).changePlayer();
        verify(mockChessGame, times(2)).getGameState();
    }
    
    @Test
    public void checkStatusInCheckTest() throws IllegalMoveException {
        
        ServerChessGame scgGame = createServerChessGame(UID, fixture.getChessGame());
        scgGame.setChessGame(mockChessGame);
        when(mockChessGame.getGameState()).thenReturn(GameState.BLACK_IN_CHECK);
        
        Move move = new Move("A2-A3");
        scgGame.move(fixture.getCurrentPlayer(), move);
        verify(mockChessGame, times(1)).move(eq(fixture.getWhitePlayer()), eq(move));
        verify(mockChessGame, times(1)).changePlayer();
        verify(mockChessGame, times(3)).getGameState();
    }
    
    @Test
    public void checkStatusStalemateTest() throws IllegalMoveException {
        
        ServerChessGame scgGame = createServerChessGame(UID, fixture.getChessGame());
        scgGame.setChessGame(mockChessGame);
        when(mockChessGame.getGameState()).thenReturn(GameState.STALEMATE);
        
        Move move = new Move("A2-A3");
        scgGame.move(fixture.getCurrentPlayer(), move);
        verify(mockChessGame, times(1)).move(eq(fixture.getWhitePlayer()), eq(move));
        verify(mockChessGame, times(1)).changePlayer();
        verify(mockChessGame, times(2)).getGameState();
        assertEquals(ServerGameStatus.FINISHED, scgGame.getCurrentStatus());
        
    }
    
    @Test
    public void testMoveOnNullChessBoard() throws IllegalMoveException {
        ServerChessGame scgGame = createServerChessGame(UID, fixture.getChessGame());
        scgGame.setChessGame(null);
        
        Move move = new Move("A2-A3");
        scgGame.move(fixture.getCurrentPlayer(), move);
        
        verify(mockChessGame, times(0)).move(eq(fixture.getWhitePlayer()), eq(move));
        verify(mockChessGame, times(0)).changePlayer();
        verify(mockChessGame, times(0)).getGameState();
    }
}
