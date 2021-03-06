package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;
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
public class ChessGameMoveKingOutOfCheckTest {
    private static ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"),
                    Colour.WHITE);
    private static ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"),
                    Colour.BLACK);
    private ChessGamePlayer currentPlayer;
    private Move defendingChessPieceMove;
    private ChessBoard board;
    private AbstractChessGame chessGame;

    public ChessGameMoveKingOutOfCheckTest(ChessGamePlayer currentPlayer, Move defendingChessPieceMove) {
        this.currentPlayer = currentPlayer;
        this.defendingChessPieceMove = defendingChessPieceMove;
    }

    @Before
    public void setUp() throws Exception {     
        ChessGameFactory factory = new StandardChessGameFactory();
        chessGame = factory.getChessGame(new ChessBoard(), whitePlayer, blackPlayer);
        board = chessGame.getChessBoard();
        ChessPiece attackingPiece = BishopPiece.getPiece(Colour.BLACK);
        board.put(attackingPiece, new Location(H, 4));
        board.put(KingPiece.getPiece(Colour.WHITE),
                        StartingSquare.WHITE_KING.getLocation());
        board.put(KingPiece.getPiece(Colour.BLACK),
                        StartingSquare.BLACK_KING.getLocation());
    }

    @Parameters
    public static Collection<?> addedChessPieces() {

        return Arrays.asList(new Object[][] { { whitePlayer,
                new Move(StartingSquare.WHITE_KING.getLocation(), new Location(D, 2)) } });

    }

    @Test
    public void kingMovesOutOfCheckTest() throws IllegalMoveException {
        ChessPiece kingPiece = board.get(defendingChessPieceMove.getStart());
        chessGame.move(currentPlayer, defendingChessPieceMove);
        assertEquals(kingPiece.moved(), board.get(defendingChessPieceMove.getEnd()));
        assertEquals(NO_CHESSPIECE, 
        		board.get(defendingChessPieceMove.getStart()));
    }
}