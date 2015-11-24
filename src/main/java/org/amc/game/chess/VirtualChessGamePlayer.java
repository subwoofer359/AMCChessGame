package org.amc.game.chess;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class VirtualChessGamePlayer extends ChessGamePlayer {

    private static final long serialVersionUID = -8578490703777299358L;
    
    @OneToOne(cascade=CascadeType.ALL,
                    targetEntity=org.amc.game.chess.HumanPlayer.class)
            private Player player = null;
            
            protected VirtualChessGamePlayer() {
            }
            
            public VirtualChessGamePlayer(Player player, Colour colour) {
                this.player = player;
                setColour(colour);
            }
            
            @Override
            public String getName() {
                return player.getName();
            }
            
            public int getId() {
                return player.getId();
            }
            
            @Override
            public void setId(int uid) {
               this.player.setId(uid); 
            }

            @Override
            public String getUserName() {
                return this.player.getUserName();
            }
            
            @Override
            public void setUserName(String userName) {
                this.player.setUserName(userName);
            }
            
            @Override
            public String toString(){
                return String.format("%s(%s)",player.getName(),getColour().toString());
            }
}
