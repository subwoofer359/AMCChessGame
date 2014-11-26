package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReversibleMoveTest {
    private ChessBoard board;
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
        board.initialise();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Location pawnStartLocation=new Location(A,2);
        Location pawnEndLocation=new Location(A,3);
        Move pawnMove=new Move(pawnStartLocation,pawnEndLocation);
        ReversibleMove reversibleMove=new ReversibleMove(board, pawnMove);
        reversibleMove.move();
        assertTrue(board.getPieceFromBoardAt(pawnStartLocation) == null);
        assertTrue(board.getPieceFromBoardAt(pawnEndLocation) instanceof PawnPiece);
        assertTrue(board.getTheLastMove()==pawnMove);
        assertTrue(board.allGameMoves.contains(pawnMove));
        reversibleMove.undoMove();
        assertTrue(board.getPieceFromBoardAt(pawnStartLocation) instanceof PawnPiece);
        assertTrue(board.getPieceFromBoardAt(pawnEndLocation) == null);
        assertFalse(board.allGameMoves.contains(pawnMove));
    }

}
