package org.amc.game.chess;

/**
 * Contains the starting square location of the main Chess Pieces
 * @author Adrian Mclaughlin
 *
 */
public enum StartingSquare {
    WHITE_KING("E1"),
    WHITE_QUEEN("D1"),
    WHITE_BISHOP_LEFT("C1"),
    WHITE_BISHOP_RIGHT("F1"),
    WHITE_KNIGHT_LEFT("B1"),
    WHITE_KNIGHT_RIGHT("G1"),
    WHITE_ROOK_LEFT("A1"),
    WHITE_ROOK_RIGHT("H1"),
    BLACK_KING("E8"),
    BLACK_QUEEN("D8"),
    BLACK_BISHOP_LEFT("C8"),
    BLACK_BISHOP_RIGHT("F8"),
    BLACK_KNIGHT_LEFT("B8"),
    BLACK_KNIGHT_RIGHT("G8"),
    BLACK_ROOK_LEFT("A8"),
    BLACK_ROOK_RIGHT("H8");
    
    private Location location;
    
    StartingSquare(String locationStr) {
        this.location = new Location(locationStr);
    }
    
    /**
     * Returns a Location object representing the starting square of the ChessPiece
     * @return Location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * @return {@link Location} as a {@link String}
     */
    @Override
    public String toString() {
    	return location.asString();
    }    
}