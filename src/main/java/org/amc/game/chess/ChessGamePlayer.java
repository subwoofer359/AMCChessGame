package org.amc.game.chess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
    
    protected ChessGamePlayer() {
    }
    
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
}
