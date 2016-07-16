package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.junit.Before;
import org.junit.Test;

public class ChessGameGetOpposingPlayerTest {

	private static final long GAME_UID = 2030L;
	
    private ChessGameFixture chessGameFixture;
    private ServerChessGame scg;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();

        scg = new TwoViewServerChessGame(GAME_UID, chessGameFixture.getWhitePlayer());
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
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
