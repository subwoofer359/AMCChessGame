package org.amc.game.chessserver;

import org.amc.game.chessserver.observers.GameFinishedListener;

public abstract class SCGInitialiser {

	public abstract void init(ServerChessGame serverChessGame);
	
	protected abstract GameFinishedListener createGameFinishedListener();

}