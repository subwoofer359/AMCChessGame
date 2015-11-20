package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TwoViewServerChessGameTest {
    
    private Player player;
    private Player opponent;
    private ChessGameFactory chessGameFactory;
    
    private static final long UID = 120l;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Ted");
        opponent = new HumanPlayer("Chris");
        chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
    }
    

    @After
    public void tearDown() throws Exception {
        
    }

    @Test
    public void getPlayerTest() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(opponent, game.getPlayer(opponent)));
        assertTrue(ComparePlayers.comparePlayers(player, game.getPlayer(player)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getPlayerNullPlayer() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(opponent);
        game.getPlayer(null);
    }
    
    @Test
    public void getPlayerUnknownPlayer() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(opponent);
        Player unknownPlayer = new HumanPlayer("Evil Ralph");
        game.getPlayer(unknownPlayer);
    }
    
    private ServerChessGame getServerChessGame() {
        ServerChessGame game = new TwoViewServerChessGame(UID, player);
        game.setChessGameFactory(chessGameFactory);
        return game;
    }
    
    @Test
    public void testAddOpponent() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNotNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, game.getCurrentStatus());
        assertEquals(Colour.BLACK, game.getOpponent().getColour());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullAddOpponent() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(null);
    }

    @Test
    public void testAddPlayerAsOpponent() {
        ServerChessGame game = getServerChessGame();
        game.addOpponent(player);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, game.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToFinishedGame() {
        ServerChessGame game = getServerChessGame();
        game.setCurrentStatus(ServerChessGame.ServerGameStatus.FINISHED);
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, game.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToInProgressGame() {
        ServerChessGame game = getServerChessGame();
        game.setCurrentStatus(ServerChessGame.ServerGameStatus.IN_PROGRESS);
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, game.getCurrentStatus());
    }
    
    @Test
    public void getPlayerOpponentNotAddedTest() {
        ServerChessGame game = getServerChessGame();
        assertEquals(null,game.getPlayer(opponent));
    }
    
    @Test
    public void ConstructorTest() {
        ServerChessGame scgGame = new TwoViewServerChessGame();
        assertEquals(0L, scgGame.getUid());
        assertNull(scgGame.getChessGame());
        assertNull(scgGame.getPlayer());
        assertNull(scgGame.getOpponent());
        assertEquals(ServerGameStatus.NEW, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
    }
    
    @Test
    public void ConstructorChessGameTest() {
        ChessGameFixture fixture = new ChessGameFixture();
        ServerChessGame scgGame = new TwoViewServerChessGame(UID, fixture.getChessGame());
        
        assertEquals(UID, scgGame.getUid());
        assertNotNull(scgGame.getChessGame());
        assertFalse(scgGame.getChessGame() == fixture.getChessGame());
        assertTrue(ComparePlayers.comparePlayers(fixture.getWhitePlayer(), scgGame.getPlayer()));
        assertTrue(ComparePlayers.comparePlayers(fixture.getBlackPlayer(), scgGame.getOpponent()));
        assertEquals(ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
        
        ChessBoardUtilities.compareBoards(fixture.getChessGame().getChessBoard(), 
                        scgGame.getChessGame().getChessBoard());
    }

}
