package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test() {
        new Location(Coordinate.D, 9);
    }

}
