package org.amc.game.chess;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents an immutable move in the game of chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class Move implements Serializable {
    private static final long serialVersionUID = -7511044104434383204L;
    private final Location start;
    private final Location end;
    public static final EmptyMove EMPTY_MOVE = new EmptyMove();
    public static final char MOVE_SEPARATOR = '-';

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
     * @return String representation of the move
     */
    public String asString() {
        return start.asString() + MOVE_SEPARATOR + end.asString();
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

    private Object writeReplace() {
        return new MoveSerializedProxy(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    private static class MoveSerializedProxy implements Serializable {
        private static final long serialVersionUID = 3118097389735009346L;
        private transient Location start;
        private transient Location end;

        public MoveSerializedProxy(Move move) {
            this.start = move.start;
            this.end = move.end;
        }

        private Object readResolve() {
            return new Move(start, end);
        }

        private void readObject(ObjectInputStream stream) throws IOException,
                        ClassNotFoundException {
            stream.defaultReadObject();
            String moveStr = stream.readUTF();
            Move m = new Move(moveStr);
            this.start = m.start;
            this.end = m.end;

        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeUTF(new Move(start, end).asString());
        }
    }
}
