package org.amc.game.chess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testIsValidMoveOnEmptyBoard() {
        KingPiece king = KingPiece.getKingPiece(Colour.BLACK);
        addChessPieceToBoard(king, "F7");
        assertTrue(king.isValidMove(board, createMove("F7", "F8")));
        assertTrue(king.isValidMove(board, createMove("F7", "F6")));
        assertTrue(king.isValidMove(board, createMove("F7", "G7")));
        assertTrue(king.isValidMove(board, createMove("F7", "E7")));

        assertTrue(king.isValidMove(board, createMove("F7", "E8")));
        assertTrue(king.isValidMove(board, createMove("F7", "G8")));
        assertTrue(king.isValidMove(board, createMove("F7", "G6")));
        assertTrue(king.isValidMove(board, createMove("F7", "E8")));
    }
    
    private Move createMove(String start, String end) {
        return new Move(start + Move.MOVE_SEPARATOR + end);
    }
    
    private void addChessPieceToBoard(ChessPiece piece, String location) {
        board.putPieceOnBoardAt(piece, new Location(location));
    }

    @Test
    public void testIsNotValidMoveOnEmptyBoard() {
        KingPiece king = KingPiece.getKingPiece(Colour.BLACK);
        addChessPieceToBoard(king, "F7");
        assertFalse(king.isValidMove(board, createMove("F7", "A8")));
        assertFalse(king.isValidMove(board, createMove("F7", "B6")));
        assertFalse(king.isValidMove(board, createMove("F7", "G3")));
        assertFalse(king.isValidMove(board,
                        createMove("F7", StartingSquare.WHITE_KING.getLocation().asString())));

        assertFalse(king.isValidMove(board, createMove("F7", "D2")));
        assertFalse(king.isValidMove(board, createMove("F7", "C3")));
        assertFalse(king.isValidMove(board, createMove("F7", "G3")));
        assertFalse(king.isValidMove(board, createMove("F7", "D8")));

        assertFalse(king.isValidMove(board, createMove("F7", "F7")));

    }

    @Test
    public void testIsValidMoveOnBoard() {
        KingPiece king = KingPiece.getKingPiece(Colour.BLACK);
        KingPiece kingWhite = KingPiece.getKingPiece(Colour.WHITE);
        KingPiece anotherBlackKing = KingPiece.getKingPiece(Colour.BLACK);
        addChessPieceToBoard(king, "F7");
        addChessPieceToBoard(kingWhite, "E7");
        addChessPieceToBoard(anotherBlackKing, "G7");

        // Move to square occupied by the white king should be valid
        boolean isValid = king.isValidMove(board, createMove("F7", "E7"));
        // Move to square occupied by the another king should not be valid
        boolean notValid = king
                        .isValidMove(board, createMove("F7", "G7"));

        assertTrue(isValid);
        assertFalse(notValid);
    }

    @Test
    public void testGetPossibleMoveLocations() {
        KingPiece whiteKing = KingPiece.getKingPiece(Colour.WHITE);
        addChessPieceToBoard(whiteKing, StartingSquare.WHITE_KING.getLocation().asString());
        Set<Location> expectedMoveLocations = new HashSet<>();
        expectedMoveLocations.add(new Location("D1"));
        expectedMoveLocations.add(new Location("D2"));
        expectedMoveLocations.add(new Location("E2"));
        expectedMoveLocations.add(new Location("F1"));
        expectedMoveLocations.add(new Location("F2"));

        Set<Location> possibleMoveLocations = whiteKing.getPossibleMoveLocations(board,
                        StartingSquare.WHITE_KING.getLocation());

        assertTrue(expectedMoveLocations.equals(possibleMoveLocations));
    }

    @Test
    public void testCanSlisde() {
        assertTrue(KingPiece.getKingPiece(Colour.BLACK).canSlide());
    }
}
