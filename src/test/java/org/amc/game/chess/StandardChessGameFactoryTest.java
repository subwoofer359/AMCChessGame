package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StandardChessGameFactoryTest {
    private ChessGameFactory factory;
    private ChessGameFixture chessGameFixture;

    @Before
    public void setUp() throws Exception {
        factory = new StandardChessGameFactory();
        chessGameFixture = new ChessGameFixture();
    }

    @Test
    public void testGetChessGameBoardPlayerPlayer() {
        ChessGame game = factory.getChessGame(chessGameFixture.getBoard(),
                        chessGameFixture.getWhitePlayer(), chessGameFixture.getBlackPlayer());
        ChessBoardUtil.compareBoards(chessGameFixture.getBoard(), game.getChessBoard());
        assertEquals(chessGameFixture.getWhitePlayer(), game.getWhitePlayer());
        assertEquals(chessGameFixture.getBlackPlayer(), game.getBlackPlayer());
        assertEquals(3, game.getChessMoveRules().size());
    }

}
