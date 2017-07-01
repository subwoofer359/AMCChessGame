package org.amc.game.chess;

import static org.amc.game.chess.NoPlayer.NO_PLAYER;

import java.util.Collections;
import java.util.List;

public class NoChessGame extends ChessGame {

	public static final NoChessGame NO_CHESSGAME = new NoChessGame();
	
	private NoChessGame() {}
	
	@Override
	public int getId() {
		return -1;
	}

	@Override
	public ChessGamePlayer getOpposingPlayer(ChessGamePlayer player) {
		return NO_PLAYER;
	}

	@Override
	public void changePlayer() {
		// do nothing
	}

	@Override
	public boolean isGameOver() {
		return true;
	}

	@Override
	boolean doesAGameRuleApply(ChessGame game, Move move) {
		return false;
	}

	@Override
	public Move getTheLastMove() {
		return EmptyMove.EMPTY_MOVE;
	}

	@Override
	void setGameRules(List<ChessMoveRule> rules) {
		//do nothing
	}

	@Override
	public void setChessBoard(ChessBoard board) {
		//do nothing
	}

	@Override
	public ChessBoard getChessBoard() {
		// TODO Auto-generated method stub
		return EmptyChessBoard.EMPTY_CHESSBOARD;
	}

	@Override
	public GameState getGameState() {
		return GameState.NEW;
	}

	@Override
	void setGameState(GameState gameState) {
		//do nothing
	}

	@Override
	void setPromotionState() {
		// TODO Auto-generated method stub
		super.setPromotionState();
	}

	@Override
	void setRunningState() {
		//do nothing
	}

	@Override
	public ChessGamePlayer getCurrentPlayer() {
		return NO_PLAYER;
	}

	@Override
	public ChessGamePlayer getWhitePlayer() {
		return NO_PLAYER;
	}

	@Override
	public ChessGamePlayer getBlackPlayer() {
		return NO_PLAYER;
	}

	@Override
	List<ChessMoveRule> getChessMoveRules() {
		return Collections.emptyList();
	}

	@Override
	void addChessMoveRule(ChessMoveRule rule) {
		//do nothing
	}

	@Override
	public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
		// No nothing

	}

}
