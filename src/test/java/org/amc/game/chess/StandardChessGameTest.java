package org.amc.game.chess;

import static org.junit.Assert.*;

import java.util.List;

import org.amc.game.chess.ChessGame.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StandardChessGameTest {
	private ChessGameFixture fixture;
	@Before
	public void setUp() throws Exception {
		fixture = new ChessGameFixture();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ChessGame game = new StandardChessGame();
		testForChessRules(game);
		checkChessGameUninitialised(game);
	}
	
	private void checkChessGameUninitialised(ChessGame game) {
		assertNull(game.getBlackPlayer());
		assertNull(game.getWhitePlayer());
		assertNotNull(game.getChessBoard());
		assertEquals(GameState.NEW, game.getGameState());
	}

	
	@Test
	public void constructorTest() {
		ChessGame game = new StandardChessGame(fixture.getBoard(), fixture.getWhitePlayer(), 
				fixture.getBlackPlayer());
		testForChessRules(game);
		checkChessGame(game);
		assertEquals(GameState.RUNNING, game.getGameState());
	}
	
	@Test 
	public void testCopyConstructor() {
		ChessGame game = new StandardChessGame(fixture.getBoard(), fixture.getWhitePlayer(), 
				fixture.getBlackPlayer());
		ChessGame copy = new ChessGame(game);
		testForChessRules(copy);
		checkChessGame(copy);
		assertEquals(GameState.RUNNING, copy.getGameState());
	}
	
	@Test 
	public void testCopyConstructorForEmptyConstructor() {
		ChessGame game = new StandardChessGame();
		ChessGame copy = new ChessGame(game);
		testForChessRules(copy);
		checkChessGameUninitialised(game);
		assertEquals(GameState.NEW, game.getGameState());
	}
	
	private void checkChessGame(ChessGame game) {
		assertEquals(fixture.getBlackPlayer(), game.getBlackPlayer());
		assertEquals(fixture.getWhitePlayer(), game.getWhitePlayer());
		assertNotNull(game.getChessBoard());
		assertFalse(game.getChessBoard() == fixture.getBoard());
		ChessBoardUtilities.compareBoards(fixture.getBoard(), game.getChessBoard());
	}
	
	private void testForChessRules(ChessGame game) {
		List<ChessMoveRule> rules = game.getChessMoveRules();
		assertFalse(rules.isEmpty());
		assertEquals(3, rules.size());
		assertTrue(rules.contains(EnPassantRule.getInstance()));
		assertTrue(rules.contains(CastlingRule.getInstance()));
		assertTrue(rules.contains(PawnPromotionRule.getInstance()));
	}
}
