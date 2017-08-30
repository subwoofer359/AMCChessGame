package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.ChessGameInfo;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import java.util.Map;
import java.util.UUID;

class GameControllerHelper {
    static final String CHESS_APPLICATION_PAGE = "ChessApplicationPage";
    static final String TWOVIEW_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String TWOVIEW_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";
    static final String CHESSGAME_PORTAL = "chessGamePortal";
	
	private SCGDAOInterface serverChessGameDAO;
	private ServerChessGameFactory scgFactory;
	
	private final Logger logger;
	
	GameControllerHelper(Logger logger) {
		this.logger = logger;
	}

	void setDAO(SCGDAOInterface serverChessGameDAO) {
		this.serverChessGameDAO = serverChessGameDAO;
	}
	
	SCGDAOInterface getDAO() {
		return this.serverChessGameDAO;
	}
	
	void setServerChessGameFactory(ServerChessGameFactory factory) {
		this.scgFactory = factory;
	}
	
	ServerChessGameFactory getServerChessGameFactory() {
		return this.scgFactory;
	}
	
	AbstractServerChessGame getServerChessGame(GameType gameType, long uid, Player player) {
		return this.scgFactory.getServerChessGame(gameType, uid, player);
	}
	
	Map<Long, ChessGameInfo> getGamesForPlayer(Player player) throws DAOException {
		return this.serverChessGameDAO.getGameInfoForPlayer(player);
	}
	
	long getNewGameUID() {
		return UUID.randomUUID().getMostSignificantBits();
	}

	void saveToDatabase(AbstractServerChessGame serverGame) {
		try {
			serverChessGameDAO.saveServerChessGame(serverGame);
		} catch (DAOException de) {
			logger.error(de);
		}
	}
	
    void setUpModel(Model model, long uuid, AbstractServerChessGame serverGame, Player player) {
        model.addAttribute(ServerConstants.GAME_TYPE, ServerConstants.ONE_VIEW);
    	model.addAttribute(ServerConstants.GAME_UUID, uuid);
        model.addAttribute(ServerConstants.GAME, serverGame);
        model.addAttribute(ServerConstants.CHESSPLAYER, serverGame.getPlayer(player));
    }
}
