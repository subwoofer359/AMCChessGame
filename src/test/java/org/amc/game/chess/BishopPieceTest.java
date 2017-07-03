package org.amc.game.chess;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;

public class BishopPieceTest extends ChessPieceTest {
    private ChessBoard board;
    private Location testStartPosition = new Location("D5");

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
        setChessBoard(board);
    }

    @Override
    @Test
    public void testOnEmptyBoardIsValidMove() {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        board.put(bishop, this.testStartPosition);

        ValidMovements.getListOfDiagonalLocationsFromD5().forEach(endPosition -> {
            assertTrue(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));
        });

    }

    @Override
    @Test
    public void testOnEmptyBoardIsNotValidMove() throws Exception {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);

        board.put(bishop, new Location("F8"));
        String[] invalidMoves = {
        		"D4-G7",
        		"D4-D6",
        		"D4-A3",
        		"D4-A8", 
        		"D4-D4" 
        };
        assertNotValidMove(bishop, invalidMoves);
    }
    
    private void assertNotValidMove(ChessPiece piece, String[] moves) {
    	Arrays.asList(moves).forEach(move -> {
    		assertFalse("Shouldn't be a valid move: " + move, piece.isValidMove(board, new Move(move)));
    	});
    }

    @Override
    @Test
    public void testOnBoardIsValidCapture() throws ParseException {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        board.put(bishop, this.testStartPosition);

        String[] bishopLocations = {
        		"A8",
        		"G8",
        		"A2",
        		"H1"
        };
        
        putBishopOnBoard(Colour.WHITE, bishopLocations);

        ValidMovements.getListOfDiagonalLocationsFromD5().forEach( endPosition -> {
            assertTrue(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));
        });
    }
    
    private void putBishopOnBoard(Colour colour, String[] locations) {
    	Arrays.asList(locations).forEach(location -> {
    		placeOnBoard(BishopPiece.getPiece(colour), location);
    	});
    }

    @Override
    @Test
    public void testOnBoardInvalidCapture() {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        board.put(bishop, this.testStartPosition);

        ValidMovements.getListOfDiagonalLocationsFromD5().forEach(endPosition -> {
            board.put(BishopPiece.getPiece(Colour.BLACK), endPosition);
            assertFalse(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));
        });
    }


    @Override
    @Test
    public void testOnBoardIsNotValidMove() throws ParseException {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        BishopPiece bishopWhite = BishopPiece.getPiece(Colour.WHITE);
        board.put(bishop, new Location("F8"));
        board.put(bishopWhite, new Location("D6"));

        boolean isValid = bishop.isValidMove(this.board, new Move("F8-C5"));
        assertFalse(isValid);
    }

    @Test
    @Override
    public void testCanSlide() {
        BishopPiece bishop = BishopPiece.getPiece(Colour.BLACK);
        assertTrue(bishop.canSlide());
    }
}
