package org.amc.game.chess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BugCG24PawnCantMoveTwoSquares {
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private ChessBoard board;
    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Teddy"), Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(new HumanPlayer("Robin"), Colour.BLACK);
        board = new ChessBoard();
        SetupChessBoard.setUpChessBoardToDefault(board);
        game = new ChessGame(board, whitePlayer, blackPlayer);

    }

    @After
    public void tearDown() throws Exception {
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
