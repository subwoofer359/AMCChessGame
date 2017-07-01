package org.amc.game.chessserver;

import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.*;

public class AbstractServerChessGameTest {
	private ChessGameFactory factory;
	private AbstractServerChessGame ascgGame;
	private static final long GAME_UID = 2L;
	private static final int NO_OBSERVERS = 0;
	private ChessGameFixture cgFixture;

	@Before
	public void setUp() throws Exception {
		cgFixture = new ChessGameFixture();
		ascgGame = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
		
		ascgGame.addOpponent(cgFixture.getBlackPlayer());
		
        factory = new ChessGameFactory() {
			
			@Override
			public ChessGame getChessGame(ChessBoard board,
					ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
				return null;
			}
		};
	}

	@Test
	public void toStringTest() {
		String toStringStr = ascgGame.toString();
		String expectedStr = "ServerChessGame[" + cgFixture.getWhitePlayer() + " vs " + 
				cgFixture.getBlackPlayer() + "]";
		assertEquals(expectedStr, toStringStr);
	}
	
	
	
    @Test
    public void testDestroy() {
        AbstractServerChessGame game = new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
        game.destroy();
        
        assertEquals(NO_PLAYER, game.getOpponent());
        assertNull(game.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, game.getCurrentStatus());
    }
    
    @Test
    public void emptyConstructorTest() {
        ServerChessGame scgGame = new ServerChessGame() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void addOpponent(Player opponent) {
                // do nothing
            }
        };
        
        assertEquals(0L, scgGame.getUid());
        assertNull(scgGame.getChessGame());
        assertEquals(NO_PLAYER, scgGame.getPlayer());
        assertEquals(NO_PLAYER, scgGame.getOpponent());
        assertEquals(ServerGameStatus.NEW, scgGame.getCurrentStatus());
        assertEquals(NO_OBSERVERS, scgGame.getNoOfObservers());
        assertNull(scgGame.getChessGameFactory());
    }
    
    @Test
    public void constructorChessGameTest() {
        ChessGameFixture fixture = new ChessGameFixture();
        AbstractServerChessGame scgGame = new MockServerChessGame(GAME_UID, fixture.getChessGame());
        
        assertEquals(GAME_UID, scgGame.getUid());
        assertNotNull("ChessGame should not be null", scgGame.getChessGame());
        assertFalse(scgGame.getChessGame() == fixture.getChessGame());
        assertTrue(ComparePlayers.isSamePlayer(fixture.getWhitePlayer(), scgGame.getPlayer()));
        assertTrue(ComparePlayers.isSamePlayer(fixture.getBlackPlayer(), scgGame.getOpponent()));
        assertEquals(ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
        assertEquals(NO_OBSERVERS, scgGame.getNoOfObservers());
        assertNull("Should be no ChessGameFactory", scgGame.getChessGameFactory());
        
        ChessBoardUtilities.compareBoards(fixture.getChessGame().getChessBoard(), 
                        scgGame.getChessGame().getChessBoard());
    }
    
    @Test
    public void constructorPlayerTest() {
        ChessGameFixture fixture = new ChessGameFixture();
        AbstractServerChessGame scgGame = new MockServerChessGame(GAME_UID, fixture.getWhitePlayer());
        
        assertEquals(GAME_UID, scgGame.getUid());
        assertNull("ChessGame should be null", scgGame.getChessGame());
        assertTrue(ComparePlayers.isSamePlayer(fixture.getWhitePlayer(), scgGame.getPlayer()));
        assertEquals("Opponent should be null", NO_PLAYER, scgGame.getOpponent());
        assertEquals(ServerGameStatus.AWAITING_PLAYER, scgGame.getCurrentStatus());
        assertEquals(NO_OBSERVERS, scgGame.getNoOfObservers());
        assertNull("Should be no ChessGameFactory", scgGame.getChessGameFactory());
    }
    
    @Test(expected=RuntimeException.class)
    public void getPlayerUnknownPlayerTest() {
        Player unknownPlayer = new HumanPlayer("Evil Ralph");
        ascgGame.getPlayer(unknownPlayer);
    }

    @Test
    public void getPlayerOpponentNotAddedTest() {
    	ascgGame =  new MockServerChessGame(GAME_UID, cgFixture.getWhitePlayer());
    	assertEquals("Player shouldn't have been added", NO_PLAYER, ascgGame.getPlayer(cgFixture.getBlackPlayer()));
    }
    
    @Test
    public void getPlayerTest() {
        assertTrue(ComparePlayers.isSamePlayer(cgFixture.getBlackPlayer(), 
        		ascgGame.getPlayer(cgFixture.getBlackPlayer())));
        assertTrue(ComparePlayers.isSamePlayer(cgFixture.getWhitePlayer(), 
        		ascgGame.getPlayer(cgFixture.getWhitePlayer())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPlayerNullPlayer() {
        ascgGame.getPlayer(null);
    }
    
    @Test
    public void setServerChessGameFactoryTest() {
    	ascgGame.setChessGameFactory(factory);
    	assertEquals(factory, ascgGame.chessGameFactory);
    }

}
