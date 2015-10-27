package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;

import javax.annotation.Resource;

public class ServerChessGameFactory {
	
    private OfflineChessGameMessagerFactory offlineChessGameMessagerFactory;
    private ObserverFactoryChain observerFactoryChain;
    
    private static final String LOCAL_OBSERVERS = "JsonChessGameView|GameFinishedListener|GameStateListener";
    private static final String NETWORK_OBSERVERS = LOCAL_OBSERVERS + "|OfflineChessGameMessager";
    
	public enum GameType {
		LOCAL_GAME,
		NETWORK_GAME;
	}
	public ServerChessGame getServerChessGame(GameType gameType, long uid, Player player) {
	    ServerChessGame serverChessGame;
	    String observerStr = "";
	    
		switch(gameType) {
		case LOCAL_GAME:
			serverChessGame = new OneViewServerChessGame(uid, player);
			observerStr = LOCAL_OBSERVERS;
			break;
		case NETWORK_GAME:
		default:
		    serverChessGame = new ServerChessGame(uid, player);
		    serverChessGame.attachObserver(createOfflineChessGameMessager());
		    observerStr = NETWORK_OBSERVERS;
		    break;
		}
		
		observerFactoryChain.addObserver(observerStr, serverChessGame);
		return serverChessGame;
	}
	
	

	
	@Resource(name="offlineChessGameMessagerFactory")
    public void setOfflineChessGameMessagerFactory(OfflineChessGameMessagerFactory factory) {
        this.offlineChessGameMessagerFactory = factory;
    }
	
	private OfflineChessGameMessager createOfflineChessGameMessager() {
        return offlineChessGameMessagerFactory.createOfflineChessGameMessager();
    }
	
	@Resource(name = "defaultObserverFactoryChain")
	public void setObserverFactoryChain(ObserverFactoryChain observerFactoryChain) {
	    this.observerFactoryChain = observerFactoryChain;
	}

}
