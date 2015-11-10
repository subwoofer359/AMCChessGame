package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.amc.game.chess.view.ChessBoardView;
import org.junit.Before;
import org.junit.Test;
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
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
        this.promotion = PawnPromotionRule.getInstance();
        
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { new PawnPiece(Colour.BLACK), new Move(new Location(A, 2), new Location(A, 1)) },
                { new PawnPiece(Colour.BLACK), new Move(new Location(H, 2), new Location(H, 1)) },
                { new PawnPiece(Colour.WHITE), new Move(new Location(A, 7), new Location(A, 8)) } });

    }

    @Test
    public void test() {
        board.putPieceOnBoardAt(piece, move.getStart());
        assertTrue(promotion.isRuleApplicable(chessGame,  move));
        promotion.applyRule(chessGame, move);
        ChessPiece piece = board.getPieceFromBoardAt(move.getEnd());
        assertNotNull(piece);
        assertFalse(piece instanceof PawnPiece);
    }

    @Test
    public void testChessGamePawnPromotion() throws IllegalMoveException {
        ChessGamePlayer whitePlayer = new ChessGamePlayer(new HumanPlayer("Test1"), Colour.WHITE);
        ChessGamePlayer blackPlayer = new ChessGamePlayer(new HumanPlayer("Test2"), Colour.BLACK);
        ChessGame chessGame = new StandardChessGameFactory().getChessGame(board, whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        new ChessBoardView(board);
        board.putPieceOnBoardAt(piece, move.getStart());
        if (piece.getColour().equals(Colour.BLACK)) {
            chessGame.changePlayer();
        }
        chessGame.move(piece.getColour().equals(Colour.WHITE) ? whitePlayer : blackPlayer, move);
        ChessPiece piece = board.getPieceFromBoardAt(move.getEnd());
        assertNotNull(piece);
        assertFalse(piece instanceof PawnPiece);
    }

}
