package org.amc.game.chess;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueenPieceTest implements ChessPieceTest {
    
    private ChessBoard board;
    private Location testStartPosition = new Location(ChessBoard.Coordinate.D, 5);

    private static List<Location> validLocationsFromD5;

    private static final Location[] notValidLocationsFromD5 = {
            new Location(ChessBoard.Coordinate.E, 7), new Location(ChessBoard.Coordinate.C, 7),
            new Location(ChessBoard.Coordinate.B, 6), new Location(ChessBoard.Coordinate.B, 4),
            new Location(ChessBoard.Coordinate.C, 3), new Location(ChessBoard.Coordinate.E, 3),
            new Location(ChessBoard.Coordinate.F, 4), new Location(ChessBoard.Coordinate.F, 6) };

    @BeforeClass
    public static void setUpClass() throws Exception {
        validLocationsFromD5 = new ArrayList<>();
        validLocationsFromD5.addAll(ValidMovements.getListOfDiagonalLocationsFromD5());
        validLocationsFromD5.addAll(ValidMovements.getListOfUpAndDownLocationsFromD5());
    }

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
        QueenPiece queen = new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(queen, testStartPosition);

        for (Location endPosition : validLocationsFromD5) {
            System.out.println(endPosition);
            assertTrue(queen.isValidMove(board, new Move(testStartPosition, endPosition)));

        }

    }

    @Test
    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        QueenPiece rook = new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(rook, testStartPosition);

        for (Location endPosition : notValidLocationsFromD5) {
            assertFalse(rook.isValidMove(board, new Move(testStartPosition, endPosition)));
        }
    }

    @Test
    @Override
    public void testOnBoardIsValidCapture() {
        QueenPiece queen = new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(queen, testStartPosition);
        List<Location> validCaptureLocations = new ArrayList<Location>();
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D, 1));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H, 5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.G, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 2));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H, 1));

        for (Location endPosition : validCaptureLocations) {
            board.putPieceOnBoardAt(new QueenPiece(Colour.BLACK), endPosition);
            System.out.println(endPosition);
            assertTrue(queen.isValidMove(board, new Move(testStartPosition, endPosition)));

        }
    }

    @Test
    @Override
    public void testOnBoardInvalidCapture() {
        QueenPiece queen = new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(queen, testStartPosition);
        List<Location> validCaptureLocations = new ArrayList<Location>();
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D, 1));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H, 5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.G, 8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A, 2));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H, 1));
        for (Location endPosition : validCaptureLocations) {
            board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE), endPosition);
            System.out.println(endPosition);
            assertFalse(queen.isValidMove(board, new Move(testStartPosition, endPosition)));

        }

    }

    @Test
    @Override
    public void testOnBoardIsNotValidMove() {
        QueenPiece queen = new QueenPiece(Colour.WHITE);
        QueenPiece blockingQueen = new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(queen, testStartPosition);
        board.putPieceOnBoardAt(blockingQueen, new Location(ChessBoard.Coordinate.B, 7));

        assertFalse(queen.isValidMove(board, new Move(testStartPosition, new Location(
                        ChessBoard.Coordinate.A, 1))));

    }

    @Test
    public void testCanSlisde(){
        assertTrue(new QueenPiece(Colour.BLACK).canSlide());
    }
}
