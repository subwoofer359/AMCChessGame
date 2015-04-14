package org.amc.game.chess.view;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.view.ChessBoardView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessBoardViewTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDisplayTheBoard() {
        ChessBoard board = new ChessBoard();
        board.initialise();
        ChessBoardView view = new ChessBoardView(board);
        view.displayTheBoard();
    }

}
