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
        assertTrue(ComparePlayers.comparePlayers(playerA, playerB));
        assertTrue(ComparePlayers.comparePlayers(playerA, playerC));
        assertTrue(ComparePlayers.comparePlayers(playerB, playerC));
        assertFalse(ComparePlayers.comparePlayers(playerA, playerD));
        assertFalse(ComparePlayers.comparePlayers(playerA, playerE));
        assertFalse(ComparePlayers.comparePlayers(playerB, playerD));
        assertFalse(ComparePlayers.comparePlayers(playerB, playerE));
        assertFalse(ComparePlayers.comparePlayers(playerA, playerF));
        assertFalse(ComparePlayers.comparePlayers(playerA, playerG));
    }

}
