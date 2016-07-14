package org.amc.game.chessserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.amc.game.chess.ChessGameFixture;
import org.junit.Before;
import org.junit.Test;

public class AbstractServerChessGameEqualsTest {
	
	private static final long GAME_UID = 2L;
	private static final long OTHER_GAME_UID = 22L;
	private AbstractServerChessGame ascgGame;
	private ChessGameFixture cgFixture;
	
	@Before
	public void setUp() throws Exception {
		cgFixture = new ChessGameFixture();
		ascgGame = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		
		ascgGame.addOpponent(cgFixture.getBlackPlayer());
	}
	
	@Test
	public void equalsItself() {
		assertTrue(ascgGame.equals(ascgGame));
	}
	
	@Test
	public void equalsNull() {
		AbstractServerChessGame scg = null;
		assertFalse(ascgGame.equals(scg));
	}
	
	@Test
	public void equalsNotSameClass() {
		assertFalse(ascgGame.equals(new SignUpController()));
	}

	@Test
	public void equalAbstractServerChessGame() {
		AbstractServerChessGame otherAscgGame = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		assertTrue(ascgGame.equals(otherAscgGame));
	}
	
	@Test
	public void equalsAbstractServerChessGameDifferentUid() {
		AbstractServerChessGame otherAscgGame = new MockServerChessGame(OTHER_GAME_UID, cgFixture.getWhitePlayer());
		assertFalse(ascgGame.equals(otherAscgGame));
	}
	
	@Test
	public void equalsAbstractServerChessGameDifferentId() throws Exception {
		AbstractServerChessGame otherAscgGame = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		Field idField = AbstractServerChessGame.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.setInt(otherAscgGame, 1);
		
		assertFalse(ascgGame.equals(otherAscgGame));
	}
	
    @Test 
    public void hashCodeSameTest() {
		AbstractServerChessGame otherAscgGame = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
    	
    	int hashCode = ascgGame.hashCode();
    	int otherHashCode = otherAscgGame.hashCode();
    	assertEquals(hashCode, otherHashCode);
    }
    
    @Test 
    public void hashCodeNotSameTest() {
		AbstractServerChessGame otherAscgGame = new MockServerChessGame(OTHER_GAME_UID, cgFixture.getWhitePlayer());
    	
    	int hashCode = ascgGame.hashCode();
    	int otherHashCode = otherAscgGame.hashCode();
    	assertNotEquals(hashCode, otherHashCode);
    }
}
