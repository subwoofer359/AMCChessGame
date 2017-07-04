package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.StartingSquare.WHITE_KING;
import static org.amc.game.chess.StartingSquare.BLACK_KING;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_LEFT;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_RIGHT;

import org.junit.Before;
import org.junit.Test;

public class CastlingTest {
    private KingPiece king;
    private RookPiece leftRook;
    private RookPiece rightRook;
    private String kingStart;
    private final String castlingRight = "G1";
    private final String castlingLeft = "C1";
    private String leftRookStart;
	private String rightRookStart;
    private CastlingRule gameRule;
    private ChessGame chessGame;
    
    private ChessBoardUtil cbUtils;

    @Before
    public void setUp() throws Exception {
        chessGame = new ChessGameFixture().getChessGame();
        cbUtils = new ChessBoardUtil(chessGame.getChessBoard());

        gameRule = CastlingRule.getInstance();
        king = KingPiece.getPiece(Colour.WHITE);
        ChessPiece blackKing = KingPiece.getPiece(Colour.BLACK);
        
        leftRook = RookPiece.getPiece(Colour.WHITE);
        rightRook = RookPiece.getPiece(Colour.WHITE);
        kingStart = WHITE_KING.getLocation().asString();
        leftRookStart = WHITE_ROOK_LEFT.getLocation().asString();
        rightRookStart = WHITE_ROOK_RIGHT.getLocation().asString();
        
        cbUtils.add(king, kingStart);
        cbUtils.add(rightRook, rightRookStart);
        cbUtils.add(leftRook, leftRookStart);
        cbUtils.add(blackKing, BLACK_KING.getLocation().asString());

    }

    @Test
    public void testLeftSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, 
        		cbUtils.newMove(kingStart, castlingLeft)));
    }

    @Test
    public void testRightSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
    }

    @Test
    public void testKingMovedCastlingNotAllowed() {
        cbUtils.add(king.moved(), kingStart);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
    }

    @Test
    public void testRightRookMovedCastlingNotAllowed() {
        cbUtils.add(king.moved(), kingStart);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
    }

    @Test
    public void testLeftRookMovedCastlingNotAllowed() {
        cbUtils.add(king.moved(), kingStart);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingLeft)));
    }

    @Test
    public void testKingHasMoveOneSquare() {
        String castlingKingLocation = "F1";
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingKingLocation)));
    }

    @Test
    public void testKingHasTwoSquareUpAndAcrossTheBoard() {
        String castlingKingLocation = "G3";
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingKingLocation)));
    }

    @Test
    public void testNotLeftRook() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE),
                        leftRookStart);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingLeft)));
    }

    @Test
    public void testNotRightRook() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE),
                        rightRookStart);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
    }

    @Test
    public void testSquareBetweenKingAndRightRookNotEmpty() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "F1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), castlingRight);
        cbUtils.remove("F1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingRight)));
    }

    @Test
    public void testSquareBetweenKingAndLeftRookNotEmpty() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "B1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingLeft)));
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), castlingLeft);
        cbUtils.remove("B1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingLeft)));
        cbUtils.remove(castlingLeft);
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "D1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(kingStart,
                        castlingLeft)));
    }

    @Test
    public void testRightRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(kingStart, castlingRight);
        chessGame.move(chessGame.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece("F1");
        assertEquals(piece, rightRook.moved());
    }

    @Test
    public void testLeftRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(kingStart, castlingLeft);
        chessGame.move(chessGame.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece("D1");
        assertEquals(piece, leftRook.moved());
    }

    @Test
    public void testKingMovesRighttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(kingStart, castlingRight);
        chessGame.move(chessGame.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece(castlingRight);
        assertEquals(piece, king.moved());
    }

    @Test
    public void testKingMovesLefttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(kingStart, castlingLeft);
        chessGame.move(chessGame.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece(castlingLeft);
        assertEquals(piece, king.moved());
    }
}
