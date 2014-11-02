package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Console;

import static org.mockito.Mockito.*;

public class ConsoleControllerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testChangePlayer() {
        Player player1=new HumanPlayer("Test1", Colour.BLACK);
        Player player2=new HumanPlayer("Test2", Colour.WHITE);
        ConsoleController controller=new ConsoleController(new ChessBoard(),player1, player2);
        assertEquals(player1, controller.getCurrentPlayer());
        controller.changePlayer();
        assertEquals(player2, controller.getCurrentPlayer());
        controller.changePlayer();
        assertEquals(player1, controller.getCurrentPlayer());
    }
    
    @Test
    public void testTakeTurn()throws InvalidMoveException{
        Player player1=new HumanPlayer("Test1", Colour.BLACK);
        Player player2=new HumanPlayer("Test2", Colour.WHITE);
        ConsoleController controller=new ConsoleController(new ChessBoard(),player1, player2);
        fail("To be completed");
    }
}
