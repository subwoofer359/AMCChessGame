package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KnightPieceTest extends ChessPieceTest {

    private ChessBoard board;
    private Location testStartPosition = new Location(ChessBoard.Coordinate.D, 4);

    private Location[] validLocationsFromD4 = { new Location(ChessBoard.Coordinate.B, 5),
            new Location(ChessBoard.Coordinate.C, 6), new Location(ChessBoard.Coordinate.E, 6),
            new Location(ChessBoard.Coordinate.F, 5), new Location(ChessBoard.Coordinate.B, 3),
            new Location(ChessBoard.Coordinate.C, 2), new Location(ChessBoard.Coordinate.E, 2),
            new Location(ChessBoard.Coordinate.F, 3) };

    private Location[] notValidLocationsFromD4 = { new Location(ChessBoard.Coordinate.B, 4),
            new Location(ChessBoard.Coordinate.D, 6), new Location(ChessBoard.Coordinate.F, 4),
            new Location(ChessBoard.Coordinate.D, 2), new Location(ChessBoard.Coordinate.C, 5),
            new Location(ChessBoard.Coordinate.C, 3), new Location(ChessBoard.Coordinate.E, 5),
            new Location(ChessBoard.Coordinate.E, 3) };

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Override
    public void testOnEmptyBoardIsValidMove() {
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight, testStartPosition);

        for (Location endPosition : validLocationsFromD4) {
            assertTrue(knight.isValidMove(board, new Move(testStartPosition, endPosition)));
        }
    }

    @Test
    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight, testStartPosition);

        for (Location endPosition : notValidLocationsFromD4) {
            assertFalse(knight.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    @Test
    @Override
    public void testOnBoardIsValidCapture() {
        for (int i = 0; i < validLocationsFromD4.length; i++) {
            board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), validLocationsFromD4[i]);
        }
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight, testStartPosition);

        for (Location endPosition : validLocationsFromD4) {
            assertTrue(knight.isValidMove(board, new Move(testStartPosition, endPosition)));
        }
    }

    @Test
    @Override
    public void testOnBoardInvalidCapture() {
        for (int i = 0; i < validLocationsFromD4.length; i++) {
            board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), validLocationsFromD4[i]);
        }
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight, testStartPosition);

        for (Location endPosition : validLocationsFromD4) {
            assertFalse(knight.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    @Override
    public void testOnBoardIsNotValidMove() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testCanSlide() {
        assertFalse(new KnightPiece(Colour.BLACK).canSlide());
    }
}
