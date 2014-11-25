package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.Before;
import org.junit.Test;

public class EnPassantTest {
    private ChessBoard board;
    private EnPassantRule enPassantRule;
    @Before
    public void setUp(){
        board=new ChessBoard();
        enPassantRule=new EnPassantRule();
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
        assertTrue(enPassantRule.isEnPassantCapture(board, whiteEnPassantMove));
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
        assertFalse(enPassantRule.isEnPassantCapture(board, whiteEnPassantMove));
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
        assertFalse(enPassantRule.isEnPassantCapture(board, whiteEnPassantMove));
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
        assertFalse(enPassantRule.isEnPassantCapture(board, whiteEnPassantMove));
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
        assertFalse(enPassantRule.isEnPassantCapture(board, whiteEnPassantMove));
    }

    @Test
    public void TestWhiteEnPassantCapture()throws InvalidMoveException{
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(E,5);
        Location whitePawnEndPosition=new Location(F,6);
        Location blackPawnStartPosition=new Location(F,7);
        Location blackPawnEndPosition=new Location(F,5);
        board.putPieceOnBoardAt(whitePawn, whitePawnStartPosition);
        board.putPieceOnBoardAt(blackPawn, blackPawnEndPosition);
        
        Move blackMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteEnPassantMove =new Move(whitePawnStartPosition,new Location(F,6));
        
        board.allGameMoves.add(blackMove);
        
        enPassantRule.applyRule(board, whiteEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(whitePawnEndPosition).equals(whitePawn));
        assertNull(board.getPieceFromBoardAt(blackPawnEndPosition));
    }
    
    @Test
    public void TestBlackEnPassantCapture()throws InvalidMoveException{
        PawnPiece whitePawn=new PawnPiece(Colour.WHITE);
        PawnPiece blackPawn=new PawnPiece(Colour.BLACK);
        Location whitePawnStartPosition=new Location(F,2);
        Location whitePawnEndPosition=new Location(F,4);
        Location blackPawnStartPosition=new Location(G,4);
        Location blackPawnEndPosition=new Location(F,3);
        board.putPieceOnBoardAt(blackPawn, blackPawnStartPosition);
        board.putPieceOnBoardAt(whitePawn, whitePawnEndPosition);
        
        Move blackEnPassantMove =new Move(blackPawnStartPosition,blackPawnEndPosition);
        Move whiteMove =new Move(whitePawnStartPosition,whitePawnEndPosition);
        
        board.allGameMoves.add(whiteMove);
        
        enPassantRule.applyRule(board, blackEnPassantMove);
        assertTrue(board.getPieceFromBoardAt(blackPawnEndPosition).equals(blackPawn));
        assertNull(board.getPieceFromBoardAt(whitePawnEndPosition));
    }
    
    @Test
    public void notMoveEnPassantCapture() {
        BishopPiece bishop=new BishopPiece(Colour.WHITE);
        Location startSquare=new Location(A,2);
        Location endSquare=new Location(B,3);
        Move move =new Move(startSquare,endSquare);
        
        board.putPieceOnBoardAt(bishop,startSquare);
        EnPassantRule rule =new EnPassantRule();
        
        assertFalse(rule.isMoveEnPassantCapture(board,move));
        
    }
    
}
