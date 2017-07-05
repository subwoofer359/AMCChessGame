package org.amc.game.chess;

import static org.junit.Assert.*;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.junit.Test;

import java.util.Arrays;

public class NoChessGameTest {
	
	private NoChessGame game = NoChessGame.NO_CHESSGAME;
	
	@Test
	public void moveTest() throws IllegalMoveException {
		game.move(null, new Move("a1-a2"));
		assertTrue("No moves in empty game", game.allGameMoves.isEmpty());
	}
	
	@Test
	public void isGameOverTest() {
		assertTrue("Game should always be over", game.isGameOver());
	}
	
	@Test
	public void getPlayersTest() {
		assertEquals(NoPlayer.NO_PLAYER, game.getBlackPlayer());
		assertEquals(NoPlayer.NO_PLAYER, game.getWhitePlayer());
		assertEquals(NoPlayer.NO_PLAYER, game.getCurrentPlayer());
		assertEquals(NoPlayer.NO_PLAYER, game.getOpposingPlayer(null));
	}
	
	@Test
	public void getSetGameStateTest() {
		game.setRunningState();
		assertEquals(GameState.NEW, game.getGameState());
		
		game.setPromotionState();
		assertEquals(GameState.NEW, game.getGameState());
		
		game.setGameState(GameState.BLACK_CHECKMATE);
		assertEquals(GameState.NEW, game.getGameState());
	}
	
	@Test
	public void getChessBoardTest() {
		assertEquals(EmptyChessBoard.EMPTY_CHESSBOARD, game.getChessBoard());
	}
	
	@Test
	public void setChessBoardTest() {
		ChessBoard board = new ChessBoard();
		game.setChessBoard(board);
		assertEquals(EmptyChessBoard.EMPTY_CHESSBOARD, game.getChessBoard());
	}
	
	@Test
	public void getSetChessRuleTest() {
		CastlingRule rule = CastlingRule.getInstance();
		
		game.addChessMoveRule(rule);
		
		assertTrue("Should no rules on a non game", game.getChessMoveRules().isEmpty());
	}
	
	@Test
	public void getIdTest() {
		assertEquals(-1, game.getId());
	}
	
	@Test
	public void getLastMoveTest() {
		assertEquals(EmptyMove.EMPTY_MOVE, game.getTheLastMove());
	}
	
	@Test
	public void doesAGameRuleApplyTest() {
		assertFalse("Should always return false", game.doesAGameRuleApply(game, new Move("a1-a2")));
	}
	
	@Test
	public void changePlayerTest() {
		game.changePlayer();
		assertEquals(NoPlayer.NO_PLAYER, game.getCurrentPlayer());
	}
	
	@Test
	public void addRulesTest() {
		game.setGameRules(Arrays.asList(CastlingRule.getInstance()));
		assertTrue("Should no rules on a non game", game.getChessMoveRules().isEmpty());
	}
	

}
