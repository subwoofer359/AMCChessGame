package org.amc.game.chessserver;

import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.ComputerPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SetupChessBoard;
import org.amc.game.chess.VirtualChessGamePlayer;
import org.amc.game.chess.computer.ComputerPlayerStrategy;
import org.amc.game.chess.computer.SimplePlayerStrategy;

import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class ComputerServerChessGame extends ServerChessGame {

private static final long serialVersionUID = -1118824178927416957L;
	
	private static final Logger LOGGER = Logger.getLogger(ComputerServerChessGame.class);
	
	@Transient
	private ComputerPlayerStrategy strategy = new SimplePlayerStrategy();

	@Transient
	private Player computer = new ComputerPlayer();
	
	private static ChessGamePlayer wrapPlayer(Player player, Colour colour) {
		if(isComputerPlayer(player)) {
			return new VirtualChessGamePlayer(player, colour);
		} else {
			return new RealChessGamePlayer(player, colour);
		}
	}
	
	private static boolean isComputerPlayer(Player player) {
		return ComputerPlayer.class.equals(player.getType());
	}
	
	public ComputerServerChessGame() {
		super();
	}
	
	public ComputerServerChessGame(long gameUID, Player player) {
		super(gameUID, wrapPlayer(player, Colour.WHITE));
	}
	
	@Override
	public void addOpponent(Player opponent) {
		checkForNull(Player.class, opponent);
	    if(ComparePlayers.isSamePlayer(opponent, getPlayer())) {
	        LOGGER.error("Player can't join their own game");
	    } else if(isGameAwaitingPlayer()) {
	    	ChessGamePlayer gamePlayer = wrapPlayer(opponent, Colour.BLACK);
	        ChessBoard board = new ChessBoard();
	        
	        SetupChessBoard.setUpChessBoardToDefault(board);
	        setChessGame(getChessGameFactory().getChessGame(board, getPlayer(), gamePlayer));
	        setCurrentStatus(ServerGameStatus.IN_PROGRESS);
	    } else 
	    {
	        LOGGER.error("Player can't chess game already in process");
	    }
	    ifComputerPlayerMakeMove();
	}

	private void ifComputerPlayerMakeMove() {
		if(isComputerPlayer(getPlayer())) {
			try {
				computerMakeMove();
			} catch (IllegalMoveException ime) {
				LOGGER.error(ime);
			}
		}
	}
	
	public void addOpponent() {
		addOpponent(computer);
	}

	@Override
	public void move(ChessGamePlayer player, Move move) throws IllegalMoveException {
		super.move(player, move);
		if(ServerGameStatus.IN_PROGRESS == getCurrentStatus()) {
			computerMakeMove();
		}
		
	}
	
	private void computerMakeMove()  throws IllegalMoveException {
		if(isComputerPlayer(getChessGame().getCurrentPlayer())) {
			Move move = strategy.getNextMove(getChessGame());
			super.move(getChessGame().getCurrentPlayer(), move);
			if(GameState.PAWN_PROMOTION == getChessGame().getGameState()) {
				promotePawnTo(QueenPiece.getPiece(getChessGame().getCurrentPlayer().getColour()), move.getEnd());
			}
			computerMakeMove();
		}
		
	}

	@Override
	public void promotePawnTo(ChessPiece piece, Location location) throws IllegalMoveException {
		super.promotePawnTo(piece, location);
		computerMakeMove();
	}
}
