package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueenPieceTest implements ChessPieceTest {
    private ChessBoard board;
    private Location testStartPosition=new Location(ChessBoard.Coordinate.D,5);
    
    private static List<Location> validLocationsFromD5;
    
    @BeforeClass
    public static void setUpClass() throws Exception{
        validLocationsFromD5=new ArrayList<>();
        validLocationsFromD5.addAll(ValidMovements.getListOfDiagonalLocationsFromD5());
        validLocationsFromD5.addAll(ValidMovements.getListOfUpAndDownLocationsFromD5());
    }
    
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
        QueenPiece queen=new QueenPiece(Colour.WHITE);
        board.putPieceOnBoardAt(queen,testStartPosition);
        
        for(Location endPosition:validLocationsFromD5){
            System.out.println(endPosition);
            assertTrue(queen.isValidMove(board, new Move(testStartPosition,endPosition)));
            
        }
        
    }

    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        // TODO Auto-generated method stub
        
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
