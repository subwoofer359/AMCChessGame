package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.D;
import static org.amc.game.chess.ChessBoard.Coordinate.H;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChessGameMoveKingOutOfCheck {
    private static ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"),
                    Colour.WHITE);
    private static ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"),
                    Colour.BLACK);
    private ChessGamePlayer currentPlayer;
    private ChessPiece attackingPiece;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private ChessGame chessGame;

    public ChessGameMoveKingOutOfCheck(ChessGamePlayer currentPlayer, Move defendingChessPieceMove) {
        this.currentPlayer = currentPlayer;
        this.defendingChessPieceMove = defendingChessPieceMove;
    }

    @Before
    public void setUp() throws Exception {     
        ChessGameFactory factory = new StandardChessGameFactory();
        chessGame = factory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        attackingPiece = new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(attackingPiece, new Location(H, 4));
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] { { whitePlayer,
                new Move(StartingSquare.WHITE_KING.getLocation(), new Location(D, 2)) } });

    }

    @Test
    public void kingMovesOutOfCheckTest() throws IllegalMoveException {
        ChessPiece kingPiece = board.getPieceFromBoardAt(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(kingPiece, board.getPieceFromBoardAt(defendingChessPieceMove.getEnd()));
        assertNull(board.getPieceFromBoardAt(defendingChessPieceMove.getStart()));
    }
}