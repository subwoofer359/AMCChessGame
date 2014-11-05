package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestChessGame {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGame() {
        Player playerOne=new HumanPlayer("Stephen", Colour.BLACK);
        Player playerTwo=new HumanPlayer("Richard", Colour.WHITE);
        ChessBoard board=new ChessBoard();
        ChessBoardView view=new ChessBoardView(board);
        Controller controller=new ConsoleController(board, playerOne, playerTwo);
        
        ChessGame game=new ChessGame();
        game.setBoard(board);
        game.setController(controller);
        game.setView(view);
        
        fail("Not yet implemented");
    }

}
