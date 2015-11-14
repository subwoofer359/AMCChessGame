package org.amc.game.chess;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class BishopPieceTest extends ChessPieceTest {
    private ChessBoard board;
    private Location testStartPosition = new Location(ChessBoard.Coordinate.D, 5);

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amc.game.chess.ChessPieceTest#testOnEmptyBoardIsValidMove()
     */
    @Override
    @Test
    public void testOnEmptyBoardIsValidMove() {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, this.testStartPosition);

        for (Location endPosition : ValidMovements.getListOfDiagonalLocationsFromD5()) {
            System.out.println(endPosition);
            assertTrue(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amc.game.chess.ChessPieceTest#testOnEmptyBoardIsNotValidMove()
     */
    @Override
    @Test
    public void testOnEmptyBoardIsNotValidMove() throws Exception {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);

        board.putPieceOnBoardAt(bishop, new Location("F8"));
        boolean isValid = bishop.isValidMove(this.board, new Move("D4-G7"));
        isValid = isValid & bishop.isValidMove(this.board, new Move("D4-D6"));
        isValid = isValid & bishop.isValidMove(this.board, new Move("D4-A3"));
        isValid = isValid & bishop.isValidMove(this.board, new Move("D4-A8"));
        // A non move
        isValid = isValid & bishop.isValidMove(this.board, new Move("D4-D4"));
        assertFalse(isValid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardIsValidCapture()
     */
    @Override
    @Test
    public void testOnBoardIsValidCapture() throws ParseException {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, this.testStartPosition);

        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location("A8"));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location("G8"));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location("A2"));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location("H1"));

        for (Location endPosition : ValidMovements.getListOfDiagonalLocationsFromD5()) {
            System.out.println(endPosition);
            assertTrue(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardInvalidCapture()
     */
    @Override
    @Test
    public void testOnBoardInvalidCapture() {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, this.testStartPosition);

        for (Location endPosition : ValidMovements.getListOfDiagonalLocationsFromD5()) {
            board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), endPosition);
            assertFalse(bishop.isValidMove(board, new Move(testStartPosition, endPosition)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amc.game.chess.ChessPieceTest#testOnBoardIsNotValidMove()
     */
    @Override
    @Test
    public void testOnBoardIsNotValidMove() throws ParseException {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        BishopPiece bishopWhite = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, new Location("F8"));
        board.putPieceOnBoardAt(bishopWhite, new Location("D6"));

        boolean isValid = bishop.isValidMove(this.board, new Move("F8-C5"));
        assertFalse(isValid);
    }

    @Test
    @Override
    public void testCanSlide() {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        assertTrue(bishop.canSlide());
    }
}
