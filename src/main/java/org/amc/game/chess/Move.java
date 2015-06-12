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
    public static final EmptyMove EMPTY_MOVE = new EmptyMove();

    public Move(Location start, Location end) {
        this.start = start;
        this.end = end;
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
     * @return Integer can be postive or negative
     */
    public Integer getDistanceX() {
        return end.getLetter().getIndex() - start.getLetter().getIndex();
    }

    /**
     * The distance travelled in the Y direction
     * 
     * @return Integer can be postive or negative
     */
    public Integer getDistanceY() {
        return end.getNumber() - start.getNumber();
    }

    /**
     * The distance travelled in the X direction
     * 
     * @return Integer is postive
     */
    public Integer getAbsoluteDistanceX() {
        return Math.abs(getDistanceX());
    }

    /**
     * The distance travelled in the Y direction
     * 
     * @return Integer is postive
     */
    public Integer getAbsoluteDistanceY() {
        return Math.abs(getDistanceY());
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(this.start);
        sb.append("-->");
        sb.append(this.end);
        sb.append(']');

        return sb.toString();
    }

    /**
     * Returns true if the movement is along a rank or file
     * 
     * @param move
     * @return boolean
     */
    public static boolean isFileOrRankMove(Move move) {
        return move.getAbsoluteDistanceX() == 0 && move.getAbsoluteDistanceY() > 0
                        || move.getAbsoluteDistanceX() > 0 && move.getAbsoluteDistanceY() == 0;
    }

    /**
     * Returns true if the movement is along a diagonal
     * 
     * @param move
     * @return boolean
     */
    public static boolean isDiagonalMove(Move move) {
        return move.getAbsoluteDistanceX().equals(move.getAbsoluteDistanceY())
                        && move.getAbsoluteDistanceX() > 0;
    }
    
    
}
