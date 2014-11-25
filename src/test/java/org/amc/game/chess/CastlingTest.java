package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.amc.game.chess.StartingSquare.WHITE_KING;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_LEFT;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_RIGHT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CastlingTest {
    private ChessBoard board;
    private KingPiece whiteKing;
    private RookPiece whiteLeftRook;
    private RookPiece whiteRightRook;
    private Location whiteKingStartPosition;
    private final Location castlingKingRightLocation=new Location(G,1);
    private final Location castlingKingLeftLocation=new Location(C,1);
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
        whiteKingStartPosition=WHITE_KING.getLocation();
        whiteLeftRookStartPosition=WHITE_ROOK_LEFT.getLocation();
        whiteRightRookStartPosition=WHITE_ROOK_RIGHT.getLocation();
        board.putPieceOnBoardAt(whiteKing, whiteKingStartPosition);
        board.putPieceOnBoardAt(whiteRightRook, whiteRightRookStartPosition);
        board.putPieceOnBoardAt(whiteLeftRook, whiteLeftRookStartPosition);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLeftSideCastling(){
        assertTrue(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
    }
    
    @Test
    public void testRightSideCastling(){
        assertTrue(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
    }
    
    @Test
    public void testKingMovedCastlingNotAllowed(){
        whiteKing.moved();
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
    }
    
    @Test
    public void testRightRookMovedCastlingNotAllowed(){
        whiteRightRook.moved();
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
    }
    
    @Test
    public void testLeftRookMovedCastlingNotAllowed(){
        whiteLeftRook.moved();
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
    }
    
    @Test
    public void testKingHasMoveOneSquare(){
        Location castlingKingLocation=new Location(F,1);
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testKingHasTwoSquareUpAndAcrossTheBoard(){
        Location castlingKingLocation=new Location(G,3);
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLocation)));
    }
    
    @Test
    public void testNotLeftRook(){
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), whiteLeftRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
    }
    
    @Test
    public void testNotRightRook(){
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), whiteRightRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
    }
    
    @Test
    public void testSquareBetweenKingAndRightRookNotEmpty(){
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(F,1));
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), castlingKingRightLocation);
        board.removePieceOnBoardAt(new Location(F,1));
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingRightLocation)));
    }
    
    @Test
    public void testSquareBetweenKingAndLeftRookNotEmpty(){
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(B,1));
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), castlingKingLeftLocation);
        board.removePieceOnBoardAt(new Location(B,1));
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
        board.removePieceOnBoardAt(castlingKingLeftLocation);
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(D,1));
        assertFalse(gameRule.isRuleApplicable(board,new Move(whiteKingStartPosition,castlingKingLeftLocation)));
    }
    
    @Test
    public void testRightRookMovesToCastlePosition() throws InvalidMoveException{
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
        ChessPiece piece=board.getPieceFromBoardAt(new Location(F,1));
        assertEquals(piece, whiteRightRook);
    }
    
    @Test
    public void testLeftRookMovesToCastlePosition() throws InvalidMoveException{
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
        ChessPiece piece=board.getPieceFromBoardAt(new Location(D,1));
        assertEquals(piece, whiteLeftRook);
    }
    
    @Test
    public void testKingMovesLefttoCastlePosition() throws InvalidMoveException{
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
        ChessPiece piece=board.getPieceFromBoardAt(castlingKingRightLocation);
        assertEquals(piece, whiteKing);
    }
    
    @Test
    public void testKingMovesRighttoCastlePosition() throws InvalidMoveException{
        Move whiteKingCastleMove=new Move(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(whitePlayer, whiteKingCastleMove);
        ChessPiece piece=board.getPieceFromBoardAt(castlingKingLeftLocation);
        assertEquals(piece, whiteKing);
    }
}
