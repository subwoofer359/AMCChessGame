package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnPieceTest  {
    private ChessBoard board;
    private Location testWhiteStartPosition = new Location(ChessBoard.Coordinate.F, 2);
    private Location testBlackStartPosition = new Location(ChessBoard.Coordinate.F, 7);
    
    private static final Location[] invalidWhiteMovesFromF2={
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
    
    private static final Location[] invalidBlackMovesFromF7={
        new Location(ChessBoard.Coordinate.F,8),
        new Location(ChessBoard.Coordinate.E,8),
        new Location(ChessBoard.Coordinate.E,7),
        new Location(ChessBoard.Coordinate.G,7),
        new Location(ChessBoard.Coordinate.G,8),
        new Location(ChessBoard.Coordinate.E,5),
        new Location(ChessBoard.Coordinate.G,5),
        new Location(ChessBoard.Coordinate.F,4),
        new Location(ChessBoard.Coordinate.D,5),
        new Location(ChessBoard.Coordinate.H,5),
    };
    
    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOnEmptyBoardIsValidWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,
                        new Location(ChessBoard.Coordinate.F,3))));
        assertTrue(pawn.isValidMove(board, new Move(testWhiteStartPosition,
                        new Location(ChessBoard.Coordinate.F,4))));
        
    }
    
    @Test
    public void testOnEmptyBoardIsValidBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,
                        new Location(ChessBoard.Coordinate.F,6))));
        assertTrue(pawn.isValidMove(board, new Move(testBlackStartPosition,
                        new Location(ChessBoard.Coordinate.F,5))));
        
    }

    @Test
    public void testOnEmptyBoardIsNotValidWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        
        for(Location endLocation:invalidWhiteMovesFromF2){
            assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocation)));
        }
        
    }
    
    @Test
    public void testOnEmptyBoardIsNotValidBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        
        for(Location endLocation:invalidBlackMovesFromF7){
            assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocation)));
        }
        
    }
    

    @Test
    public void testOnEmptyBoardIsNotValidNonIntialWhiteMove() {
        PawnPiece pawn=new PawnPiece(Colour.WHITE);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testWhiteStartPosition);
        Location endLocation=new Location(Coordinate.F,4);
        assertFalse(pawn.isValidMove(board, new Move(testWhiteStartPosition,endLocation)));
    }
    
    @Test
    public void testOnEmptyBoardIsNotValidNonIntialBlackMove() {
        PawnPiece pawn=new PawnPiece(Colour.BLACK);
        pawn.moved();
        board.putPieceOnBoardAt(pawn, testBlackStartPosition);
        Location endLocation=new Location(Coordinate.F,4);
        assertFalse(pawn.isValidMove(board, new Move(testBlackStartPosition,endLocation)));
    }
    
    public void testOnBoardIsValidCapture() {

        
    }

    public void testOnBoardInvalidCapture() {
      
    }

    public void testOnBoardIsNotValidMove() {
        // TODO Auto-generated method stub
        
    }
    
}
