package org.amc.game.chessserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public abstract class DefaultSCGInitialiser extends SCGInitialiser {
	
    @Autowired
    private SimpMessagingTemplate template;
	
    private ServerChessGame chessGame;
    
	public DefaultSCGInitialiser() {
	}

	@Override
	public void init(ServerChessGame serverChessGame) {
		this.chessGame = serverChessGame;
		addView();
		addGameListener();
	}
	
    private void addView() {
        new JsonChessGameView(chessGame, template);
    }

    private void addGameListener() {
        new GameStateListener(chessGame, template);
        createGameFinishedListener().addServerChessGame(chessGame);
    }
}
