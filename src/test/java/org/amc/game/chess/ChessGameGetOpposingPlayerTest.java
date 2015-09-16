package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;

public class ChessGameGetOpposingPlayerTest {

    private ChessGameFixture chessGameFixture;
    private ServerChessGame scg;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();

        scg = new ServerChessGame(2030L, chessGameFixture.getWhitePlayer());
        scg.addOpponent(chessGameFixture.getBlackPlayer());

    }

    @Test
    public void testChessGame() {
        assertEquals(chessGameFixture.getBlackPlayer(), chessGameFixture.getChessGame()
                        .getOpposingPlayer(chessGameFixture.getWhitePlayer()));
    }

    @Test
    public void testServerChessGame() {
        assertTrue(ComparePlayers.comparePlayers(chessGameFixture.getBlackPlayer(), scg
                        .getChessGame().getOpposingPlayer(chessGameFixture.getWhitePlayer())));
    }

}
