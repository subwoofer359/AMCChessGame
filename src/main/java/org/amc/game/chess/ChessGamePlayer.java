package org.amc.game.chess;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Entity
@Table(name="chessGamePlayers")
public abstract class ChessGamePlayer implements Player, Serializable {

    private static final long serialVersionUID = 3040542012240005856L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable=false)
    private Colour colour;
    
    @Version
    private int version;
    
    
    /*
     * (Hack) Shadow variable not used
     * It's for JPQL queries
     * Subclasses define specific JPA behaviour
     * for the player field 
     */
    @OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
                    targetEntity=org.amc.game.chess.HumanPlayer.class)
    private Player player;
    
    protected ChessGamePlayer() {
    }
    
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
    
    
}
