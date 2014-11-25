package org.amc.game.chess;

import static org.junit.Assert.*;

import static org.amc.game.chess.ChessBoard.Coordinate.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IsKingInCheckTest {
    private Player whitePlayer;
    private Player blackPlayer;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ChessBoard board=new ChessBoard();
        ChessPiece whiteKing=new KingPiece(Colour.WHITE);
        board.putPieceOnBoardAt(whiteKing, StartingSquare.WHITE_KING.getLocation());
        ChessPiece blackBishop=new BishopPiece(Colour.BLACK);
        board.putPieceOnBoardAt(blackBishop, new Location(D,2));
        ChessGame chessGame=new ChessGame(board,whitePlayer, blackPlayer);
        assertTrue(chessGame.isPlayersKingInCheck(whitePlayer,board));
    }

}
