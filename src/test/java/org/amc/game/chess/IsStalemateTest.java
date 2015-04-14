package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class IsStalemateTest {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessBoardFactory chessBoardFactory;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
        board = new ChessBoard();
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void isStalemate() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Kf8:bf7:kf6");
        PlayerInStalement stalementCheck = new PlayerInStalement(blackPlayer, whitePlayer, board);
        assertTrue(stalementCheck.isStalemate());
    }

    @Test
    public void isStalemate2() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka8:Bb8:kb6:rh8");
        PlayerInStalement stalementCheck = new PlayerInStalement(blackPlayer, whitePlayer, board);
        assertTrue(stalementCheck.isStalemate());
    }

    @Test
    public void isStalemate3() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka1:rb2:kc3");
        PlayerInStalement stalementCheck = new PlayerInStalement(blackPlayer, whitePlayer, board);
        assertTrue(stalementCheck.isStalemate());
    }

    @Test
    public void isStalemate4() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka1:Pa2:qb3:kg5");
        PlayerInStalement stalementCheck = new PlayerInStalement(blackPlayer, whitePlayer, board);
        assertTrue(stalementCheck.isStalemate());
    }

    @Test
    public void isStalemate5() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Kf5:Pf6:Pg7:kh5:ph4");
        PlayerInStalement stalementCheck = new PlayerInStalement(whitePlayer, blackPlayer, board);
        assertTrue(stalementCheck.isStalemate());
    }

    @Test
    public void testInCheckNotStalemate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kf5:kh5:rf1");
        PlayerInStalement stalementCheck = new PlayerInStalement(blackPlayer, whitePlayer, board);
        assertFalse(stalementCheck.isStalemate());
    }
}
