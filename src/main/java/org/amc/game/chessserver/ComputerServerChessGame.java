package org.amc.game.chessserver;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.ComputerPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
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
		LOGGER.info("***********Computer Game Move ********************");
		super.move(player, move);
		LOGGER.info("***********" +  getChessGame().getCurrentPlayer() + "***********");
		LOGGER.info("***********" +  computer + "***********");
		if(ComparePlayers.isSamePlayer(getChessGame().getCurrentPlayer(), getChessGame().getBlackPlayer())) {
			LOGGER.info("***********Computer Move Called********************");
			super.move(getChessGame().getCurrentPlayer(), strategy.getNextMove(getChessGame()));
		}
	}
	
	
}
