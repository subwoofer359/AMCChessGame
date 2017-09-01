package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import org.amc.game.chess.view.ChessBoardView;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PawnPieceTest extends ChessPieceTest {
    private ChessBoard board;
    private String whiteStart = "F2";
    private String blackStart = "F7";

    private static final String[] invalidWhiteMovesFromF2 = {
            "F1", "E1",
            "E2", "G2",
            "G1", "E4",
            "G4", "F5",
            "D4", "H4",
            "H1"
    };

    private static final String[] invalidBlackMovesFromF7 = {
            "F8", "E8",
            "E7", "G7",
            "G8", "E5",
            "G5", "F4",
            "D5", "H5",
            "H8" 
    };

    @Before
    public void setUp() {
        board = new ChessBoard();
        setChessBoard(board);
    }
    
    @Test
    @Override
    public void testCanSlide() {
        assertTrue(PawnPiece.getPiece(Colour.BLACK).canSlide());
    }

    @Test
    public void testIsMovingForwardOneSquareOnly() {
        PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F3";
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
    }

    @Test
    public void testMoveBackOneSquare() {
        PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F1";
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
    }

    @Test
    public void testMoveNoSquare() {
        PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        String startLocation = "F3";
        placeOnBoard(pawn, startLocation);
        String endLocationOne = "F3";
        assertFalse(pawn.isValidMove(board, newMove(startLocation, endLocationOne)));
    }
    
	@Override
	@Test
	public void testOnEmptyBoardIsValidMove() throws Exception {
		testOnEmptyBoardIsValidBlackMove();
		testOnEmptyBoardIsValidWhiteMove();
		
	}

	@Override
	@Test
	public void testOnEmptyBoardIsNotValidMove() throws Exception {
		testOnEmptyBoardIsNotValidBlackMove();
		testOnEmptyBoardIsNotValidWhiteMove();
		testOnEmptyBoardIsNotValidNonIntialWhiteMove();
		testOnEmptyBoardIsNotValidNonIntialBlackMove();
		
	}

	@Override
	@Test
	public void testOnBoardIsValidCapture() throws Exception {
		testOnBoardIsValidBlackCapture();
		testOnBoardIsValidWhiteCapture();
		
	}

	@Override
	@Test
	public void testOnBoardInvalidCapture() throws Exception {
		testOnBoardInvalidBlackCapture();
		testOnBoardInvalidWhiteCapture();
		
	}

	@Override
	@Test
	public void testOnBoardIsNotValidMove() throws Exception {
		testOnBoardIsNotValidWhiteMove();
		testOnBoardIsNotValidBlackMove();
	}

	@Test
    public void testOnBoardIsNotValidBlackMove2() {
        PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPiece(Colour.WHITE);
        placeOnBoard(pawn, blackStart);
        String endLocationOne = "F6";
        String endLocationTwo = "F5";

        placeOnBoard(enemyPawn, endLocationTwo);
        
        new ChessBoardView(board).displayTheBoard();

        assertTrue(pawn.isValidMove(board, newMove(blackStart, endLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocationTwo)));
    }
	
	@Test
	public void testPossibleMoveLocationsForWhite() {
		List<String> locationStr = Arrays.asList("C4", "C3", "B3");
		Location location = new Location("c2");
		
		PawnPiece blackPiece = PawnPiece.getPiece(Colour.BLACK);
		placeOnBoard(blackPiece, "b3");
		
		testPawnPossibleMoveLocations(Colour.WHITE, location, locationStr);
	}
	
	@Test
	public void testPossibleMoveLocationsForWhiteBlockedByWhite() {
		List<String> locationStr = Arrays.asList("C4", "C3");
		Location location = new Location("c2");
		
		PawnPiece blackPiece = PawnPiece.getPiece(Colour.WHITE);
		placeOnBoard(blackPiece, "b3");
		
		testPawnPossibleMoveLocations(Colour.WHITE, location, locationStr);
	}
	
	@Test
	public void testPossibleMoveLocationsForWhiteBlockedByBlack() {
		Location location = new Location("c2");
		
		PawnPiece blackPiece = PawnPiece.getPiece(Colour.BLACK);
		placeOnBoard(blackPiece, "c3");

		testPawnPossibleMoveLocations(Colour.WHITE, location, Collections.emptyList());
	}
	
	@Test
	public void testPossibleMoveLocationsForWhiteHalfBlockedByBlack() {
		List<String> locationStr = Arrays.asList("C3");
		
		Location location = new Location("c2");
		
		PawnPiece blackPiece = PawnPiece.getPiece(Colour.BLACK);
		placeOnBoard(blackPiece, "c4");
		
		testPawnPossibleMoveLocations(Colour.WHITE, location, locationStr);
	}
	
	@Test
	public void testPossibleMoveLocationsForWhiteAtEnd() {
		
		Location location = new Location("h8");
		
		testPawnPossibleMoveLocations(Colour.WHITE, location, Collections.emptyList());
	}
	
	private void testPawnPossibleMoveLocations(Colour pawnColour,Location pawnLocation, List<String> locationStr) {   	
    	Set<Location> expectedLocations = new HashSet<>();   	
    	locationStr.forEach(loc -> expectedLocations.add(new Location(loc)));
    	
    	PawnPiece pawn = PawnPiece.getPiece(pawnColour);
    	board.put(pawn, pawnLocation);
    	
    	Set<Location> locations = pawn.getPossibleMoveLocations(board, pawnLocation);
    	
    	assertTrue("Should contain all the expected move locations", 
    			locations.equals(expectedLocations));
    }
	
	@Test
	public void testPossibleMoveLocationsForBlack() {
		List<String> locationStr = Arrays.asList("C6", "C5", "B6");
		
		Location location = new Location("c7");
		
		PawnPiece whitePiece = PawnPiece.getPiece(Colour.WHITE);
		placeOnBoard(whitePiece, "b6");
		
		testPawnPossibleMoveLocations(Colour.BLACK, location, locationStr);
	}
	
	@Test
	public void testPossibleMoveLocationsForBlackAtEdge() {
		List<String> locationStr = Arrays.asList("A6", "A5", "B6");
		
		Location location = new Location("a7");
		
		PawnPiece whitePiece = PawnPiece.getPiece(Colour.WHITE);
		placeOnBoard(whitePiece, "B6");

		testPawnPossibleMoveLocations(Colour.BLACK, location, locationStr);
	}
	
	@Test
	public void testPossibleMoveLocationsForBlackAtEdgeAtEnd() {
		Location location = new Location("a1");
		
		testPawnPossibleMoveLocations(Colour.BLACK, location, Collections.emptyList());
		
	}
	
    private void testOnEmptyBoardIsValidWhiteMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        placeOnBoard(pawn, whiteStart);

        assertTrue(pawn.isValidMove(board, newMove(whiteStart, "F3")));
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, "F4")));

    }
    
    private Move newMove(String start, String end) {
    	return new Move(start + Move.LOCATION_SEPARATOR + end);
    }

    private void testOnEmptyBoardIsValidBlackMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        placeOnBoard(pawn, (blackStart));

        assertTrue(pawn.isValidMove(board, newMove(blackStart, "F6")));
        assertTrue(pawn.isValidMove(board, newMove(blackStart, "F5")));

    }

    private void testOnEmptyBoardIsNotValidWhiteMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        placeOnBoard(pawn, whiteStart);

        for (String endLocation : invalidWhiteMovesFromF2) {
            assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocation)));
        }

    }

    private void testOnEmptyBoardIsNotValidBlackMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        placeOnBoard(pawn, blackStart);
        
        for (String endLocation : invalidBlackMovesFromF7) {
            assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocation)));
        }

    }

    private void testOnEmptyBoardIsNotValidNonIntialWhiteMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        pawn = (PawnPiece)pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocation = "F4";
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocation)));
    }

    private void testOnEmptyBoardIsNotValidNonIntialBlackMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        pawn.moved();
        placeOnBoard(pawn, blackStart);
        String endLocation = "F4";
        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocation)));
    }

    private void testOnBoardIsValidWhiteCapture() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPiece(Colour.BLACK);
        String captureLocationOne = "E3";
        String captureLocationTwo = "G3";
        placeOnBoard(pawn, whiteStart);
        placeOnBoard(enemyPawn, captureLocationOne);
        placeOnBoard(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, captureLocationTwo)));
    }

    private void testOnBoardIsValidBlackCapture() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPiece(Colour.WHITE);
        String captureLocationOne = "E6";
        String captureLocationTwo = "G6";
        placeOnBoard(pawn, blackStart);
        placeOnBoard(enemyPawn, captureLocationOne);
        placeOnBoard(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, newMove(blackStart, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, newMove(blackStart, captureLocationTwo)));
    }

    private void testOnBoardInvalidWhiteCapture() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        String captureLocationOne = "E3";
        String captureLocationTwo = "G3";
        placeOnBoard(pawn, whiteStart);
        placeOnBoard(PawnPiece.getPiece(Colour.WHITE), captureLocationOne);
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, captureLocationTwo)));
    }

    private void testOnBoardInvalidBlackCapture() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        String captureLocationOne = "E6";
        String captureLocationTwo = "G6";
        placeOnBoard(pawn, blackStart);
        placeOnBoard(PawnPiece.getPiece(Colour.BLACK), captureLocationOne);
        assertFalse(pawn.isValidMove(board, newMove(blackStart, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(blackStart, captureLocationTwo)));
    }

    private void testOnBoardIsNotValidBlackMove() {
    	setUp();
    	PawnPiece pawn = PawnPiece.getPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPiece(Colour.WHITE);
        placeOnBoard(pawn, blackStart);
        String endLocationOne = "F6";
        String endLocationTwo = "F5";

        placeOnBoard(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocationTwo)));
    }
    
    private void testOnBoardIsNotValidWhiteMove() {
    	setUp();
        PawnPiece pawn = PawnPiece.getPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPiece(Colour.BLACK);
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F3";
        String endLocationTwo = "F4";

        placeOnBoard(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationTwo)));
    }
}
