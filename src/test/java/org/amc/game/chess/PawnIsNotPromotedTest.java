package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PawnIsNotPromotedTest {

    private ChessGame chessGame;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessPiece piece;
    private ChessMoveRule promotion;
    private Move move;

    public PawnIsNotPromotedTest(ChessPiece piece, Move move) {
        this.piece = piece;
        this.move = move;
    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new ChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        board = new ChessBoard();
        this.promotion = PawnPromotionRule.getInstance();
        this.chessGame = new ChessGame(board, whitePlayer, blackPlayer);
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { new PawnPiece(Colour.WHITE), new Move(new Location(A, 2), new Location(A, 1)) },
                { new PawnPiece(Colour.WHITE), new Move(new Location(H, 2), new Location(H, 1)) },
                { new PawnPiece(Colour.BLACK), new Move(new Location(A, 7), new Location(A, 8)) } });

    }

    @Test
    public void test() {
        board.putPieceOnBoardAt(piece, move.getStart());
        assertFalse(promotion.isRuleApplicable(chessGame, move));
        promotion.applyRule(chessGame, move);
        assertEquals(piece, board.getPieceFromBoardAt(move.getStart()));
    }

}
