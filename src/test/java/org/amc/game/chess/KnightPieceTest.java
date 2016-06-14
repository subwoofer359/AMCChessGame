package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KnightPieceTest extends ChessPieceTest {

    private ChessBoard board;
    private Location testStartPosition = new Location("D4");

    private Location[] validLocationsFromD4 = { new Location("B5"),
            new Location("C6"), new Location("E6"),
            new Location("F5"), new Location("B3"),
            new Location("C2"), new Location("E2"),
            new Location("F3") };

    private Location[] notValidLocationsFromD4 = { new Location("B4"),
            new Location("D6"), new Location("F4"),
            new Location("D2"), new Location("C5"),
            new Location("C3"), new Location("E5"),
            new Location("E3") };

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
            board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), validLocationsFromD4[i]);
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
            board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.BLACK), validLocationsFromD4[i]);
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
