package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessBoardTest {

    private ChessBoard board;
    private Location endLocation;
    private Location startLocation;

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
        startLocation = new Location(ChessBoard.Coordinate.A, 8);
        endLocation = new Location(ChessBoard.Coordinate.B, 7);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMovesAreSaved() throws InvalidMoveException {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, startLocation);
        Move move = new Move(startLocation, endLocation);
        board.move(move);
        Move lastMove = board.getTheLastMove();
        assertEquals(lastMove.getStart(), startLocation);
        assertEquals(lastMove.getEnd(), endLocation);
    }
    
    @Test
    public void getEmptyMove(){
        Move move=board.getTheLastMove();
        assertTrue(move instanceof EmptyMove);
    }
    
    @Test
    public void testInitialse(){
        board.initialise();
        for(int i=7;i<=8;i++){
            for(Coordinate coord:Coordinate.values()){
                ChessPiece piece=board.getPieceFromBoardAt(coord.getIndex(), i);
                assertTrue(piece instanceof ChessPiece);
                assertEquals(piece.getColour(),Colour.BLACK);
            }
        }
        for(int i=3;i<=6;i++){
            for(Coordinate coord:Coordinate.values()){
                assertNull(board.getPieceFromBoardAt(coord.getIndex(), i));
            }
        }
        for(int i=1;i<=2;i++){
            for(Coordinate coord:Coordinate.values()){
                ChessPiece piece=board.getPieceFromBoardAt(coord.getIndex(), i);
                assertTrue(piece instanceof ChessPiece);
                assertEquals(piece.getColour(),Colour.WHITE);
            }
        }
        
    }
    
    /**
     * Tests the copying constructor of ChessBoard and copy method of ChessPiece
     */
    @Test
    public void CloneConstructorTest(){
        board.initialise();
        ChessBoard clone=new ChessBoard(board);       
        for(Coordinate coord:ChessBoard.Coordinate.values()){
            for(int i=1;i<=ChessBoard.BOARD_WIDTH;i++){
                Location location=new Location(coord,i);
                ChessPiece piece=board.getPieceFromBoardAt(location);
                ChessPiece clonedPiece=clone.getPieceFromBoardAt(location);
                if(piece instanceof ChessPiece){
                    assertFalse(piece==clonedPiece);
                    assertTrue(piece.equals(clonedPiece));
                }else{
                    assertNull(piece);
                    assertNull(clonedPiece);
                }
            }
        }
    }
}
