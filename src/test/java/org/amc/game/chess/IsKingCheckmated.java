package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

public class IsKingCheckmated {
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessGame chessGame;
    private ChessBoardFactory chessBoardFactory;
    private PlayersKingCheckmateCondition checkmateCondtion;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer = new HumanPlayer("Robin", Colour.BLACK);
        board = new ChessBoard();
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        checkmateCondtion = new PlayersKingCheckmateCondition();
        chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    }

    @Test
    public void checkmateWithARookTest() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh5:kf5:ra1");
        Move rookMove = new Move(new Location(A, 1), new Location(H, 1));
        board.move(rookMove);

        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void checkmateFoolsMate() {
        board.initialise();
        board.removePieceOnBoardAt(new Location(D, 8));
        board.removePieceOnBoardAt(new Location(E, 7));
        board.removePieceOnBoardAt(new Location(F, 2));
        board.removePieceOnBoardAt(new Location(G, 2));
        board.putPieceOnBoardAt(new PawnPiece(Colour.BLACK), new Location(E, 5));
        board.putPieceOnBoardAt(new QueenPiece(Colour.BLACK), new Location(D, 8));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE), new Location(G, 4));
        board.putPieceOnBoardAt(new PawnPiece(Colour.WHITE), new Location(F, 3));
        Move queenMove = new Move(new Location(D, 8), new Location(H, 4));
        board.move(queenMove);

        assertTrue(chessGame.isCheckMate(whitePlayer, board));
    }

    @Test
    public void checkmateSupportMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ka6:kc6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        board.move(queenMove);
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void notCheckmateSupportMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Ka6:kc6:Bc7:qb1");
        Move move = new Move(new Location(B, 1), new Location(B, 6));
        board.move(move);
        assertFalse(chessGame.isCheckMate(blackPlayer, board));

    }

    @Test
    public void checkmateRightTriangleMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh6:kf6:qa1");
        Move queenMove = new Move(new Location(A, 1), new Location(H, 1));
        board.move(queenMove);

        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void checkmateOuterRowMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh3:kf2:qa5");
        Move queenMove = new Move(new Location(A, 5), new Location(H, 5));
        board.move(queenMove);
        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void notCheckmateOuterRowMate() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kh3:kf2:Ra4:qh4");
        board.move(new Move(new Location(H, 4), new Location(H, 5)));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void checkmateWithTwoBishops() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);

        assertTrue(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void checkmateWithTwoBishopsCanBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7:Rh7");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);

        assertFalse(chessGame.isCheckMate(blackPlayer, board));
        assertTrue(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer, whitePlayer, board));
    }

    @Test
    public void checkmateWithTwoBishopsCanBeCaptured() throws ParseException {
        board = chessBoardFactory.getChessBoard("Kb8:kb6:bb7:be7:Rh6");
        Move move = new Move(new Location(E, 7), new Location(D, 6));
        board.move(move);

        assertTrue(checkmateCondtion.canAttackingPieceBeCaptured(blackPlayer, whitePlayer, board));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void notCheckMate() {
        board.initialise();
        assertFalse(chessGame.isCheckMate(whitePlayer, board));
        assertFalse(chessGame.isCheckMate(blackPlayer, board));
    }

    @Test
    public void testCannotAttackingPieceBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("qh8:Kh3:kf2");
        Move move = new Move(new Location(H, 8), new Location(H, 5));
        board.move(move);
        assertFalse(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer, whitePlayer, board));
    }

    @Test
    public void testCanAttackingPieceBeBlocked() throws ParseException {
        board = chessBoardFactory.getChessBoard("qh8:Kh3:Ra4:kf2");
        Move move = new Move(new Location(H, 8), new Location(H, 5));
        board.move(move);
        assertTrue(checkmateCondtion.canAttackingPieceBeBlocked(blackPlayer, whitePlayer, board));
    }

}
