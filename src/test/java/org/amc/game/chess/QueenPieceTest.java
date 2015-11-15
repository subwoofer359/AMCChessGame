package org.amc.game.chess;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueenPieceTest extends ChessPieceTest {

    private ChessBoard board;
    private Location testStartPosition = new Location("D5");

    private static List<Location> validLocationsFromD5;

    private static final Location[] notValidLocationsFromD5 = {
            new Location("E7"), new Location("C7"),
            new Location("B6"), new Location("B4"),
            new Location("C3"), new Location("E3"),
            new Location("F4"), new Location("F6") };

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
        validCaptureLocations.add(new Location("D1"));
        validCaptureLocations.add(new Location("D8"));
        validCaptureLocations.add(new Location("H5"));
        validCaptureLocations.add(new Location("A5"));
        validCaptureLocations.add(new Location("A8"));
        validCaptureLocations.add(new Location("G8"));
        validCaptureLocations.add(new Location("A2"));
        validCaptureLocations.add(new Location("H1"));

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
        validCaptureLocations.add(new Location("D1"));
        validCaptureLocations.add(new Location("D8"));
        validCaptureLocations.add(new Location("H5"));
        validCaptureLocations.add(new Location("A5"));
        validCaptureLocations.add(new Location("A8"));
        validCaptureLocations.add(new Location("G8"));
        validCaptureLocations.add(new Location("A2"));
        validCaptureLocations.add(new Location("H1"));
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
        board.putPieceOnBoardAt(blockingQueen, new Location("B7"));

        assertFalse(queen.isValidMove(board, new Move(testStartPosition, new Location(
                        "A1"))));

    }

    @Test
    @Override
    public void testCanSlide() {
        assertTrue(new QueenPiece(Colour.BLACK).canSlide());
    }

    // @Test
    // @Override
    // public void testCopy() {
    // this.piece = new QueenPiece(Colour.WHITE);
    // super.testCopy();
    // }
}
