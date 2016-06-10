package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

/**
 * Contains the starting square location of the main Chess Pieces
 * @author Adrian Mclaughlin
 *
 */
public enum StartingSquare {
    WHITE_KING(new Location(E,1)),
    WHITE_QUEEN(new Location(D,1)),
    WHITE_BISHOP_LEFT(new Location(C,1)),
    WHITE_BISHOP_RIGHT(new Location(F,1)),
    WHITE_KNIGHT_LEFT(new Location(B,1)),
    WHITE_KNIGHT_RIGHT(new Location(G,1)),
    WHITE_ROOK_LEFT(new Location(A,1)),
    WHITE_ROOK_RIGHT(new Location(H,1)),
    BLACK_KING(new Location(E,8)),
    BLACK_QUEEN(new Location(D,8)),
    BLACK_BISHOP_LEFT(new Location(C,8)),
    BLACK_BISHOP_RIGHT(new Location(F,8)),
    BLACK_KNIGHT_LEFT(new Location(B,8)),
    BLACK_KNIGHT_RIGHT(new Location(G,8)),
    BLACK_ROOK_LEFT(new Location(A,8)),
    BLACK_ROOK_RIGHT(new Location(H,8));
    
    
    private Location location;
    StartingSquare(Location location){
        this.location=location;
    }
    
    /**
     * Returns a Location object representing the starting square of the ChessPiece
     * @return Location
     */
    public Location getLocation(){
        return location;
    }
}