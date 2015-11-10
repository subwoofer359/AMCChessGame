package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.amc.game.chess.ChessGame.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameTest {

    private ChessGameFixture chessGameFixture;
    private Location endLocation;
    private Location startLocation;

    @Before
    public void setUp() throws Exception {
        chessGameFixture = new ChessGameFixture();
        chessGameFixture.putPieceOnBoardAt(new KingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        chessGameFixture.putPieceOnBoardAt(new KingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        startLocation = new Location(A, 8);
        endLocation = new Location(B, 7);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testChangePlayer() {
        assertEquals(chessGameFixture.getWhitePlayer(), chessGameFixture.getCurrentPlayer());
        chessGameFixture.changePlayer();
        assertEquals(chessGameFixture.getBlackPlayer(), chessGameFixture.getCurrentPlayer());
        chessGameFixture.changePlayer();
        assertEquals(chessGameFixture.getWhitePlayer(), chessGameFixture.getCurrentPlayer());
    }

    @Test(expected = IllegalMoveException.class)
    public void testMoveWithAnEmptySquare() throws IllegalMoveException {
        chessGameFixture.move(chessGameFixture.getWhitePlayer(), new Move(startLocation, endLocation));
    }

    @Test(expected = IllegalMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        chessGameFixture.putPieceOnBoardAt(bishop, startLocation);
        chessGameFixture.move(chessGameFixture.getBlackPlayer(), new Move(startLocation, new Location(B, 7)));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        chessGameFixture.putPieceOnBoardAt(bishop, startLocation);
        chessGameFixture.move(chessGameFixture.getWhitePlayer(), new Move(startLocation, endLocation));
        assertEquals(bishop, chessGameFixture.getPieceFromBoardAt(endLocation));
        assertNull(chessGameFixture.getPieceFromBoardAt(startLocation));
    }

    @Test
    public void doesGameRuleApply() {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(G, 1));
        chessGameFixture.putPieceOnBoardAt(rook, rookStartPosition);
        assertTrue(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void doesNotGameRuleApply() {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(F, 1));
        chessGameFixture.putPieceOnBoardAt(rook, rookStartPosition);
        assertFalse(chessGameFixture.doesAGameRuleApply(chessGameFixture, move));
    }

    @Test
    public void gameRuleApplied() throws IllegalMoveException {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(F, 1));
        chessGameFixture.putPieceOnBoardAt(rook, rookStartPosition);
        chessGameFixture.move(chessGameFixture.getWhitePlayer(), move);
    }
    
    @Test
    public void testMovesAreSaved() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        chessGameFixture.putPieceOnBoardAt(bishop, startLocation);
        Move move = new Move(startLocation, endLocation);
        chessGameFixture.changePlayer();
        chessGameFixture.move(chessGameFixture.getBlackPlayer(), move);
        Move lastMove = chessGameFixture.getTheLastMove();
        assertEquals(lastMove.getStart(), startLocation);
        assertEquals(lastMove.getEnd(), endLocation);
    }

    @Test
    public void getEmptyMove() {
        Move move = chessGameFixture.getTheLastMove();
        assertTrue(move instanceof EmptyMove);
    }
    
    @Test
    public void cloneConstuctorMoveListCopyTest() {
        chessGameFixture.initialise();
        ChessGame clone = new ChessGame(chessGameFixture.getChessGame());
        assertTrue(chessGameFixture.getTheLastMove().equals(clone.getTheLastMove()));
        assertEquals(GameState.RUNNING, clone.getGameState());
    }
    
    @Test
    public void cloneConstuctorRuleListCopyTest() {
        chessGameFixture.initialise();
        ChessGame clone = new ChessGame(chessGameFixture.getChessGame());
        clone.addChessMoveRule(EnPassantRule.getInstance());
        assertEquals(3, chessGameFixture.getChessGame().getChessMoveRules().size());
        assertEquals(4, clone.getChessMoveRules().size());
    }

    /**
     * JIRA CG-33 Player can make a move out of turn
     * 
     * @throws IllegalMoveException
     */
    @Test(expected = IllegalMoveException.class)
    public void notPlayersTurn() throws IllegalMoveException {
        Move move = new Move(StartingSquare.BLACK_KING.getLocation(), new Location(E, 7));
        chessGameFixture.move(chessGameFixture.getBlackPlayer(), move);
        assertEquals(chessGameFixture.getWhitePlayer(), chessGameFixture.getCurrentPlayer());
    }
}
