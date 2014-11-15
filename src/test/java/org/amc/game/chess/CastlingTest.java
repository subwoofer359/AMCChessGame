package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CastlingTest {
    private ChessBoard board;
    private KingPiece whiteKing;
    private RookPiece whiteLeftRook;
    private RookPiece whiteRightRook;
    private Location whiteKingStartPosition;
    private Location whiteLeftRookStartPosition;
    private Location whiteRightRookStartPosition;
    private ChessGame chessGame;
    private CastlingRule gameRule;
    private Player whitePlayer;
    private Player blackPlayer;
    
    @Before
    public void setUp() throws Exception {
        board=new ChessBoard();
        whitePlayer=new HumanPlayer("White Player",Colour.WHITE);
        blackPlayer=new HumanPlayer("Black Player", Colour.BLACK);
        chessGame=new ChessGame(board,whitePlayer,blackPlayer);
        gameRule=new CastlingRule();
        whiteKing=new KingPiece(Colour.WHITE);
        whiteLeftRook=new RookPiece(Colour.WHITE);
        whiteRightRook=new RookPiece(Colour.WHITE);
        whiteKingStartPosition=new Location(E,1);
        whiteLeftRookStartPosition=new Location(A,1);
        whiteRightRookStartPosition=new Location(H,1);
        board.putPieceOnBoardAt(whiteKing, whiteKingStartPosition);
        board.putPieceOnBoardAt(whiteRightRook, whiteRightRookStartPosition);
        board.putPieceOnBoardAt(whiteLeftRook, whiteLeftRookStartPosition);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLeftSideCastling(){
        Location castlingKingLocation=new Location(C,1);
        assertTrue(whiteKing.isCastlingMove(new Move(whiteKingStartPosition,castlingKingLocation)));
        assertTrue(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testRightSideCastling(){
        Location castlingKingLocation=new Location(G,1);
        assertTrue(whiteKing.isCastlingMove(new Move(whiteKingStartPosition,castlingKingLocation)));
        assertTrue(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testKingMovedCastlingNotAllowed(){
        Location castlingKingLocation=new Location(G,1);
        whiteKing.moved();
        assertFalse(whiteKing.isCastlingMove(new Move(whiteKingStartPosition,castlingKingLocation)));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testRightRookMovedCastlingNotAllowed(){
        Location castlingKingLocation=new Location(G,1);
        whiteRightRook.moved();
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testLeftRookMovedCastlingNotAllowed(){
        Location castlingKingLocation=new Location(C,1);
        whiteLeftRook.moved();
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testKingHasMoveOneSquare(){
        Location castlingKingLocation=new Location(F,1);
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testKingHasTwoSquareUpAndAcrossTheBoard(){
        Location castlingKingLocation=new Location(G,3);
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testNotLeftRook(){
        Location castlingKingLocation=new Location(C,1);
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), whiteLeftRookStartPosition);
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testNotRightRook(){
        Location castlingKingLocation=new Location(G,1);
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), whiteRightRookStartPosition);
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testSquareBetweenKingAndRightRookNotEmpty(){
        Location castlingKingLocation=new Location(G,1);
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(F,1));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(G,1));
        board.removePieceOnBoardAt(new Location(F,1));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testSquareBetweenKingAndLeftRookNotEmpty(){
        Location castlingKingLocation=new Location(C,1);
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(B,1));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(C,1));
        board.removePieceOnBoardAt(new Location(B,1));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
        board.removePieceOnBoardAt(new Location(C,1));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(D,1));
        assertFalse(gameRule.isCastlingMove(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testRightRookMovesToCastlePosition() throws InvalidMoveException{
        Location castlingKingLocation=new Location(G,1);
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
    }
    
    @Test
    public void testLeftRookMovesToCastlePosition() throws InvalidMoveException{
        Location castlingKingLocation=new Location(C,1);
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
    }
}
