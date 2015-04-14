package org.amc.game.chess;

/**
 * Decorator for Player to be used by the Chess Game class
 * @author Adrian Mclaughlin
 *
 */
public class ChessGamePlayer implements Player {

    private Colour colour;
    private Player player;
    
    public ChessGamePlayer(Player player, Colour colour) {
        this.player = player;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    public final Colour getColour() {
        return colour;
    }

    public final void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)%n",player.getName(),getColour().toString());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass().equals(Player.class.getClass()))
            return false;
        Player other = (Player) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }
}
