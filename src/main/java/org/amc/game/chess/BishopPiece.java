package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Bishop in the game of chess
 * 
 * The bishop can move any number of squares diagonally.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public final class BishopPiece extends ComplexPiece {

    private static final BishopPiece BISHOP_WHITE = new BishopPiece(Colour.WHITE);
    private static final BishopPiece BISHOP_BLACK = new BishopPiece(Colour.BLACK);
    private static final BishopPiece BISHOP_WHITE_MOVED = new BishopPiece(Colour.WHITE, true);
    private static final BishopPiece BISHOP_BLACK_MOVED = new BishopPiece(Colour.BLACK, true);

    private BishopPiece(Colour colour) {
        super(colour);
    }

    private BishopPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    public static BishopPiece getPiece(Colour colour) {
        if (colour == Colour.WHITE) {
            return BISHOP_WHITE;
        } else {
            return BISHOP_BLACK;
        }
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return Move.isDiagonalMove(move);
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return BISHOP_WHITE_MOVED;
        } else {
            return BISHOP_BLACK_MOVED;
        }
    }

	@Override
	public Set<Location> getPossibleMoveLocations(ChessBoard board, Location location) {
		/*
		 *  Using eqn y-y1 = m(x-x1) where m = -1 and 1
		 *  therefore co = y1 - x1 and cp = y1 + x1
		 */
		int co = location.getNumber() - location.getLetter().getIndex();
		int cp = location.getNumber() + location.getLetter().getIndex();
		
		Set<Location> locations = new HashSet<>();
		
		for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
			int y1 = i + co;
			int y2 = cp - i; 
			addLocationIfValid(locations, location, board, i, y1);
			
			addLocationIfValid(locations, location, board, i, y2);
		}
		locations.remove(location);
		return locations;
	}
	
	private void addLocationIfValid(Set<Location> locations, Location location, ChessBoard board, int i, int y) {
		if((y > 0) && (y <= ChessBoard.BOARD_WIDTH)) {
			Location newLocation = new Location(Coordinate.getCoordinate(i), y);
			Move move = new Move(location, newLocation);
			if(isValidMove(board, move)) {
				locations.add(newLocation);
			}
		}
	}
}

