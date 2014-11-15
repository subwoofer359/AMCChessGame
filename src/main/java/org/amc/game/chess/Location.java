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
    
    
}
