package org.amc.game.chess;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * Decorator for Player to be used by the Chess Game class
 * @author Adrian Mclaughlin
 *
 */
@Embeddable
@Entity
@Table(name="chessGamePlayers")
public class ChessGamePlayer implements Player, Serializable {

    private static final long serialVersionUID = 3040542012240005856L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable=false)
    private Colour colour;
    
    @OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
    		targetEntity=org.amc.game.chess.HumanPlayer.class)
    private Player player;
    
    @Version
    private int version;
    
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
