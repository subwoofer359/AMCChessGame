package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RookPieceTest extends ChessPieceTest {
    private ChessBoard board;
    private Location testStartPosition = new Location("D5");

    private Location[] notValidLocationsFromD5 = { new Location("C6"),
            new Location("E6"), new Location("E4"),
            new Location("C4"), new Location("B7"),
            new Location("E7"), new Location("F4"),
            new Location("C3"), new Location("D5") };

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @Test
    @Override
    public void testOnEmptyBoardIsValidMove() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : ValidMovements.getListOfUpAndDownLocationsFromD5()) {
            System.out.println(endPosition);
            assertTrue(rook.isValidMove(board, new Move(testStartPosition, endPosition)));

        }
    }

    @Test
    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        RookPiece rook = RookPiece.getRookPiece(Colour.WHITE);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : notValidLocationsFromD5) {
            assertFalse(rook.isValidMove(board, new Move(testStartPosition, endPosition)));
        }
    }

    @Test
    @Override
    public void testOnBoardIsValidCapture() {
        List<Location> validCaptureLocations = new ArrayList<Location>();
        validCaptureLocations.add(new Location("D1"));
        validCaptureLocations.add(new Location("D8"));
        validCaptureLocations.add(new Location("H5"));
        validCaptureLocations.add(new Location("A5"));

        RookPiece rook = RookPiece.getRookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : validCaptureLocations) {
            board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), endPosition);
            assertTrue(rook.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    @Test
    @Override
    public void testOnBoardInvalidCapture() {
        List<Location> validCaptureLocations = new ArrayList<Location>();
        validCaptureLocations.add(new Location("D1"));
        validCaptureLocations.add(new Location("D8"));
        validCaptureLocations.add(new Location("H5"));
        validCaptureLocations.add(new Location("A5"));

        RookPiece rook = RookPiece.getRookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : validCaptureLocations) {
            board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.BLACK), endPosition);
            assertFalse(rook.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    @Test
    @Override
    public void testOnBoardIsNotValidMove() {
        List<Location> validMoveLocations = new ArrayList<Location>();
        validMoveLocations.add(new Location("D1"));
        validMoveLocations.add(new Location("D8"));
        validMoveLocations.add(new Location("H5"));
        validMoveLocations.add(new Location("A5"));

        List<Location> blockingPiecesPosition = new ArrayList<Location>();
        blockingPiecesPosition.add(new Location("D3"));
        blockingPiecesPosition.add(new Location("D7"));
        blockingPiecesPosition.add(new Location("E5"));
        blockingPiecesPosition.add(new Location("B5"));

        for (Location endPosition : blockingPiecesPosition) {
            board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.BLACK), endPosition);
        }

        RookPiece rook = RookPiece.getRookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : validMoveLocations) {

            assertFalse(rook.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    @Test
    public void testCanSlide() {
        assertTrue(RookPiece.getRookPiece(Colour.BLACK).canSlide());
    }

}
