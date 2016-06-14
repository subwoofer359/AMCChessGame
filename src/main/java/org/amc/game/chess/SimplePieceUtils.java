package org.amc.game.chess;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by adrian on 12/06/16.
 */
class SimplePieceUtils {

    Set<Location> getPossibleMoveLocations(ChessPiece piece, ChessBoard board, Location location){
        Set<Location> locations=new HashSet<>();
        for(ChessBoard.Coordinate coord: ChessBoard.Coordinate.values()){
            for(int i=1;i<=ChessBoard.BOARD_WIDTH;i++){
                Location moveLocation=new Location(coord,i);
                Move move=new Move(location,moveLocation);
                if(piece.isValidMove(board, move) && !moveLocation.equals(location)){
                    locations.add(moveLocation);
                }
            }
        }
        return locations;
    }

    boolean isEndSquareOccupiedByOpponentsPiece(Colour playerColour, ChessBoard board, Move move) {
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getIndex(),
                endSquare.getNumber());
        return !piece.getColour().equals(playerColour);

    }

    String toString(ChessPiece piece){
        return piece.getClass().getSimpleName() +
                '(' +
                piece.getColour() +
                ")";
    }

    int hashCode(ChessPiece piece) {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((piece.getColour() == null) ? 0 : piece.getColour().hashCode());
        result = prime * result + (piece.hasMoved() ? 1231 : 1237);
        return result;
    }

    boolean equals(ChessPiece piece, Object obj) {
        if (piece == obj)
            return true;
        if (piece == null)
            return false;
        if (piece.getClass() != obj.getClass())
            return false;
        SimplePiece other = (SimplePiece) obj;
        if (piece.getColour() != other.getColour())
            return false;
        if (piece.hasMoved() != other.hasMoved())
            return false;
        return true;
    }

    boolean isEndSquareEmpty(ChessBoard board, Move move) {
        return board.isEndSquareEmpty(move.getEnd());
    }
}
