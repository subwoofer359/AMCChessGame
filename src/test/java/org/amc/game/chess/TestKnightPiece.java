package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestKnightPiece implements ChessPieceTest{

    private ChessBoard board;
    private Location testStartPosition=new Location(ChessBoard.Coordinate.D,4);
    
    
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Override
    public void testOnEmptyBoardIsValidMove() {
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight,testStartPosition);
        
        Location[] validLocations={
                        new Location(ChessBoard.Coordinate.B,5),
                        new Location(ChessBoard.Coordinate.C,6),
                        new Location(ChessBoard.Coordinate.E,6),
                        new Location(ChessBoard.Coordinate.F,5),
                        new Location(ChessBoard.Coordinate.B,3),
                        new Location(ChessBoard.Coordinate.C,2),
                        new Location(ChessBoard.Coordinate.E,2),
                        new Location(ChessBoard.Coordinate.F,3)
        };
        for(Location endPosition:validLocations){
            assertTrue(knight.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
    }

    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        board.putPieceOnBoardAt(knight,testStartPosition);
        
        Location[] notValidLocations={
                        new Location(ChessBoard.Coordinate.B,4),
                        new Location(ChessBoard.Coordinate.D,6),
                        new Location(ChessBoard.Coordinate.F,4),
                        new Location(ChessBoard.Coordinate.D,2),
                        new Location(ChessBoard.Coordinate.C,5),
                        new Location(ChessBoard.Coordinate.C,3),
                        new Location(ChessBoard.Coordinate.E,5),
                        new Location(ChessBoard.Coordinate.E,3)
        };
        for(Location endPosition:notValidLocations){
            assertFalse(knight.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
        
    }

    @Override
    public void testOnBoardIsValidCapture() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testOnBoardInvalidCapture() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testOnBoardIsNotValidMove() {
        // TODO Auto-generated method stub
        
    }
    
 
}
