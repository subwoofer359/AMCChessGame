package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.SimpleChessBoardSetupNotation.MOVE_TOKEN;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class SimpleChessBoardSetupNotationTest {

    private static final String setupNotation = "Kd6:Qe6:Bb8:Be8:Ng8:Na6:Ra4:Ph5:"
                    + "ka3:qe1:bh3:nc3:pf4:rb5";
    private ChessBoardFactory chessBoardFactory;

    // private static final String setupNotation="Kd6Qe6";
    @Before
    public void setUp() throws Exception {
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void validInputTest() {
        SimpleChessBoardSetupNotation notation = new SimpleChessBoardSetupNotation();
        assertTrue(notation.isInputValid(setupNotation));
    }

    @Test
    public void test() throws Exception {
        ChessBoard board = chessBoardFactory.getChessBoard(setupNotation);
        assertTrue(board.get(new Location("D6")) instanceof KingPiece);
        assertTrue(board.get(new Location("E6")) instanceof QueenPiece);
        assertTrue(board.get(new Location("B8")) instanceof BishopPiece);
        assertTrue(board.get(new Location("E8")) instanceof BishopPiece);
        assertTrue(board.get(new Location("G8")) instanceof KnightPiece);
        assertTrue(board.get(new Location("A6")) instanceof KnightPiece);
        assertTrue(board.get(new Location("A4")) instanceof RookPiece);
        assertTrue(board.get(new Location("H5")) instanceof PawnPiece);
        assertTrue(board.get(new Location("A3")) instanceof KingPiece);
        assertTrue(board.get(new Location("E1")) instanceof QueenPiece);
        assertTrue(board.get(new Location("H3")) instanceof BishopPiece);
        assertTrue(board.get(new Location("C3")) instanceof KnightPiece);
        assertTrue(board.get(new Location("B5")) instanceof RookPiece);
        assertTrue(board.get(new Location("F4")) instanceof PawnPiece);
    }

    @Test(expected = ParseException.class)
    public void testInvalidPattern() throws ParseException {
        chessBoardFactory.getChessBoard("Be3:Me3");
    }
    
    @Test
    public void testMovedNotationValid() {
    	final String boardStr = setupNotation + ":pa4" + MOVE_TOKEN;
    	try {
    		chessBoardFactory.getChessBoard(boardStr);
    	} catch (ParseException pe) {
    		fail("Should be valid setUp Notation:" + boardStr);
    	}
    	
    }
    
    @Test
    public void testMovedNotation() throws Exception {
    	final String boardStr = setupNotation + ":pa4" + MOVE_TOKEN;
        ChessBoard board = chessBoardFactory.getChessBoard(boardStr);
        assertTrue("Pawn should have field moved set to true", board.get(new Location("a4")).hasMoved());
        assertFalse("Pawn should have field moved set to false", board.get(new Location("f4")).hasMoved());
    }

}
