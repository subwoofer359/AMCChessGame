package org.amc.game.chess;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    @Test
    public void testGetAllPossibleMoveLocationsFromD4() {
    	List<String> locationStr = Arrays.asList("G7", "F2", "G1", "E3", "B2", "E5", "B6", "F6", "C3",
    			"A1", "C5", "H8", "A7");
    	testBishopPossibleMoveLocations(new Location("D4"), locationStr);
 
    }
    
    @Test
    public void testGetAllPossibleMoveLocationsFromG2() {
    	List<String> locationStr = Arrays.asList("F1", "H3", "H1", "F3", "E4","D5", "C6", "B7", "A8");
    	testBishopPossibleMoveLocations(new Location("G2"), locationStr);
 
    }
    
    @Test
    public void testGetAllPossibleMoveLocationsFromH8() {
    	List<String> locationStr = Arrays.asList("G7", "F6", "E5", "D4", "C3", "B2", "A1");
    	testBishopPossibleMoveLocations(new Location("H8"), locationStr);
 
    }
    
    @Test
    public void testGetAllPossibleMoveLocationsFromH8WithOtherPiece() {
    	board.put(BishopPiece.getPiece(Colour.WHITE), new Location("E5"));
    	List<String> locationStr = Arrays.asList("G7", "F6");
    	testBishopPossibleMoveLocations(new Location("H8"), locationStr);
 
    }
    
    private void testBishopPossibleMoveLocations(Location bishopLocation, List<String> locationStr) {   	
    	Set<Location> expectedLocations = new HashSet<>();   	
    	locationStr.forEach(loc -> expectedLocations.add(new Location(loc)));
    	
    	BishopPiece bishopWhite = BishopPiece.getPiece(Colour.WHITE);
    	board.put(bishopWhite, bishopLocation);
    	
    	Set<Location> locations = bishopWhite.getPossibleMoveLocations(board, bishopLocation);
    	
    	assertTrue("Should contain all the expected move locations", 
    			locations.equals(expectedLocations));
    }
}
