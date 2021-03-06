package org.amc.game.chess;

import static org.junit.Assert.*;

import java.util.List;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.Before;
import org.junit.Test;

public class StandardChessGameTest {
	private ChessGameFixture fixture;
	private ChessGameFactory chessGameFactory;
	private AbstractChessGame chessGame;
	
	@Before
	public void setUp() throws Exception {
		fixture = new ChessGameFixture();
		chessGame = fixture.getChessGame();
		
		chessGameFactory = new StandardChessGameFactory();
	}
	
	@Test
	public void constructorTest() {
		AbstractChessGame game = chessGameFactory.getChessGame(
		                chessGame.getChessBoard(), 
		                chessGame.getWhitePlayer(), 
		                chessGame.getBlackPlayer());
		testForChessRules(game);
		checkChessGame(game);
		assertEquals(GameState.RUNNING, game.getGameState());
	}
	
	@Test 
	public void testCopyConstructor() {
		AbstractChessGame game  = chessGameFactory.getChessGame(
				chessGame.getChessBoard(), 
				chessGame.getWhitePlayer(), 
				chessGame.getBlackPlayer());
		
		AbstractChessGame copy = new ChessGame(game);
		testForChessRules(copy);
		checkChessGame(copy);
		
		assertEquals(GameState.RUNNING, copy.getGameState());
	}
	
	private void checkChessGame(AbstractChessGame game) {
		assertEquals(chessGame.getBlackPlayer(), game.getBlackPlayer());
		assertEquals(chessGame.getWhitePlayer(), game.getWhitePlayer());
		assertNotNull(game.getChessBoard());
		assertFalse(game.getChessBoard() == chessGame.getChessBoard());
		
		ChessBoardUtil.compareBoards(chessGame.getChessBoard(), game.getChessBoard());
	}
	
	private void testForChessRules(AbstractChessGame game) {
		List<ChessMoveRule> rules = game.getChessMoveRules();
		
		assertFalse(rules.isEmpty());
		assertEquals(3, rules.size());
		assertTrue(rules.contains(EnPassantRule.getInstance()));
		assertTrue(rules.contains(CastlingRule.getInstance()));
		assertTrue(rules.contains(PawnPromotionRule.getInstance()));
	}
}
