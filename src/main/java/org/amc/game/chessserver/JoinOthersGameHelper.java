package org.amc.game.chessserver;

import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Helper class to handle requests to join others' game
 * @author Adrian Mclaughlin
 *
 */
class JoinOthersGameHelper {

	private JoinGameHelper helper;
	
	public JoinOthersGameHelper(JoinGameHelper helper) {
		this.helper = helper;
	}

	public void joinChessGame(ModelAndView mav, AbstractServerChessGame chessGame, Player player, long gameUUID) {
		if (canPlayerJoinGame(chessGame, player)) {
			if (inAwaitingPlayerState(chessGame)) {
				addPlayerToGame(chessGame, player);
				helper.saveGameToDatabase(chessGame);
			}
			helper.setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUUID);
		} else {
			helper.setModelErrorMessage(chessGame, player, mav);
		}
	}

	private boolean canPlayerJoinGame(AbstractServerChessGame chessGame, Player player) {
		return inAwaitingPlayerState(chessGame) || joiningCurrentGame(chessGame, player);
	}

	private boolean inAwaitingPlayerState(AbstractServerChessGame chessGame) {
		return ServerGameStatus.AWAITING_PLAYER.equals(chessGame.getCurrentStatus());
	}

	private void addPlayerToGame(AbstractServerChessGame chessGame, Player player) {
		chessGame.addOpponent(player);
	}

	private boolean joiningCurrentGame(AbstractServerChessGame chessGame, Player player) {
		return ServerGameStatus.IN_PROGRESS.equals(chessGame.getCurrentStatus())
				&& ComparePlayers.isSamePlayer(player, chessGame.getOpponent());
	}
}
