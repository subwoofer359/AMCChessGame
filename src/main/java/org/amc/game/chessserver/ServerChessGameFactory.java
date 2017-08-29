package org.amc.game.chessserver;


import org.amc.game.chess.ComputerPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.observers.ObserverFactoryChain;

import javax.annotation.Resource;

public class ServerChessGameFactory {
	
	public enum GameType {
		LOCAL_GAME,
		NETWORK_GAME,
		COMPUTER_WHITE_GAME,
		COMPUTER_BLACK_GAME
	}
    
	/**
	 * Encapsulates the information on which observers to add to which
	 * type of ChessGame
	 * @author adrian
	 *
	 */
	enum ObserversConfig {
		LOCAL_OBSERVERS("JsonChessGameView|GameFinishedListener|OneViewGameStateListener"),
		NETWORK_OBSERVERS("JsonChessGameView|"
				+ "GameFinishedListener|GameStateListener|OfflineChessGameMessager");
		
		private String config;
		
		ObserversConfig(String config) {
			this.config = config;
		}
		
		public String getConfig() {
			return config;
		}
		
		public int getNumberOfObservers() {
			return config.split("\\|").length;
		}
	}
	
	private ObserverFactoryChain observerFactoryChain;
	
	public ServerChessGame getServerChessGame(GameType gameType, long uid, Player player) {
	    ServerChessGame serverChessGame;
	    String observerStr;
	    
		switch(gameType) {
		case LOCAL_GAME:
			serverChessGame = new OneViewServerChessGame(uid, player);
			observerStr = ObserversConfig.LOCAL_OBSERVERS.getConfig();
			break;
		case COMPUTER_BLACK_GAME:
			serverChessGame = new ComputerServerChessGame(uid, player);
			observerStr = ObserversConfig.LOCAL_OBSERVERS.getConfig();
			break;
		case COMPUTER_WHITE_GAME:
			serverChessGame = new ComputerServerChessGame(uid, new ComputerPlayer());
			observerStr = ObserversConfig.LOCAL_OBSERVERS.getConfig();
			break;
		case NETWORK_GAME:
		default:
		    serverChessGame = new TwoViewServerChessGame(uid, player);
		    observerStr = ObserversConfig.NETWORK_OBSERVERS.getConfig();
		    break;
		}
		
		observerFactoryChain.addObserver(observerStr, serverChessGame);
		serverChessGame.setChessGameFactory(new StandardChessGameFactory());
		return serverChessGame;
	}
	
	@Resource(name = "defaultObserverFactoryChain")
	public void setObserverFactoryChain(ObserverFactoryChain observerFactoryChain) {
	    this.observerFactoryChain = observerFactoryChain;
	}

}
