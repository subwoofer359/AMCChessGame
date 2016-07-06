package org.amc.game.chessserver;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractServerChessGameTest {
	private ChessGameFactory factory;
	private AbstractServerChessGame ascgGame;
	private static final long GAME_UID = 2L;
	private static final long OTHER_GAME_UID = 22L;
	private ChessGameFixture cgFixture;

	@Before
	public void setUp() throws Exception {
		cgFixture = new ChessGameFixture();
		ascgGame = new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		
		ascgGame.addOpponent(cgFixture.getBlackPlayer());
		
        factory = new ChessGameFactory() {
			
			@Override
			public ChessGame getChessGame(ChessBoard board,
					ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
				return null;
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String toStringStr = ascgGame.toString();
		String expectedStr = "ServerChessGame[" + cgFixture.getWhitePlayer() + " vs " + 
				cgFixture.getBlackPlayer() + "]";
		assertEquals(expectedStr, toStringStr);
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
		AbstractServerChessGame otherAscgGame = new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		assertTrue(ascgGame.equals(otherAscgGame));
	}
	
	@Test
	public void equalsAbstractServerChessGameDifferentUid() {
		AbstractServerChessGame otherAscgGame = new TestServerChessGame(OTHER_GAME_UID, cgFixture.getWhitePlayer());
		assertFalse(ascgGame.equals(otherAscgGame));
	}
	
	@Test
	public void equalsAbstractServerChessGameDifferentId() throws Exception {
		AbstractServerChessGame otherAscgGame = new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		Field idField = AbstractServerChessGame.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.setInt(otherAscgGame, 1);
		
		assertFalse(ascgGame.equals(otherAscgGame));
	}
	
    @Test
    public void testDestroy() {
        AbstractServerChessGame game = new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
        game.destroy();
        
        assertNull(game.getOpponent());
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, game.getCurrentStatus());
    }
    
    @Test
    public void Constructor() {
        ServerChessGame scgGame = new ServerChessGame() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void addOpponent(Player opponent) {
                // do nothing
            }
        };
        
        assertEquals(0L, scgGame.getUid());
        assertNull(scgGame.getChessGame());
        assertNull(scgGame.getPlayer());
        assertNull(scgGame.getOpponent());
        assertEquals(ServerGameStatus.NEW, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
    }
    
    @Test
    public void ConstructorChessGame() {
        ChessGameFixture fixture = new ChessGameFixture();
        AbstractServerChessGame scgGame = new TestServerChessGame(GAME_UID, fixture.getChessGame());
        
        assertEquals(GAME_UID, scgGame.getUid());
        assertNotNull(scgGame.getChessGame());
        assertFalse(scgGame.getChessGame() == fixture.getChessGame());
        assertTrue(ComparePlayers.comparePlayers(fixture.getWhitePlayer(), scgGame.getPlayer()));
        assertTrue(ComparePlayers.comparePlayers(fixture.getBlackPlayer(), scgGame.getOpponent()));
        assertEquals(ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
        assertEquals(0, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
        
        ChessBoardUtilities.compareBoards(fixture.getChessGame().getChessBoard(), 
                        scgGame.getChessGame().getChessBoard());
    }
    
    @Test(expected=RuntimeException.class)
    public void getPlayerUnknownPlayer() {
        Player unknownPlayer = new HumanPlayer("Evil Ralph");
        ascgGame.getPlayer(unknownPlayer);
    }

    @Test
    public void getPlayerOpponentNotAddedTest() {
    	ascgGame =  new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
        assertEquals(null, ascgGame.getPlayer(cgFixture.getBlackPlayer()));
    }
    
    @Test
    public void getPlayerTest() {
        assertTrue(ComparePlayers.comparePlayers(cgFixture.getBlackPlayer(), 
        		ascgGame.getPlayer(cgFixture.getBlackPlayer())));
        assertTrue(ComparePlayers.comparePlayers(cgFixture.getWhitePlayer(), 
        		ascgGame.getPlayer(cgFixture.getWhitePlayer())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPlayerNullPlayer() {
        ascgGame.getPlayer(null);
    }

    @Test 
    public void hashCodeSameTest() {
		AbstractServerChessGame otherAscgGame = new TestServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
    	
    	int hashCode = ascgGame.hashCode();
    	int otherHashCode = otherAscgGame.hashCode();
    	assertEquals(hashCode, otherHashCode);
    }
    
    @Test 
    public void hashCodeTest() {
		AbstractServerChessGame otherAscgGame = new TestServerChessGame(OTHER_GAME_UID, cgFixture.getWhitePlayer());
    	
    	int hashCode = ascgGame.hashCode();
    	int otherHashCode = otherAscgGame.hashCode();
    	assertNotEquals(hashCode, otherHashCode);
    }
    
    @Test
    public void setServerChessGameFactory() {
    	ascgGame.setChessGameFactory(factory);
    	assertEquals(factory, ascgGame.chessGameFactory);
    }
	
	private static class TestServerChessGame extends AbstractServerChessGame {
		private static final long serialVersionUID = 1L;

		public TestServerChessGame(long gameUid, Player player) {
			super(gameUid, player);
		}
		
		public TestServerChessGame(long gameUid, ChessGame chessGame) {
			super(gameUid, chessGame);
		}
		
		@Override
		public void move(ChessGamePlayer player, Move move)
				throws IllegalMoveException {
			//Do nothing
		}
		
		@Override
        public void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException {
            // Do nothing
            
        }
		
		@Override
		public void addOpponent(Player opponent) {
			if(getChessGame() == null) {
				ChessBoard board = new ChessBoard();
				setChessGame(new ChessGame(board, getPlayer(), new RealChessGamePlayer(opponent, Colour.BLACK)));
			} else {
				System.out.println("Opponent already set");
			}
		}
	}
}
