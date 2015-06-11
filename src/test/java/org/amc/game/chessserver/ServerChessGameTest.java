package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerChessGameTest {
    private Player player;
    private Player opponent;
    
    private static final long UID = 0l;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Ted");
        opponent = new HumanPlayer("Chris");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitialState() {
        ServerChessGame game = new ServerChessGame(UID, player);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, game.getCurrentStatus());
        assertEquals(Colour.WHITE, game.getPlayer().getColour());
    }

    @Test
    public void testAddOpponent() {
        ServerChessGame game = new ServerChessGame(UID, player);
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNotNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, game.getCurrentStatus());
        assertEquals(Colour.BLACK, game.getOpponent().getColour());
    }

    @Test
    public void testAddPlayerAsOpponent() {
        ServerChessGame game = new ServerChessGame(UID, player);
        game.addOpponent(player);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, game.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToFinishedGame() {
        ServerChessGame game = new ServerChessGame(UID, player);
        game.setCurrentStatus(ServerChessGame.ServerGameStatus.FINISHED);
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, game.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToInProgressGame() {
        ServerChessGame game = new ServerChessGame(UID, player);
        game.setCurrentStatus(ServerChessGame.ServerGameStatus.IN_PROGRESS);
        game.addOpponent(opponent);
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), player));
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, game.getCurrentStatus());
    }
    
    @Test
    public void testDestroy() {
        ServerChessGame game = new ServerChessGame(UID, player);
        game.addOpponent(opponent);
        game.destory();
        
        assertNull(game.getOpponent());
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, game.getCurrentStatus());
    }

}
