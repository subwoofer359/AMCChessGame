package org.amc.game.chessserver;

public abstract class SCGInitialiser {

	public abstract void init(ServerChessGame serverChessGame);
	
	protected abstract GameFinishedListener createGameFinishedListener();

}