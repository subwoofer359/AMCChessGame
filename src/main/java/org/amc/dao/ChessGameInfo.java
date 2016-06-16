package org.amc.dao;

import org.amc.game.chessserver.AbstractServerChessGame;

/**
 * Represents a view of a ServerChessGame in the Database
 * Created by adrian on 15/06/16.
 */
public final class ChessGameInfo {

    private final long uid;
    private final AbstractServerChessGame.ServerGameStatus currentStatus;
    private final String player;
    private final String opponent;

    public ChessGameInfo(long uid, AbstractServerChessGame.ServerGameStatus currentStatus,
                         String player, String opponent) {
        this.uid = uid;
        this.currentStatus = currentStatus;
        this.player = player;
        this.opponent = opponent;
    }

    public long getUid() {
        return uid;
    }

    public AbstractServerChessGame.ServerGameStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getPlayer() {
        return player;
    }

    public String getOpponent() {
        return opponent;
    }

}
