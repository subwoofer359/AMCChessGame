package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;
import static org.amc.game.chess.SimpleChessBoardSetupNotation.MOVE_TOKEN;

import org.junit.After;
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void validInputTest() {
        SimpleChessBoardSetupNotation notation = new SimpleChessBoardSetupNotation();
        assertTrue(notation.isInputValid(setupNotation));
    }

    @Test
    public void test() throws Exception {
        ChessBoard board = chessBoardFactory.getChessBoard(setupNotation);
        assertTrue(board.getPieceFromBoardAt(new Location(D, 6)) instanceof KingPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(E, 6)) instanceof QueenPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(B, 8)) instanceof BishopPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(E, 8)) instanceof BishopPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(G, 8)) instanceof KnightPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(A, 6)) instanceof KnightPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(A, 4)) instanceof RookPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(H, 5)) instanceof PawnPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(A, 3)) instanceof KingPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(E, 1)) instanceof QueenPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(H, 3)) instanceof BishopPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(C, 3)) instanceof KnightPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(B, 5)) instanceof RookPiece);
        assertTrue(board.getPieceFromBoardAt(new Location(F, 4)) instanceof PawnPiece);
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
        assertTrue("Pawn should have field moved set to true", board.getPieceFromBoardAt(new Location("a4")).hasMoved());
        assertFalse("Pawn should have field moved set to false", board.getPieceFromBoardAt(new Location("f4")).hasMoved());
    }

}
