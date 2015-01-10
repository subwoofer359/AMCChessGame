package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Player;

public class ServerChessGame {
    public enum status{
        IN_PROGRESS,
        AWAITING_PLAYER,
        FINISHED
    }
    
    private ChessGame chessGame=null;
    private status currentStatus;
    private Player player;
    private Player opponent;

    public ServerChessGame(Player player) {
        this.player=player;
        this.player.setColour(Colour.WHITE);
        this.currentStatus=status.AWAITING_PLAYER;
    }
    
    public void addOpponent(Player player){
        if(!this.currentStatus.equals(status.FINISHED)){
            player.setColour(Colour.BLACK);
            this.opponent=player;
            chessGame=new ChessGame(new ChessBoard(),this.player,player);
            this.currentStatus=status.IN_PROGRESS;
        }
    }

    public final status getCurrentStatus() {
        return currentStatus;
    }

    public final Player getPlayer() {
        return player;
    }

    public final void setCurrentStatus(status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public final ChessGame getChessGame() {
        return chessGame;
    }
    
    public final Player getOpponent(){
        return opponent;
    }
}
