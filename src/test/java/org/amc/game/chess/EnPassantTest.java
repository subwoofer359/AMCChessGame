package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.Before;
import org.junit.Test;

public class EnPassantTest {
    private ChessBoard board;
    
    @Before
    public void setUp(){
        board=new ChessBoard();
    }

    @Test
    public void testIsEnPassantCapture(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertTrue(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCapture(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        BishopPiece blackBishop=new BishopPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackBishop, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureMoveLessTwo(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,6);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.F,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureAfterTwoSquareMove(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,7);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.D,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }
    
    @Test
    public void testIsNotEnPassantCaptureAfterTwoSeparateSquareMove(){
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(Coordinate.E,5);
        Location blackPawnStartPosition=new Location(Coordinate.F,6);
        Location blackPawnEndPosition=new Location(Coordinate.F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(Coordinate.D,6));
        
        board.allGameMoves.add(blackMove);
        assertFalse(whitePawn.isEnPassantCapture(board, whiteEnPassantMove));
    }

}
