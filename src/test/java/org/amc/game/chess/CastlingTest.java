package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.StartingSquare.WHITE_KING;
import static org.amc.game.chess.StartingSquare.BLACK_KING;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_LEFT;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_RIGHT;

import org.junit.Before;
import org.junit.Test;

public class CastlingTest {
    private KingPiece whiteKing;
    private RookPiece whiteLeftRook;
    private RookPiece whiteRightRook;
    private String whiteKingStartPosition;
    private final String castlingKingRightLocation = "G1";
    private final String castlingKingLeftLocation = "C1";
    private String whiteLeftRookStartPosition;
    private String whiteRightRookStartPosition;
    private CastlingRule gameRule;
    private ChessGameFixture chessGameFixture;
    private ChessGame chessGame;
    
    private ChessBoardUtil cbUtils;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        chessGame = chessGameFixture.getChessGame();
        cbUtils = new ChessBoardUtil(chessGame.getChessBoard());

        gameRule = CastlingRule.getInstance();
        whiteKing = KingPiece.getPiece(Colour.WHITE);
        ChessPiece blackKing = KingPiece.getPiece(Colour.BLACK);
        whiteLeftRook = RookPiece.getPiece(Colour.WHITE);
        whiteRightRook = RookPiece.getPiece(Colour.WHITE);
        whiteKingStartPosition = WHITE_KING.getLocation().asString();
        whiteLeftRookStartPosition = WHITE_ROOK_LEFT.getLocation().asString();
        whiteRightRookStartPosition = WHITE_ROOK_RIGHT.getLocation().asString();
        cbUtils.add(whiteKing, whiteKingStartPosition);
        cbUtils.add(whiteRightRook, whiteRightRookStartPosition);
        cbUtils.add(whiteLeftRook, whiteLeftRookStartPosition);
        cbUtils.add(blackKing, BLACK_KING.getLocation().asString());

    }

    @Test
    public void testLeftSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testRightSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testKingMovedCastlingNotAllowed() {
        cbUtils.add(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testRightRookMovedCastlingNotAllowed() {
        cbUtils.add(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testLeftRookMovedCastlingNotAllowed() {
        cbUtils.add(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testKingHasMoveOneSquare() {
        String castlingKingLocation = "F1";
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLocation)));
    }

    @Test
    public void testKingHasTwoSquareUpAndAcrossTheBoard() {
        String castlingKingLocation = "G3";
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLocation)));
    }

    @Test
    public void testNotLeftRook() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE),
                        whiteLeftRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testNotRightRook() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE),
                        whiteRightRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testSquareBetweenKingAndRightRookNotEmpty() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "F1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), castlingKingRightLocation);
        cbUtils.remove("F1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testSquareBetweenKingAndLeftRookNotEmpty() {
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "B1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), castlingKingLeftLocation);
        cbUtils.remove("B1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
        cbUtils.remove(castlingKingLeftLocation);
        cbUtils.add(BishopPiece.getPiece(Colour.WHITE), "D1");
        assertFalse(gameRule.isRuleApplicable(chessGame, cbUtils.newMove(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testRightRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece("F1");
        assertEquals(piece, whiteRightRook.moved());
    }

    @Test
    public void testLeftRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece("D1");
        assertEquals(piece, whiteLeftRook.moved());
    }

    @Test
    public void testKingMovesRighttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece(castlingKingRightLocation);
        assertEquals(piece, whiteKing.moved());
    }

    @Test
    public void testKingMovesLefttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = cbUtils.newMove(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = cbUtils.getPiece(castlingKingLeftLocation);
        assertEquals(piece, whiteKing.moved());
    }
}
