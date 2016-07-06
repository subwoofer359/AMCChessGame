package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnPieceTest {
    private ChessBoard board;
    private Location testWhiteStartPosition = new Location("F2");
    private Location testBlackStartPosition = new Location("F7");

    private static final Location[] invalidWhiteMovesFromF2 = {
            new Location("F1"), new Location("E1"),
            new Location("E2"), new Location("G2"),
            new Location("G1"), new Location("E4"),
            new Location("G4"), new Location("F5"),
            new Location("D4"), new Location("H4"),
            new Location("H1") };

    private static final Location[] invalidBlackMovesFromF7 = {
            new Location("F8"), new Location("E8"),
            new Location("E7"), new Location("G7"),
            new Location("G8"), new Location("E5"),
            new Location("G5"), new Location("F4"),
            new Location("D5"), new Location("H5"),
            new Location("H8") };

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @Test
    public void testOnEmptyBoardIsValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);

        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition, new Location(
                        "F3"))));
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition, new Location(
                        "F4"))));

    }

    @Test
    public void testOnEmptyBoardIsValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);

        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition, new Location(
                        "F6"))));
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition, new Location(
                        "F5"))));

    }

    @Test
    public void testOnEmptyBoardIsNotValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);

        for (Location endLocation : invalidWhiteMovesFromF2) {
            assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocation)));
        }

    }

    @Test
    public void testOnEmptyBoardIsNotValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);

        for (Location endLocation : invalidBlackMovesFromF7) {
            assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, endLocation)));
        }

    }

    @Test
    public void testOnEmptyBoardIsNotValidNonIntialWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn = (PawnPiece)pawn.moved();
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocation = new Location(Coordinate.F, 4);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocation)));
    }

    @Test
    public void testOnEmptyBoardIsNotValidNonIntialBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        Location endLocation = new Location(Coordinate.F, 4);
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, endLocation)));
    }

    @Test
    public void testOnBoardIsValidWhiteCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location captureLocationOne = new Location(Coordinate.E, 3);
        Location captureLocationTwo = new Location(Coordinate.G, 3);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        board.putPieceOnBoardAt(enemyPawn, captureLocationOne);
        board.putPieceOnBoardAt(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition, captureLocationTwo)));
    }

    @Test
    public void testOnBoardIsValidBlackCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.WHITE);
        Location captureLocationOne = new Location(Coordinate.E, 6);
        Location captureLocationTwo = new Location(Coordinate.G, 6);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        board.putPieceOnBoardAt(enemyPawn, captureLocationOne);
        board.putPieceOnBoardAt(enemyPawn, captureLocationTwo);
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition, captureLocationOne)));
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition, captureLocationTwo)));
    }

    @Test
    public void testOnBoardInvalidWhiteCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.WHITE);
        Location captureLocationOne = new Location(Coordinate.E, 3);
        Location captureLocationTwo = new Location(Coordinate.G, 3);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        board.putPieceOnBoardAt(enemyPawn, captureLocationOne);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, captureLocationTwo)));
    }

    @Test
    public void testOnBoardInvalidBlackCapture() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        Location captureLocationOne = new Location(Coordinate.E, 6);
        Location captureLocationTwo = new Location(Coordinate.G, 6);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        board.putPieceOnBoardAt(enemyPawn, captureLocationOne);
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, captureLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, captureLocationTwo)));
    }

    @Test
    public void testOnBoardIsNotValidBlackMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        Location endLocationOne = new Location(Coordinate.F, 6);
        Location endLocationTwo = new Location(Coordinate.F, 5);

        board.putPieceOnBoardAt(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, endLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition, endLocationTwo)));
    }

    @Test
    public void testOnBoardIsNotValidWhiteMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        PawnPiece enemyPawn = PawnPiece.getPawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocationOne = new Location(Coordinate.F, 3);
        Location endLocationTwo = new Location(Coordinate.F, 4);

        board.putPieceOnBoardAt(enemyPawn, endLocationOne);

        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocationOne)));
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocationTwo)));
    }

    @Test
    public void testCanSlide() {
        assertTrue(PawnPiece.getPawnPiece(Colour.BLACK).canSlide());
    }

    @Test
    public void testIsMovingForwardOneSquareOnly() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocationOne = new Location(Coordinate.F, 3);
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocationOne)));
    }

    @Test
    public void testMoveBackOneSquare() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocationOne = new Location(Coordinate.F, 1);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition, endLocationOne)));
    }

    @Test
    public void testMoveNoSquare() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        Location startLocation = new Location(Coordinate.F, 3);
        board.putPieceOnBoardAt(pawn, startLocation);
        Location endLocationOne = new Location(Coordinate.F, 3);
        assertFalse(pawn.isValidMove(board, new Move(startLocation, endLocationOne)));
    }

}
