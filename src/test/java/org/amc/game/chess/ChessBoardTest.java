package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
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
}
