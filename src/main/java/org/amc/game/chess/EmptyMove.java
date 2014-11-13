package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

public class EmptyMove extends Move {

    public EmptyMove() {
        super(null, null);
    }

    @Override
    public Integer getDistanceX() {
        return 0;
    }

    @Override
    public Integer getDistanceY() {
        return 0;
    }

    @Override
    public Integer getAbsoluteDistanceX() {
        return 0;
    }

    @Override
    public Integer getAbsoluteDistanceY() {
        return 0;
    }

    @Override
    public String toString() {
        return "(Empty Move)";
    }

    @Override
    public Location getStart() {
        return new Location(Coordinate.A, 1);
    }

    @Override
    public Location getEnd() {
        // TODO Auto-generated method stub
        return new Location(Coordinate.A, 1);
    }

}