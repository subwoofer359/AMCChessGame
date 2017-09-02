package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Helper class to handle requests to join one's own game 
 * @author Adrian Mclaughlin
 *
 */
class JoinOwnGameHelper {
	
    private JoinGameHelper helper;

	public JoinOwnGameHelper(JoinGameHelper helper) {
		this.helper = helper;
	}
	
	public void enterChessGame(ModelAndView mav, AbstractServerChessGame chessGame, Player player, long gameUID) {
		if (canPlayerEnterGame(chessGame)) {
			helper.setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUID);
        } else {
        	helper.setModelErrorMessage(chessGame, player, mav);
        }
	}
	
	private boolean canPlayerEnterGame(AbstractServerChessGame chessGame) {
        return inProgressState(chessGame);
    }
	
	public boolean inProgressState(AbstractServerChessGame chessGame) {
        return ServerGameStatus.IN_PROGRESS == chessGame.getCurrentStatus();
	}
}
