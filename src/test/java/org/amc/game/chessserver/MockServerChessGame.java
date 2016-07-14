package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;

public class MockServerChessGame extends AbstractServerChessGame {
	private static final long serialVersionUID = 1L;

	public MockServerChessGame(long gameUid, Player player) {
		super(gameUid, player);
	}

	public MockServerChessGame(long gameUid, ChessGame chessGame) {
		super(gameUid, chessGame);
	}

	@Override
	public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
		// Do nothing
	}

	@Override
	public void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException {
		// Do nothing

	}

	@Override
	public void addOpponent(Player opponent) {
		if (getChessGame() == null) {
			ChessBoard board = new ChessBoard();
			setChessGame(new ChessGame(board, getPlayer(), new RealChessGamePlayer(opponent, Colour.BLACK)));
		} else {
			System.out.println("Opponent already set");
		}
	}
}
