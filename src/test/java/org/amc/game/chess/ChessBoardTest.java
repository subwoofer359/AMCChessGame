package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

public class ChessBoardTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private Location endLocation;
    private Location startLocation;

    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("White Player",Colour.WHITE);
        blackPlayer=new HumanPlayer("Black Player", Colour.BLACK);
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
            boolean moveToggle=false;
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
    
    @Test
    public void CloneConstuctorMoveListCopyTest(){
        board.initialise();  
        ChessBoard clone=new ChessBoard(board);
        assertTrue(board.getTheLastMove().equals(clone.getTheLastMove()));
    }
    
    @Test
    public void CloneConstuctorPieceMovedCopyTest(){
        board.initialise();
        board.getPieceFromBoardAt(StartingSquare.BLACK_KING.getLocation()).moved();
        board.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()).moved();
        ChessBoard clone=new ChessBoard(board);
        assertTrue(clone.getPieceFromBoardAt(StartingSquare.BLACK_KING.getLocation()).hasMoved());
        assertTrue(clone.getPieceFromBoardAt(StartingSquare.WHITE_KING.getLocation()).hasMoved());
        assertFalse(clone.getPieceFromBoardAt(StartingSquare.BLACK_KNIGHT_LEFT.getLocation()).hasMoved());
        assertFalse(clone.getPieceFromBoardAt(StartingSquare.WHITE_ROOK_LEFT.getLocation()).hasMoved());
        
    }
    
    @Test
    public void getListOfPlayersPiecesOnTheBoardTest()throws ParseException{
        ChessBoardFactory factory=new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        ChessBoard board=factory.getChessBoard("Ke1:ke2:Bf1:nf3:na1:ra2");
        List<?> blackPieceList=board.getListOfPlayersPiecesOnTheBoard(blackPlayer);
        List<?> whitePieceList=board.getListOfPlayersPiecesOnTheBoard(whitePlayer);
        assertTrue(blackPieceList.size()==2);
        assertTrue(whitePieceList.size()==4);
    }
}
