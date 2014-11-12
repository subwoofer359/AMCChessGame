package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnPieceTest implements ChessPieceTest  {
    private ChessBoard board;
    private Location testStartPosition = new Location(ChessBoard.Coordinate.F, 2);
    private static final Location[] invalidMovesFromF2={
        new Location(ChessBoard.Coordinate.F,1),
        new Location(ChessBoard.Coordinate.E,1),
        new Location(ChessBoard.Coordinate.E,2),
        new Location(ChessBoard.Coordinate.G,2),
        new Location(ChessBoard.Coordinate.G,1),
        new Location(ChessBoard.Coordinate.E,4),
        new Location(ChessBoard.Coordinate.G,4),
        new Location(ChessBoard.Coordinate.F,5),
        new Location(ChessBoard.Coordinate.D,4),
        new Location(ChessBoard.Coordinate.H,4),
    };
    
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
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testStartPosition);
        
        assertTrue(pawn.isValidMove(board, new Move(testStartPosition,
                        new Location(ChessBoard.Coordinate.F,3))));
        assertTrue(pawn.isValidMove(board, new Move(testStartPosition,
                        new Location(ChessBoard.Coordinate.F,4))));
        
    }

    @Test
    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testStartPosition);
        
        for(Location endLocation:invalidMovesFromF2){
            assertFalse(pawn.isValidMove(board, new Move(testStartPosition,endLocation)));
        }
        
    }

    @Test
    public void testOnEmptyBoardIsNotValidNonIntialMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testStartPosition);
        Location endLocation=new Location(Coordinate.F,4);
        assertFalse(pawn.isValidMove(board, new Move(testStartPosition,endLocation)));
        
        
    }
    
    @Override
    public void testOnBoardIsValidCapture() {

        
    }

    @Override
    public void testOnBoardInvalidCapture() {
      
    }

    @Override
    public void testOnBoardIsNotValidMove() {
        // TODO Auto-generated method stub
        
    }
    
}
