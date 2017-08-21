package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StandardChessGameFactoryTest {
    private ChessGameFactory factory;
    private AbstractChessGame chessGame;

    @Before
    public void setUp() throws Exception {
        factory = new StandardChessGameFactory();
        chessGame= new ChessGameFixture().getChessGame();
    }

    @Test
    public void testGetChessGameBoardPlayerPlayer() {
        AbstractChessGame game = factory.getChessGame(chessGame.getChessBoard(),
                        chessGame.getWhitePlayer(), chessGame.getBlackPlayer());
        ChessBoardUtil.compareBoards(chessGame.getChessBoard(), game.getChessBoard());
        assertEquals(chessGame.getWhitePlayer(), game.getWhitePlayer());
        assertEquals(chessGame.getBlackPlayer(), game.getBlackPlayer());
        assertEquals(3, game.getChessMoveRules().size());
    }

}
