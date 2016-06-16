package org.amc.game.chess;

/**
 * Represents an immutable move in the game of chess
 * 
 * @author Adrian Mclaughlin
 *
 */

public class Move {
    private final Location start;
    private final Location end;
    public static final char LOCATION_SEPARATOR = '-';
    public static final char MOVE_SEPARATOR = ':';

    public Move(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    public Move(String moveString) throws IllegalArgumentException {
        if (moveString.length() == 5) {
            this.start = new Location(moveString.substring(0, 2));
            this.end = new Location(moveString.substring(3, 5));
        } else {
            throw new IllegalArgumentException("Not a valid move string");
        }
    }

    /**
     * @return the start Location
     */
    public Location getStart() {
        return start;
    }

    /**
     * @return the end Location
     */
    public Location getEnd() {
        return end;
    }

    /**
     * The distance travelled in the X direction
     * 
     * @return Integer can be positive or negative
     */
    public Integer getDistanceX() {
        return end.getLetter().getIndex() - start.getLetter().getIndex();
    }

    /**
     * The distance travelled in the Y direction
     * 
     * @return Integer can be positive or negative
     */
    public Integer getDistanceY() {
        return end.getNumber() - start.getNumber();
    }

    /**
     * The distance travelled in the X direction
     * 
     * @return Integer is positive
     */
    public Integer getAbsoluteDistanceX() {
        return Math.abs(getDistanceX());
    }

    /**
     * The distance travelled in the Y direction
     * 
     * @return Integer is positive
     */
    public Integer getAbsoluteDistanceY() {
        return Math.abs(getDistanceY());
    }

    /**
     * @return String representation of the move
     */
    public String asString() {
        return start.asString() + LOCATION_SEPARATOR + end.asString();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "[" +
                this.start +
                "-->" +
                this.end +
                ']';

    }

    /**
     * Returns true if the movement is along a rank or file
     * 
     * @param move {@link Move}
     * @return boolean {@link Boolean}
     */
    public static boolean isFileOrRankMove(Move move) {
        return move.getAbsoluteDistanceX() == 0 && move.getAbsoluteDistanceY() > 0
                        || move.getAbsoluteDistanceX() > 0 && move.getAbsoluteDistanceY() == 0;
    }

    /**
     * Returns true if the movement is along a diagonal
     * 
     * @param move {@link Move}
     * @return boolean boolean
     */
    public static boolean isDiagonalMove(Move move) {
        return move.getAbsoluteDistanceX().equals(move.getAbsoluteDistanceY())
                        && move.getAbsoluteDistanceX() > 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
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
        Move other = (Move) obj;
        return other.start.equals(start) && other.end.equals(end);
    }
}
