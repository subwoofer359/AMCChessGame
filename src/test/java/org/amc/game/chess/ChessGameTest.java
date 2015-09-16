package org.amc.game.chess;

import static org.junit.Assert.*;
import static org.amc.game.chess.ChessBoard.Coordinate.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameTest {

    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private Location endLocation;
    private Location startLocation;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
        board = new ChessBoard();
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        startLocation = new Location(A, 8);
        endLocation = new Location(B, 7);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testChangePlayer() {
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(blackPlayer, chessGame.getCurrentPlayer());
        chessGame.changePlayer();
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
    }

    @Test(expected = IllegalMoveException.class)
    public void testMoveWithAnEmptySquare() throws IllegalMoveException {
        chessGame.move(whitePlayer, new Move(startLocation, endLocation));
    }

    @Test(expected = IllegalMoveException.class)
    public void testPlayerCantMoveOtherPlayersPiece() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        chessGame.move(blackPlayer, new Move(startLocation, new Location(B, 7)));
    }

    @Test
    public void testPlayerCanMoveTheirOwnPiece() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        board.putPieceOnBoardAt(bishop, startLocation);
        chessGame.move(whitePlayer, new Move(startLocation, endLocation));
        assertEquals(bishop, board.getPieceFromBoardAt(endLocation));
        assertNull(board.getPieceFromBoardAt(startLocation));
    }

    @Test
    public void doesGameRuleApply() {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(G, 1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertTrue(chessGame.doesAGameRuleApply(chessGame, move));
    }

    @Test
    public void doesNotGameRuleApply() {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(F, 1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        assertFalse(chessGame.doesAGameRuleApply(chessGame, move));
    }

    @Test
    public void gameRuleApplied() throws IllegalMoveException {
        RookPiece rook = new RookPiece(Colour.WHITE);
        Location rookStartPosition = new Location(H, 1);
        Move move = new Move(StartingSquare.WHITE_KING.getLocation(), new Location(F, 1));
        board.putPieceOnBoardAt(rook, rookStartPosition);
        chessGame.move(whitePlayer, move);
    }
    
    @Test
    public void testMovesAreSaved() throws IllegalMoveException {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(bishop, startLocation);
        Move move = new Move(startLocation, endLocation);
        chessGame.changePlayer();
        chessGame.move(blackPlayer, move);
        Move lastMove = chessGame.getTheLastMove();
        assertEquals(lastMove.getStart(), startLocation);
        assertEquals(lastMove.getEnd(), endLocation);
    }

    @Test
    public void getEmptyMove() {
        Move move = chessGame.getTheLastMove();
        assertTrue(move instanceof EmptyMove);
    }
    
    @Test
    public void CloneConstuctorMoveListCopyTest() {
        board.initialise();
        ChessGame clone = new ChessGame(chessGame);
        assertTrue(chessGame.getTheLastMove().equals(clone.getTheLastMove()));
    }

    /**
     * JIRA CG-33 Player can make a move out of turn
     * 
     * @throws IllegalMoveException
     */
    @Test(expected = IllegalMoveException.class)
    public void notPlayersTurn() throws IllegalMoveException {
        Move move = new Move(StartingSquare.BLACK_KING.getLocation(), new Location(E, 7));
        chessGame.move(blackPlayer, move);
        assertEquals(whitePlayer, chessGame.getCurrentPlayer());
    }
}
