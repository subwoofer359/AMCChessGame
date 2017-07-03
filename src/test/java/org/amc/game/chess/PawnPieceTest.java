package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
    public void setUp() throws Exception {
        board = new ChessBoard();
        setChessBoard(board);
    }
    
    @Test
    @Override
    public void testCanSlide() {
        assertTrue(PawnPiece.getPawnPiece(Colour.BLACK).canSlide());
    }

    @Test
    public void testIsMovingForwardOneSquareOnly() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F3";
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
    }

    @Test
    public void testMoveBackOneSquare() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F1";
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
    }

    @Test
    public void testMoveNoSquare() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
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

    private void testOnEmptyBoardIsValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        placeOnBoard(pawn, whiteStart);

        assertTrue(pawn.isValidMove(board, newMove(whiteStart, "F3")));
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, "F4")));

    }
    
    private Move newMove(String start, String end) {
    	return new Move(start + Move.LOCATION_SEPARATOR + end);
    }

    private void testOnEmptyBoardIsValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        placeOnBoard(pawn, (blackStart));

        assertTrue(pawn.isValidMove(board, newMove(blackStart, "F6")));
        assertTrue(pawn.isValidMove(board, newMove(blackStart, "F5")));

    }

    private void testOnEmptyBoardIsNotValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        placeOnBoard(pawn, whiteStart);

        for (String endLocation : invalidWhiteMovesFromF2) {
            assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocation)));
        }

    }

    private void testOnEmptyBoardIsNotValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        placeOnBoard(pawn, blackStart);
        
        for (String endLocation : invalidBlackMovesFromF7) {
            assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocation)));
        }

    }

    private void testOnEmptyBoardIsNotValidNonIntialWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn = (PawnPiece)pawn.moved();
        placeOnBoard(pawn, whiteStart);
        String endLocation = "F4";
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocation)));
    }

    private void testOnEmptyBoardIsNotValidNonIntialBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        pawn.moved();
        placeOnBoard(pawn, blackStart);
        String endLocation = "F4";
        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocation)));
    }

    private void testOnBoardIsValidWhiteCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        String captureLocationOne = "E3";
        String captureLocationTwo = "G3";
        placeOnBoard(pawn, whiteStart);
        placeOnBoard(enemyPawn, captureLocationOne);
        placeOnBoard(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, newMove(whiteStart, captureLocationTwo)));
    }

    private void testOnBoardIsValidBlackCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.WHITE);
        String captureLocationOne = "E6";
        String captureLocationTwo = "G6";
        placeOnBoard(pawn, blackStart);
        placeOnBoard(enemyPawn, captureLocationOne);
        placeOnBoard(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, newMove(blackStart, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, newMove(blackStart, captureLocationTwo)));
    }

    private void testOnBoardInvalidWhiteCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        String captureLocationOne = "E3";
        String captureLocationTwo = "G3";
        placeOnBoard(pawn, whiteStart);
        placeOnBoard(PawnPiece.getPawnPiece(Colour.WHITE), captureLocationOne);
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, captureLocationTwo)));
    }

    private void testOnBoardInvalidBlackCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        String captureLocationOne = "E6";
        String captureLocationTwo = "G6";
        placeOnBoard(pawn, blackStart);
        placeOnBoard(PawnPiece.getPawnPiece(Colour.BLACK), captureLocationOne);
        assertFalse(pawn.isValidMove(board, newMove(blackStart, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(blackStart, captureLocationTwo)));
    }

    private void testOnBoardIsNotValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.WHITE);
        placeOnBoard(pawn, blackStart);
        String endLocationOne = "F6";
        String endLocationTwo = "F5";

        placeOnBoard(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(blackStart, endLocationTwo)));
    }

    private void testOnBoardIsNotValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        placeOnBoard(pawn, whiteStart);
        String endLocationOne = "F3";
        String endLocationTwo = "F4";

        placeOnBoard(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationOne)));
        assertFalse(pawn.isValidMove(board, newMove(whiteStart, endLocationTwo)));
    }
}
