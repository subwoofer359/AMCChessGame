package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ComparePlayers;

import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.*;

public class TwoViewServerChessGameConstructorTest {

    private static final long UID = 120l;

    @Test
    public void constructorTest() {
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
    public void constructorChessGameTest() {
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

        ChessBoardUtilities.compareBoards(fixture.getChessGame().getChessBoard(), scgGame
                        .getChessGame().getChessBoard());
    }

}
