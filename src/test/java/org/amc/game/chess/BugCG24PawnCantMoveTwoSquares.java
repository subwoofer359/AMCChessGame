package org.amc.game.chess;

import org.junit.Before;
import org.junit.Test;

public class BugCG24PawnCantMoveTwoSquares {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
        final ChessBoard board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        game = new ChessGame(board, whitePlayer, blackPlayer);

    }
    @Test
    public void test() throws IllegalMoveException {
        Move firstMove = new Move("H2-H4");
        game.move(whitePlayer, firstMove);
        game.changePlayer();
        Move secondMoveThatFails = new Move("A7-A5");
        game.move(blackPlayer, secondMoveThatFails);
    }

}
