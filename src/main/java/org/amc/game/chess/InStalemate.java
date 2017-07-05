package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.util.Set;

public class InStalemate {

	private static final InStalemate INSTANCE = new InStalemate();

	/**
	 * Singleton
	 * @return {@link InStalemate} instance
	 */
	public static InStalemate getInstance() {
		return INSTANCE;
	}

	private final KingInCheck inCheck;

	private InStalemate() {
		inCheck = KingInCheck.getInstance();
	}

	/**
	 * 
	 * @param player {@link ChessGamePlayer}
	 * @param opponent {@link ChessGamePlayer}
	 * @param board {@link ChessBoard}
	 * @return true if there are no moves which doesn't result in check
	 */
	public boolean isStalemate(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board) {	
		if (inCheck.isPlayersKingInCheck(player, opponent, board)) {
			return false;
		}
		
		Game game = new Game(player, opponent, board);

		return !board.getListOfPieces(player).stream().filter(pieceLoc -> isPieceNotInStalemate(pieceLoc, game))
				.findFirst().isPresent();
	}

	/**
	 * 
	 * @param pieceLoc
	 * @param game
	 * @return true if the Piece can move and not place the player's king in
	 * check
	 */
	private boolean isPieceNotInStalemate(ChessPieceLocation pieceLoc, Game game) {
		return getMoveLocations(pieceLoc, game.board).stream()
				.filter(location -> willNotBeInCheck(game, new Move(pieceLoc.getLocation(), location))).findFirst()
				.isPresent();
	}

	/**
	 * 
	 * @param pieceLoc {@link ChessPieceLocation}
	 * @param board {@link ChessBoard}
	 * @return Set of {@link Location}s which the piece could move to
	 */
	private Set<Location> getMoveLocations(ChessPieceLocation pieceLoc, ChessBoard board) {
		return pieceLoc.getPiece().getPossibleMoveLocations(board, pieceLoc.getLocation());
	}

	/**
	 * 
	 * @param game {@link Game}
	 * @param move {@link Move}
	 * @return true if the move results in no check of the player's king
	 */
	private boolean willNotBeInCheck(Game game, Move move) {
		ChessBoard testBoard = new ChessBoard(game.board);
		testBoard.move(move);
		return !inCheck.isPlayersKingInCheck(game.player, game.opponent, testBoard);
	}

	/**
	 * Internal class to help pass parameters between functions
	 * @author Adrian Mclaughlin
	 *
	 */
	private class Game {
		ChessGamePlayer player;
		ChessGamePlayer opponent;
		ChessBoard board;
		
		public Game(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board) {
			this.player = player;
			this.opponent = opponent;
			this.board = board;
		}
	}
}
