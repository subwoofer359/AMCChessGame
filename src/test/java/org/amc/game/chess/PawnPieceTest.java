package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PawnPieceTest implements ChessPieceTest  {
    private ChessBoard board;
    private Location testStartPosition = new Location(ChessBoard.Coordinate.D, 5);
    
    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Override
    public void testOnEmptyBoardIsValidMove() {

        
    }

    @Override
    public void testOnEmptyBoardIsNotValidMove() {

        
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
