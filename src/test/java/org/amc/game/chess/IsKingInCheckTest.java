package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IsKingInCheckTest {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessPiece attackingChessPiece;
    private Location attackingChessPieceLocation;

    public IsKingInCheckTest(ChessPiece attackingChessPiece, Location attackingChessPieceLocation) {
        this.attackingChessPiece = attackingChessPiece;
        this.attackingChessPieceLocation = attackingChessPieceLocation;
    }

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] {
                { BishopPiece.getBishopPiece(Colour.BLACK), new Location(A, 5) },
                { PawnPiece.getPawnPiece(Colour.BLACK), new Location(F, 2) },
                { new KnightPiece(Colour.BLACK), new Location(D, 3) } });

    }

    @Test
    public void testKingIsChecked() {
        ChessBoard board = new ChessBoard();
        ChessPiece whiteKing = KingPiece.getKingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(whiteKing, StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(attackingChessPiece, attackingChessPieceLocation);
        ChessGame chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        assertTrue(chessGame.isPlayersKingInCheck(whitePlayer, board));
    }

}
