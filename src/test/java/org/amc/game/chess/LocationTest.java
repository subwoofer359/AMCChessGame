package org.amc.game.chess;

import static org.junit.Assert.*;
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
    
    @Test
    public void testString() {
        Location location = new Location("A1");
        assertTrue(location.getLetter().equals(Coordinate.A));
        assertEquals(location.getNumber(), 1);
    }
    
    @Test
    public void testLowerCaseString() {
        Location location = new Location("a1");
        assertTrue(location.getLetter().equals(Coordinate.A));
        assertEquals(location.getNumber(), 1);
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testStringTooLong() {
        new Location("A1B1");
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testStringWrongLetterCoordinate() {
        new Location("K1");
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testStringWrongNumberCoordinate() {
        new Location("A9");
    }

    
    public void testAsString() {
        Location location = new Location(Coordinate.G, 4);
        assertEquals(location.asString(), "G4");
    }
}
