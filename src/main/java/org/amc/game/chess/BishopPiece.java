package org.amc.game.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bishop in the game of chess
 * 
 * The bishop can move any number of squares diagonally.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class BishopPiece extends ComplexPiece {
    public BishopPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move);
    }

    @Override
    public List<Location> getAllPossibleMoves(Location location) {
       List<Location> possibleSquares=new ArrayList<>();
       return possibleSquares;
    }
    
    
}
