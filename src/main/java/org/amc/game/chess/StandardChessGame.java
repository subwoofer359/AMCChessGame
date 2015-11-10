package org.amc.game.chess;

import javax.persistence.Entity;


@Entity
public class StandardChessGame extends ChessGame {

	private static final long serialVersionUID = -3835551091946764392L;

	public StandardChessGame() {
		super();
		addGameRules();
	}
	
	public StandardChessGame(ChessBoard board, ChessGamePlayer playerWhite, ChessGamePlayer playerBlack) {
		super(board, playerWhite, playerBlack);
		addGameRules();
	}
	
	private void addGameRules() {
		addChessMoveRule(EnPassantRule.getInstance());
		addChessMoveRule(CastlingRule.getInstance());
		addChessMoveRule(PawnPromotionRule.getInstance());
	}
}
