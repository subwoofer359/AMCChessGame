package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveTest {
    private static final Location start = new Location(Coordinate.A, 1);
    private static final Location end = new Location(Coordinate.D, 2);
    private static final Move move = new Move(start, end);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToString() {
        String moveStr = move.toString();
        assertEquals(moveStr, "[(A,1)-->(D,2)]");
    }

    @Test
    public void testGetEnd() {
        assertEquals(end, move.getEnd());
    }

    @Test
    public void testGetStart() {
        assertEquals(start, move.getStart());
    }

    @Test
    public void testGetAbsoluteDistanceX() {
        assertEquals(3, move.getAbsoluteDistanceX().intValue());
    }

    @Test
    public void testGetAbsoluteDistanceY() {
        assertEquals(1, move.getAbsoluteDistanceY().intValue());
    }

    @Test
    public void testGetDistanceX() {
        assertEquals(3, move.getDistanceX().intValue());
    }

    @Test
    public void testGetDistanceY() {
        assertEquals(1, move.getDistanceY().intValue());
    }

    @Test
    public void testIsNotFileOrRankMove() {
        assertFalse(Move.isFileOrRankMove(move));
    }

    @Test
    public void testIsFileMove() {
        assertTrue(Move.isFileOrRankMove(new Move(start, new Location(Coordinate.H, 1))));
    }

    @Test
    public void testIsRankMove() {
        assertTrue(Move.isFileOrRankMove(new Move(start, new Location(Coordinate.A, 8))));
    }

    @Test
    public void testNoMoveIsNotFileOrRankMove() {
        assertFalse(Move.isFileOrRankMove(new Move(start, new Location(Coordinate.A, 1))));
    }

    @Test
    public void testIsNotDiagonalMove() {
        assertFalse(Move.isDiagonalMove(move));
    }

    @Test
    public void testIsDiagonalMove() {
        assertTrue(Move.isDiagonalMove(new Move(start, new Location(Coordinate.C, 3))));
    }

    
    @Test
    public void testMoveString() {
        Move move = new Move("A1-B2");
        assertTrue(move.getStart().getLetter().equals(Coordinate.A));
        assertEquals(move.getStart().getNumber(), 1);
        assertTrue(move.getEnd().getLetter().equals(Coordinate.B));
        assertEquals(move.getEnd().getNumber(), 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMoveStringMissingDash() {
        new Move("A1B2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveString() {
        new Move("A1K2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveString2() {
        new Move("\"\"-a2");
    }
    
    @Test
    public void testAsString() {
        final String moveStr = "A1-B2";
        Move move = new Move(moveStr);
        String storedMoveStr = move.asString();
        assertEquals(moveStr, storedMoveStr);
    }
}
