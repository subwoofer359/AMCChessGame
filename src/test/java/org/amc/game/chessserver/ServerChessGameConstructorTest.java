package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.ChessGame.GameState;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerChessGameConstructorTest {
    private Player player;
    private ChessGameFactory chessGameFactory;
    private ChessGameFixture fixture;
    private ChessGame mockChessGame;
    
    private static final long UID = 120l;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Ted");
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitialState() {
        ServerChessGame game = getServerChessGame(UID, player);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, game.getCurrentStatus());
        assertEquals(Colour.WHITE, game.getPlayer().getColour());
    }
        
    private ServerChessGame getServerChessGame(long UID, Player player) {
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
    
    private ServerChessGame getServerChessGame(long UID, ChessGame chessGame) {
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
    	getServerChessGame(233L, p);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullChessGamePassedToConstructor() {
    	ChessGame c = null;
    	getServerChessGame(233L, c);
    }
    
    @Test
    public void Constructor() {
        ServerChessGame scgGame = new ServerChessGame() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void addOpponent(Player opponent) {
                // do nothing
            }
        };
        
        assertEquals(0L, scgGame.getUid());
        assertNull(scgGame.getChessGame());
        assertNull(scgGame.getPlayer());
        assertNull(scgGame.getOpponent());
        assertEquals(ServerGameStatus.NEW, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
    }
    
    @Test
    public void ConstructorChessGame() {
        ChessGameFixture fixture = new ChessGameFixture();
        ServerChessGame scgGame = getServerChessGame(UID, fixture.getChessGame());
        
        assertEquals(UID, scgGame.getUid());
        assertNotNull(scgGame.getChessGame());
        assertFalse(scgGame.getChessGame() == fixture.getChessGame());
        assertTrue(ComparePlayers.comparePlayers(fixture.getWhitePlayer(), scgGame.getPlayer()));
        assertTrue(ComparePlayers.comparePlayers(fixture.getBlackPlayer(), scgGame.getOpponent()));
        assertEquals(ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNotNull(scgGame.getChessGameFactory());
        
        ChessBoardUtilities.compareBoards(fixture.getChessGame().getChessBoard(), 
                        scgGame.getChessGame().getChessBoard());
    }
    
    @Test
    public void testMove() throws IllegalMoveException {
        
        ServerChessGame scgGame = getServerChessGame(UID, fixture.getChessGame());
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
        
        ServerChessGame scgGame = getServerChessGame(UID, fixture.getChessGame());
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
        
        ServerChessGame scgGame = getServerChessGame(UID, fixture.getChessGame());
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
        ServerChessGame scgGame = getServerChessGame(UID, fixture.getChessGame());
        scgGame.setChessGame(null);
        
        Move move = new Move("A2-A3");
        scgGame.move(fixture.getCurrentPlayer(), move);
        
        verify(mockChessGame, times(0)).move(eq(fixture.getWhitePlayer()), eq(move));
        verify(mockChessGame, times(0)).changePlayer();
        verify(mockChessGame, times(0)).getGameState();
    }

}
