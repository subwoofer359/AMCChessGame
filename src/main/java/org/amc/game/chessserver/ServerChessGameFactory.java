package org.amc.game.chessserver;

import org.amc.game.chess.Player;

public class ServerChessGameFactory {
	
	public enum GameType {
		LOCAL_GAME,
		NETWORK_GAME;
	}
	public static ServerChessGame getServerChessGame(GameType gameType, long uid, Player player) {
		switch(gameType) {
		case LOCAL_GAME:
			return new OneViewServerChessGame(uid, player);
		case NETWORK_GAME:
		default:
			return new ServerChessGame(uid, player);
		}
	}

}
