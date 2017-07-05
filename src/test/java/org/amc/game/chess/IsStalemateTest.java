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
    private InStalemate inStalemate;
    
    @Before
    public void setUp() throws Exception {
    	ChessGameFixture fixture = new ChessGameFixture();
    	
    	inStalemate = InStalemate.getInstance();
        whitePlayer = fixture.getWhitePlayer();
        blackPlayer = fixture.getBlackPlayer();
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void isStalemate() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Kf8:bf7:kf6");
        assertTrue(inStalemate.isStalemate(blackPlayer, whitePlayer, board));
    }

    @Test
    public void isStalemate2() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka8:Bb8:kb6:rh8");
        assertTrue(inStalemate.isStalemate(blackPlayer, whitePlayer, board));
    }

    @Test
    public void isStalemate3() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka1:rb2:kc3");
        assertTrue(inStalemate.isStalemate(blackPlayer, whitePlayer, board));
    }

    @Test
    public void isStalemate4() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Ka1:Pa2:qb3:kg5");
        assertTrue(inStalemate.isStalemate(blackPlayer, whitePlayer, board));
    }

    @Test
    public void isStalemate5() throws IllegalMoveException, ParseException {
        board = chessBoardFactory.getChessBoard("Kf5:Pf6:Pg7:kh5:ph4");
        assertTrue(inStalemate.isStalemate(whitePlayer, blackPlayer, board));
    }

    @Test
    public void testInCheckNotStalemate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kf5:kh5:rf1");
        assertFalse(inStalemate.isStalemate(blackPlayer, whitePlayer, board));
    }
}
