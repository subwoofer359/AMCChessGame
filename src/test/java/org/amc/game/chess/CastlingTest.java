package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.amc.game.chess.StartingSquare.WHITE_KING;
import static org.amc.game.chess.StartingSquare.BLACK_KING;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_LEFT;
import static org.amc.game.chess.StartingSquare.WHITE_ROOK_RIGHT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CastlingTest {
    private KingPiece whiteKing;
    private KingPiece blackKing;
    private RookPiece whiteLeftRook;
    private RookPiece whiteRightRook;
    private Location whiteKingStartPosition;
    private final Location castlingKingRightLocation = new Location(G, 1);
    private final Location castlingKingLeftLocation = new Location(C, 1);
    private Location whiteLeftRookStartPosition;
    private Location whiteRightRookStartPosition;
    private CastlingRule gameRule;
    private ChessGameFixture chessGameFixture;
    private ChessGame chessGame;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        chessGame = chessGameFixture.getChessGame();

        gameRule = CastlingRule.getInstance();
        whiteKing = KingPiece.getKingPiece(Colour.WHITE);
        blackKing = KingPiece.getKingPiece(Colour.BLACK);
        whiteLeftRook = new RookPiece(Colour.WHITE);
        whiteRightRook = new RookPiece(Colour.WHITE);
        whiteKingStartPosition = WHITE_KING.getLocation();
        whiteLeftRookStartPosition = WHITE_ROOK_LEFT.getLocation();
        whiteRightRookStartPosition = WHITE_ROOK_RIGHT.getLocation();
        chessGameFixture.putPieceOnBoardAt(whiteKing, whiteKingStartPosition);
        chessGameFixture.putPieceOnBoardAt(whiteRightRook, whiteRightRookStartPosition);
        chessGameFixture.putPieceOnBoardAt(whiteLeftRook, whiteLeftRookStartPosition);
        chessGameFixture.putPieceOnBoardAt(blackKing, BLACK_KING.getLocation());

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLeftSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testRightSideCastling() {
        assertTrue(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testKingMovedCastlingNotAllowed() {
        chessGameFixture.putPieceOnBoardAt(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testRightRookMovedCastlingNotAllowed() {
        chessGameFixture.putPieceOnBoardAt(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testLeftRookMovedCastlingNotAllowed() {
        chessGameFixture.putPieceOnBoardAt(whiteKing.moved(), whiteKingStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testKingHasMoveOneSquare() {
        Location castlingKingLocation = new Location(F, 1);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLocation)));
    }

    @Test
    public void testKingHasTwoSquareUpAndAcrossTheBoard() {
        Location castlingKingLocation = new Location(G, 3);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLocation)));
    }

    @Test
    public void testNotLeftRook() {
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE),
                        whiteLeftRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testNotRightRook() {
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE),
                        whiteRightRookStartPosition);
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testSquareBetweenKingAndRightRookNotEmpty() {
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), new Location(F, 1));
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), castlingKingRightLocation);
        chessGameFixture.removePieceOnBoardAt(new Location(F, 1));
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingRightLocation)));
    }

    @Test
    public void testSquareBetweenKingAndLeftRookNotEmpty() {
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), new Location(B, 1));
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), castlingKingLeftLocation);
        chessGameFixture.removePieceOnBoardAt(new Location(B, 1));
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
        chessGameFixture.removePieceOnBoardAt(castlingKingLeftLocation);
        chessGameFixture.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), new Location(D, 1));
        assertFalse(gameRule.isRuleApplicable(chessGame, new Move(whiteKingStartPosition,
                        castlingKingLeftLocation)));
    }

    @Test
    public void testRightRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = new Move(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = chessGameFixture.getPieceFromBoardAt(new Location(F, 1));
        assertEquals(piece, whiteRightRook);
    }

    @Test
    public void testLeftRookMovesToCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = new Move(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = chessGameFixture.getPieceFromBoardAt(new Location(D, 1));
        assertEquals(piece, whiteLeftRook);
    }

    @Test
    public void testKingMovesRighttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = new Move(whiteKingStartPosition, castlingKingRightLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = chessGameFixture.getPieceFromBoardAt(castlingKingRightLocation);
        assertEquals(piece, whiteKing.moved());
    }

    @Test
    public void testKingMovesLefttoCastlePosition() throws IllegalMoveException {
        Move whiteKingCastleMove = new Move(whiteKingStartPosition, castlingKingLeftLocation);
        chessGame.move(chessGameFixture.getWhitePlayer(), whiteKingCastleMove);
        ChessPiece piece = chessGameFixture.getPieceFromBoardAt(castlingKingLeftLocation);
        assertEquals(piece, whiteKing.moved());
    }
}
