package org.amc.game.chess;

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
        ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("White Player"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        
        this.promotion = PawnPromotionRule.getInstance();
        this.chessGame = new ChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        this.board = this.chessGame.getChessBoard();
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { PawnPiece.getPawnPiece(Colour.WHITE), new Move("A2-A1") },
                { PawnPiece.getPawnPiece(Colour.WHITE), new Move("H2-H1") },
                { PawnPiece.getPawnPiece(Colour.BLACK), new Move("A7-A8") },
                //invalid move for a pawn
                { PawnPiece.getPawnPiece(Colour.BLACK), new Move("A7-A1") }});

    }

    @Test
    public void test() {
        board.put(piece, move.getStart());
        assertFalse("Move should not lead to promotion", promotion.isRuleApplicable(chessGame, move));
        promotion.applyRule(chessGame, move);
        assertEquals(piece, board.get(move.getStart()));
    }

}
