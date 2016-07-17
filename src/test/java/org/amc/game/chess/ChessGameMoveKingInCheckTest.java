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
public class ChessGameMoveKingInCheckTest {
    private static ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"),
                    Colour.WHITE);
    private static ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"),
                    Colour.BLACK);
    private ChessGamePlayer currentPlayer;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private ChessGame chessGame;

    public ChessGameMoveKingInCheckTest(ChessGamePlayer currentPlayer, Move defendingChessPieceMove) {
        this.currentPlayer = currentPlayer;
        this.defendingChessPieceMove = defendingChessPieceMove;
    }

    @Before
    public void setUp() throws Exception {
        board = new ChessBoard();
        chessGame = new ChessGame(board, whitePlayer, blackPlayer);
        ChessPiece attackingPiece = BishopPiece.getBishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(attackingPiece, new Location(H, 4));
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] { { whitePlayer,
                new Move(StartingSquare.WHITE_KING.getLocation(), new Location(F, 2)) } });

    }

    @Test(expected = IllegalMoveException.class)
    public void test() throws IllegalMoveException {
        ChessPiece piece = board.getPieceFromBoardAt(defendingChessPieceMove.getEnd());
        ChessPiece kingPiece = board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(piece, board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertEquals(kingPiece, board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureIntoCheck() throws IllegalMoveException {
        ChessPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        board.putPieceOnBoardAt(pawn, defendingChessPieceMove.getEnd());
        ChessPiece kingPiece = board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(pawn, board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertEquals(kingPiece, board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }
}
