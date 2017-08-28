package org.amc.game.chessserver;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.ComputerPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.computer.ComputerPlayerStrategy;
import org.amc.game.chess.computer.SimplePlayerStrategy;
import org.apache.log4j.Logger;

import javax.persistence.Entity;

@Entity
public class ComputerServerChessGame extends OneViewServerChessGame {
	
	private ComputerPlayerStrategy strategy = new SimplePlayerStrategy();

	private static final long serialVersionUID = -1118824178927416957L;
	
	private static final Logger LOGGER = Logger.getLogger(ComputerServerChessGame.class); 
	
	private Player computer = new ComputerPlayer();
	
	public ComputerServerChessGame() {
		super();
	}
	
	public ComputerServerChessGame(long gameUID, Player player) {
		super(gameUID, player);
	}

	@Override
	public void addOpponent(Player opponent) {
		LOGGER.error("The computer is the opponent, call to addOpponent ignored");
	}

	public void addOpponent() {
		super.addOpponent(computer);
	}

	@Override
	public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
		super.move(player, move);
		if(ServerGameStatus.IN_PROGRESS == getCurrentStatus()) {
			computerMakeMove();
		}
		
	}
	
	private void computerMakeMove()  throws IllegalMoveException {
		if(ComparePlayers.isSamePlayer(getChessGame().getCurrentPlayer(), getChessGame().getBlackPlayer())) {
			Move move = strategy.getNextMove(getChessGame());
			super.move(getChessGame().getCurrentPlayer(), move);
			if(GameState.PAWN_PROMOTION == getChessGame().getGameState()) {
				promotePawnTo(QueenPiece.getPiece(getChessGame().getCurrentPlayer().getColour()), move.getEnd());
			}
		}
	}

	@Override
	public void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException {
		super.promotePawnTo(piece, location);
		computerMakeMove();
	}
}
