package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayerEqualityTest {

    Player playerA = new HumanPlayer("Ted");
    Player playerB = new ChessGamePlayer(playerA, Colour.WHITE);
    Player playerC = new HumanPlayer("Ted");
    Player playerD = new HumanPlayer("Tom");
    Player playerE = new ChessGamePlayer(playerD, Colour.WHITE);
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assertEquals(playerA, playerB);
        assertEquals(playerA, playerC);
        assertEquals(playerB, playerC);
        assertNotEquals(playerA, playerD);
        assertNotEquals(playerA, playerE);
        assertNotEquals(playerB, playerD);
        assertNotEquals(playerB, playerE);
    }

}
