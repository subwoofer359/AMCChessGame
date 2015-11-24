package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetChessGameBoardPlayerPlayer() {
        ChessGame game = factory.getChessGame(chessGameFixture.getBoard(),
                        chessGameFixture.getWhitePlayer(), chessGameFixture.getBlackPlayer());
        ChessBoardUtilities.compareBoards(chessGameFixture.getBoard(), game.getChessBoard());
        assertEquals(chessGameFixture.getWhitePlayer(), game.getWhitePlayer());
        assertEquals(chessGameFixture.getBlackPlayer(), game.getBlackPlayer());
        assertEquals(3, game.getChessMoveRules().size());
    }

}
