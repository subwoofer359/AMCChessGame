package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PawnIsPromotedTest {

    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessPiece piece;
    private ChessMoveRule promotion;
    private Move move;
    private ChessGame chessGame;

    public PawnIsPromotedTest(ChessPiece piece, Move move) {
        this.piece = piece;
        this.move = move;
    }

    @Before
    public void setUp() throws Exception {
        chessGame = new StandardChessGameFactory().getChessGame(new ChessBoard(), 
                        whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        board.put(KingPiece.getPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.put(KingPiece.getPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        this.promotion = PawnPromotionRule.getInstance();
        
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { PawnPiece.getPiece(Colour.BLACK), new Move("A2-A1") },
                { PawnPiece.getPiece(Colour.BLACK), new Move("H2-H1") },
                { PawnPiece.getPiece(Colour.WHITE), new Move("A7-A8") } });

    }

    @Test
    public void test() {
        board.put(piece, move.getStart());
        assertTrue(promotion.isRuleApplicable(chessGame,  move));
        promotion.applyRule(chessGame, move);
        ChessPiece piece = board.get(move.getEnd());
        assertNotNull(piece);
        assertEquals(chessGame.getGameState(), GameState.PAWN_PROMOTION);
    }

    @Test
    public void testChessGamePawnPromotion() throws IllegalMoveException {
        ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Test1"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Test2"), Colour.BLACK);
        ChessGame chessGame = new StandardChessGameFactory().getChessGame(board, whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        new ChessBoardView(board);
        board.put(piece, move.getStart());
        if (piece.getColour().equals(Colour.BLACK)) {
            chessGame.changePlayer();
        }
        chessGame.move(piece.getColour().equals(Colour.WHITE) ? whitePlayer : blackPlayer, move);
        assertEquals(chessGame.getGameState(), GameState.PAWN_PROMOTION);
        
    }

}
