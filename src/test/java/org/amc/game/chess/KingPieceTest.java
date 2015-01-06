package org.amc.game.chess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class KingPieceTest {
    private ChessBoard board;

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsValidMoveOnEmptyBoard() {
        KingPiece king = new KingPiece(Colour.BLACK);
        board.putPieceOnBoardAt(king, new Location(F, 7));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(F, 8))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(F, 6))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(G, 7))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(E, 7))));

        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(E, 8))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(G, 8))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(G, 6))));
        assertTrue(king.isValidMove(board, new Move(new Location(F, 7), new Location(E, 8))));
    }

    @Test
    public void testIsNotValidMoveOnEmptyBoard() {
        KingPiece king = new KingPiece(Colour.BLACK);
        board.putPieceOnBoardAt(king, new Location(F, 7));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(A, 8))));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(B, 6))));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(G, 3))));
        assertFalse(king.isValidMove(board,
                        new Move(new Location(F, 7), StartingSquare.WHITE_KING.getLocation())));

        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(D, 2))));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(C, 3))));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(G, 3))));
        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(D, 8))));

        assertFalse(king.isValidMove(board, new Move(new Location(F, 7), new Location(F, 7))));

    }

    @Test
    public void testIsValidMoveOnBoard() {
        KingPiece king = new KingPiece(Colour.BLACK);
        KingPiece kingWhite = new KingPiece(Colour.WHITE);
        KingPiece anotherBlackKing = new KingPiece(Colour.BLACK);
        board.putPieceOnBoardAt(king, new Location(F, 7));
        board.putPieceOnBoardAt(kingWhite, new Location(E, 7));
        board.putPieceOnBoardAt(anotherBlackKing, new Location(G, 7));

        // Move to square occupied by the white king should be valid
        boolean isValid = king.isValidMove(board, new Move(new Location(F, 7), new Location(E, 7)));
        // Move to square occupied by the another king should not be valid
        boolean notValid = king
                        .isValidMove(board, new Move(new Location(F, 7), new Location(G, 7)));

        assertTrue(isValid);
        assertFalse(notValid);
    }

    @Test
    public void testGetPossibleMoveLocations() {
        KingPiece whiteKing = new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(whiteKing, StartingSquare.WHITE_KING.getLocation());
        Set<Location> expectedMoveLocations = new HashSet<>();
        expectedMoveLocations.add(new Location(D, 1));
        expectedMoveLocations.add(new Location(D, 2));
        expectedMoveLocations.add(new Location(E, 2));
        expectedMoveLocations.add(new Location(F, 1));
        expectedMoveLocations.add(new Location(F, 2));

        Set<Location> possibleMoveLocations = whiteKing.getPossibleMoveLocations(board,
                        StartingSquare.WHITE_KING.getLocation());

        assertTrue(expectedMoveLocations.equals(possibleMoveLocations));
    }

    @Test
    public void testCanSlisde() {
        assertTrue(new KingPiece(Colour.BLACK).canSlide());
    }
}
