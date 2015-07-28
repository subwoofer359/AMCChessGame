package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;

import javax.annotation.Resource;

public class ServerChessGameFactory {
	
    private OfflineChessGameMessagerFactory offlineChessGameMessagerFactory;
    
	public enum GameType {
		LOCAL_GAME,
		NETWORK_GAME;
	}
	public ServerChessGame getServerChessGame(GameType gameType, long uid, Player player) {
	    ServerChessGame serverGame;
		switch(gameType) {
		case LOCAL_GAME:
			serverGame = new OneViewServerChessGame(uid, player);
			break;
		case NETWORK_GAME:
		default:
		    serverGame = new ServerChessGame(uid, player);
		    serverGame.attachObserver(createOfflineChessGameMessager());
		}
		return serverGame;
	}
	
	@Resource(name="offlineChessGameMessagerFactory")
    public void setOfflineChessGameMessagerFactory(OfflineChessGameMessagerFactory factory) {
        this.offlineChessGameMessagerFactory = factory;
    }
	
	private OfflineChessGameMessager createOfflineChessGameMessager() {
        return offlineChessGameMessagerFactory.createOfflineChessGameMessager();
    }

}
