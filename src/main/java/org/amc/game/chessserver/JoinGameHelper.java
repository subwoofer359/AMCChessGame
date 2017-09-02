package org.amc.game.chessserver;

import static org.amc.game.chessserver.ServerJoinChessGameController.*;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * Utility class for {@link ServerJoinChessGameController}
 * @author Adrian Mclaughlin
 *
 */
class JoinGameHelper {
    
    private static final Logger logger = Logger.getLogger(ServerJoinChessGameController.class);
	
    private SCGDAOInterface serverChessGameDAO;
    
    public JoinGameHelper(SCGDAOInterface serverChessGameDAO) {
    	this.serverChessGameDAO = serverChessGameDAO;
    }
    
    AbstractServerChessGame getServerChessGame(long gameUid) {
        try {
            return this.serverChessGameDAO.getServerChessGame(gameUid);
        } catch(DAOException de) {
            logger.error(de);
            return null;
        }   
    }
    
    boolean isPlayerJoiningOwnGame(AbstractServerChessGame chessGame, Player player) {
        return ComparePlayers.isSamePlayer(player, chessGame.getPlayer());
    }
    
	void setupModelForChessGameScreen(ModelAndView mav, ChessGamePlayer player, long gameUUID) {
		mav.getModel().put(ServerConstants.GAME_UUID, gameUUID);
		AbstractServerChessGame serverGame = getServerChessGame(gameUUID);
		mav.getModel().put(ServerConstants.GAME, serverGame);
		mav.getModel().put(ServerConstants.CHESSPLAYER, player);
		mav.setViewName(TWO_VIEW_CHESS_PAGE);
		logger.info(String.format("Chess Game(%d): has been started", gameUUID));
		if (serverGame instanceof TwoViewServerChessGame) {
			mav.getModel().put(ServerConstants.GAME_TYPE, ServerConstants.TWO_VIEW);
		} else {
			mav.getModel().put(ServerConstants.GAME_TYPE, ServerConstants.ONE_VIEW);
		}
	}

	void setModelErrorMessage(AbstractServerChessGame chessGame, Player player, ModelAndView mav) {
		if (isPlayerJoiningOwnGame(chessGame, player)) {
			setErrorPageAndMessage(mav, ERROR_GAME_HAS_NO_OPPONENT);
		} else {
			if (isGameInFinishedState(chessGame)) {
				setErrorPageAndMessage(mav, ERROR_GAMEOVER);
			} else {
				setErrorPageAndMessage(mav, ERROR_PLAYER_NOT_OPPONENT);
			}
		}
	}

	void setErrorPageAndMessage(ModelAndView mav, String errorMessage) {
		mav.setViewName(ERROR_FORWARD_PAGE);
		mav.getModel().put(ServerConstants.ERRORS, errorMessage);
		logger.error(errorMessage);
	}

	void saveGameToDatabase(AbstractServerChessGame chessGame) {
        try {
            serverChessGameDAO.saveServerChessGame(chessGame);
        } catch (DAOException de) {
            logger.error(de);
        }
    }
	
	boolean isGameInFinishedState(AbstractServerChessGame chessGame) {
		return ServerGameStatus.FINISHED == chessGame.getCurrentStatus();
	}
}
