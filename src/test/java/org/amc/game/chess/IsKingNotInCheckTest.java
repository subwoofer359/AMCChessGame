package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IsKingNotInCheckTest {

    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessPiece attackingChessPiece;
    private Location attackingChessPieceLocation;

    public IsKingNotInCheckTest(ChessPiece attackingChessPiece, Location attackingChessPieceLocation) {
        this.attackingChessPiece = attackingChessPiece;
        this.attackingChessPieceLocation = attackingChessPieceLocation;
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { BishopPiece.getBishopPiece(Colour.BLACK), new Location(A, 2) },
                { PawnPiece.getPawnPiece(Colour.BLACK), new Location(E, 2) },
                { BishopPiece.getBishopPiece(Colour.WHITE), new Location(A, 2) } });

    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
    }

    @Test
    public void testKingIsNotChecked() {
        ChessBoard board = new ChessBoard();
        ChessPiece whiteKing = KingPiece.getKingPiece(Colour.WHITE);
        ChessPiece blackKing = KingPiece.getKingPiece(Colour.BLACK);
        board.put(whiteKing, StartingSquare.WHITE_KING.getLocation());
        board.put(blackKing, StartingSquare.BLACK_KING.getLocation());
        board.put(attackingChessPiece, attackingChessPieceLocation);
        ChessGame chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        assertFalse(chessGame.isPlayersKingInCheck(whitePlayer, board));
    }
}
