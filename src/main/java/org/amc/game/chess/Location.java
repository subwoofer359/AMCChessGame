package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

/**
 * Represents a square location on a Chess board
 * 
 * @author Adrian Mclaughlin
 *
 */
public class Location implements Comparable<Location> {
    private Coordinate letter;
    private Integer number;

    public Location(Coordinate letter, Integer number) {
        this.letter = letter;
        this.number = number;
    }

    /**
     * @return Coordinate Letter
     */
    public Coordinate getLetter() {
        return this.letter;
    }

    /**
     * @return Integer number
     */
    public Integer getNumber() {
        return this.number;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(this.letter.name());
        sb.append(',');
        sb.append(this.number);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public int compareTo(Location o) {
        return this.getLetter().compareTo(o.getLetter());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((letter == null) ? 0 : letter.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (letter != other.letter)
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }
    
    
    
    
}
