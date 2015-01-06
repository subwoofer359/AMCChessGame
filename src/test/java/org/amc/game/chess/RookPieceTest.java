package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RookPieceTest extends ChessPieceTest{
    private ChessBoard board;
    private Location testStartPosition=new Location(ChessBoard.Coordinate.D,5);
    
    
    private Location[] notValidLocationsFromD5={
                    new Location(ChessBoard.Coordinate.C,6),
                    new Location(ChessBoard.Coordinate.E,6),
                    new Location(ChessBoard.Coordinate.E,4),
                    new Location(ChessBoard.Coordinate.C,4),
                    new Location(ChessBoard.Coordinate.B,7),
                    new Location(ChessBoard.Coordinate.E,7),
                    new Location(ChessBoard.Coordinate.F,4),
                    new Location(ChessBoard.Coordinate.C,3),
                    new Location(ChessBoard.Coordinate.D,5)
    };
    
    
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
        RookPiece rook=new RookPiece(Colour.WHITE);
        board.putPieceOnBoardAt(rook,testStartPosition);
        
        for(Location endPosition:ValidMovements.getListOfUpAndDownLocationsFromD5()){
            System.out.println(endPosition);
            assertTrue(rook.isValidMove(board, new Move(testStartPosition,endPosition)));
            
        }
    }

    @Test
    @Override
    public void testOnEmptyBoardIsNotValidMove() {
        RookPiece rook=new RookPiece(Colour.WHITE);
        board.putPieceOnBoardAt(rook,testStartPosition);
        
        for(Location endPosition:notValidLocationsFromD5){
            assertFalse(rook.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
    }

    @Test
    @Override
    public void testOnBoardIsValidCapture() {
        List<Location> validCaptureLocations=new ArrayList<Location>();
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D,1));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D,8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H,5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A,5));
        
        RookPiece rook = new RookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook,testStartPosition);
        
        for(Location endPosition:validCaptureLocations){
            board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), endPosition);
            assertTrue(rook.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
        
    }

    @Test
    @Override
    public void testOnBoardInvalidCapture() {
        List<Location> validCaptureLocations=new ArrayList<Location>();
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D,1));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.D,8));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.H,5));
        validCaptureLocations.add(new Location(ChessBoard.Coordinate.A,5));
       
        RookPiece rook = new RookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook,testStartPosition);
        
        for(Location endPosition:validCaptureLocations){
            board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), endPosition);
            assertFalse(rook.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
        
    }

    @Test
    @Override
    public void testOnBoardIsNotValidMove() {
        List<Location> validMoveLocations=new ArrayList<Location>();
        validMoveLocations.add(new Location(ChessBoard.Coordinate.D,1));
        validMoveLocations.add(new Location(ChessBoard.Coordinate.D,8));
        validMoveLocations.add(new Location(ChessBoard.Coordinate.H,5));
        validMoveLocations.add(new Location(ChessBoard.Coordinate.A,5));
        
        
        List<Location> blockingPiecesPosition=new ArrayList<Location>();
        blockingPiecesPosition.add(new Location(ChessBoard.Coordinate.D,3));
        blockingPiecesPosition.add(new Location(ChessBoard.Coordinate.D,7));
        blockingPiecesPosition.add(new Location(ChessBoard.Coordinate.E,5));
        blockingPiecesPosition.add(new Location(ChessBoard.Coordinate.B,5));
        
        for(Location endPosition:blockingPiecesPosition){
            board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), endPosition);
        }
        
        RookPiece rook = new RookPiece(Colour.BLACK);
        board.putPieceOnBoardAt(rook,testStartPosition);
        
        for(Location endPosition:validMoveLocations){
            
            assertFalse(rook.isValidMove(board, new Move(testStartPosition,endPosition)));
        }
        
    }
    
    @Test
    public void testCanSlide(){
        assertTrue(new RookPiece(Colour.BLACK).canSlide());
    }

}
