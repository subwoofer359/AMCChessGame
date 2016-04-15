package org.amc.game.chessserver;


import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.observers.ObserverFactoryChain;

import javax.annotation.Resource;

public class ServerChessGameFactory {
	
    private ObserverFactoryChain observerFactoryChain;
    
    private static final String LOCAL_OBSERVERS = "JsonChessGameView|GameFinishedListener|GameStateListener|DatabaseUpdateListener";
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
		    serverChessGame = new TwoViewServerChessGame(uid, player);
		    observerStr = NETWORK_OBSERVERS;
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
