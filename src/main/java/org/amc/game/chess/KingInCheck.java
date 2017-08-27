package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.util.List;

public final class KingInCheck {

	private static final KingInCheck instance = new KingInCheck();

	public static final KingInCheck getInstance() {
		return instance;
	}

	private KingInCheck() {}

	/**
	 * Checks to see if the opponent's ChessPieces are attacking the Player's
	 * king
	 * 
	 * @param player
	 *            Player who King might be under attack
	 * @param board
	 *            ChessBoard current ChessBoard
	 * @return Boolean true if the opponent can capture the Player's king on the
	 *         next turn
	 */
	public boolean isPlayersKingInCheck(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board) {
		Location playersKingLocation = board.getKingLocation(player);
		List<ChessPieceLocation> listOfEnemysPieces = board.getListOfPieces(opponent);
		for (ChessPieceLocation pieceLocation : listOfEnemysPieces) {
			Move move = new Move(pieceLocation.getLocation(), playersKingLocation);
			if (pieceLocation.getPiece().isValidMove(board, move)) {
				return true;
			}
		}
		return false;
	}
}
