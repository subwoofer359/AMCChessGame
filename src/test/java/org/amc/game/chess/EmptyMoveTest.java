package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmptyMoveTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Move move=new EmptyMove();
        assertEquals(move.getAbsoluteDistanceX().intValue(), 0);
        assertEquals(move.getAbsoluteDistanceY().intValue(), 0);
        assertEquals(move.getDistanceX().intValue(), 0);
        assertEquals(move.getDistanceY().intValue(), 0);
        assertEquals(move.getStart().getLetter(),ChessBoard.Coordinate.A);
        assertEquals(move.getStart().getNumber(),1);
        assertEquals(move.getEnd().getLetter(),ChessBoard.Coordinate.A);
        assertEquals(move.getEnd().getNumber(),1);
        System.out.println(move.toString());
        
    }

}
