package org.amc.game.chess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


/**
 * Decorator for Player to be used by the Chess Game class
 * @author Adrian Mclaughlin
 *
 */
@Embeddable
public class ChessGamePlayer implements Player, Serializable {

    private static final long serialVersionUID = 3040542012240005856L;
    
    private Colour colour;
    
    @OneToOne(targetEntity=org.amc.game.chess.HumanPlayer.class)
    private Player player;
    
    protected ChessGamePlayer() {
        player = new HumanPlayer();
    }
    
    public ChessGamePlayer(Player player, Colour colour) {
        this.player = player;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)",player.getName(),getColour().toString());
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
    
    
}
