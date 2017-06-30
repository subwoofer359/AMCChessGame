package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayerEqualityTest {

    private static final int uidOne = 1233303939;
    private static final int uidTwo = 1903939939;
    
    Player playerA = new HumanPlayer("Ted");
    Player playerB = new RealChessGamePlayer(playerA, Colour.WHITE);
    Player playerC = new HumanPlayer("Ted");
    Player playerD = new HumanPlayer("Tom");
    Player playerE = new RealChessGamePlayer(playerD, Colour.WHITE);
    Player playerF = new HumanPlayer("Ted");
    Player playerG = new HumanPlayer("Tom");
    
    @Before
    public void setUp() throws Exception {
        playerA.setId(uidOne);
        playerC.setId(uidOne);
        playerD.setId(uidTwo);
        playerF.setId(uidTwo);
        playerG.setId(uidOne);
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assertTrue(ComparePlayers.isSamePlayer(playerA, playerB));
        assertTrue(ComparePlayers.isSamePlayer(playerA, playerC));
        assertTrue(ComparePlayers.isSamePlayer(playerB, playerC));
        assertFalse(ComparePlayers.isSamePlayer(playerA, playerD));
        assertFalse(ComparePlayers.isSamePlayer(playerA, playerE));
        assertFalse(ComparePlayers.isSamePlayer(playerB, playerD));
        assertFalse(ComparePlayers.isSamePlayer(playerB, playerE));
        assertFalse(ComparePlayers.isSamePlayer(playerA, playerF));
        assertFalse(ComparePlayers.isSamePlayer(playerA, playerG));
    }

}
