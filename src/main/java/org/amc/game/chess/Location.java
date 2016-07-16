package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

/**
 * Represents an immutable square location on a Chess board
 * 
 * @author Adrian Mclaughlin
 *
 */
public final class Location implements Comparable<Location> {
    private final Coordinate letter;
    private final int number;

    /**
     * Creates a new Location
     * 
     * @param letter
     *            File
     * @param number
     *            Rank
     * @throws java.lang.IllegalArgumentException
     *             If number is greater than the allowed value set by
     *             <code>ChessBoard.BOARD_WIDTH</code>
     */
    public Location(Coordinate letter, int number) {
        this.letter = letter;
        checkNumber(number);
        this.number = number;
    }
    
    /**
     * Creates a new Location from a String
     * 
     * @param location String in the from "A1" or "a1"
     * @throws IllegalArgumentException if not a valid location string
     */
    public Location(String location) throws IllegalArgumentException {
        if(location.length() == 2) {
            this.letter = Coordinate.valueOf(location.substring(0, 1).toUpperCase());
            int tempNumber = Integer.parseInt(location.substring(1, 2));
            checkNumber(tempNumber);
            this.number = tempNumber;
        } else
        {
            throw new IllegalArgumentException("Not a valid Location");
        }
    }

    private void checkNumber(Integer number) {
        if (number > ChessBoard.BOARD_WIDTH) {
            throw new IllegalArgumentException(number + " is greater than "
                            + ChessBoard.BOARD_WIDTH + " which is not allowed");
        }
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
    public int getNumber() {
        return this.number;
    }

    /**
     * @return String representation of the Location 
     */
    public String asString() {
        return this.letter.name() + this.number;
    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "(" +
                this.letter.name() +
                ',' +
                this.number +
                ')';

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
        result = prime * result + number;
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
        return number == other.number;
    }

}
