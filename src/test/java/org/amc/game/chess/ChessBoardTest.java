package org.amc.game.chess;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ChessBoardTest {

    private ChessBoard board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Location endLocation;
    private Location startLocation;

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
        whitePlayer = new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer = new HumanPlayer("Robin", Colour.BLACK);
        startLocation = new Location(ChessBoard.Coordinate.A, 8);
        endLocation = new Location(ChessBoard.Coordinate.B, 7);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    public void testInitialise() {
        fail("Not yet implemented");
    }

    @Test(expected = InvalidMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws InvalidMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        board.move(blackPlayer, new Move(startLocation, new Location(
                        ChessBoard.Coordinate.B, 7)));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws InvalidMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        board.move(whitePlayer, new Move(startLocation, endLocation));
        assertEquals(bishop, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }

    @Test(expected = InvalidMoveException.class)
    public void testMoveWithAnEmptySquare() throws InvalidMoveException {
        board.move(whitePlayer, new Move(startLocation, endLocation));
    }

    @Test
    public void testMovesAreSaved() throws InvalidMoveException {
        Player playerOne = new HumanPlayer("Stephen", Colour.BLACK);
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, startLocation);
        Move move = new Move(startLocation, endLocation);
        board.move(playerOne, move);
        Move lastMove = board.getTheLastMove();
        assertEquals(lastMove.getStart(), startLocation);
        assertEquals(lastMove.getEnd(), endLocation);
    }
}
