package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * Shared implementation between ChessPieces
 * 
 * @author Adrian Mclaughlin
 *
 */
abstract class SimplePiece implements ChessPiece {
    private Colour colour;
    private Boolean hasMoved = false;

    public SimplePiece(Colour colour) {
        this.colour = colour;
    }

    /**
     * @see ChessPiece#getColour()
     */
    @Override
    public Colour getColour() {
        return colour;
    }

    /**
     * @see ChessPiece#isValidMove(ChessBoard, Move) Using Template design
     *      pattern
     * Should be final not to be overridden by subclasses
     */
    @Override
    public boolean isValidMove(ChessBoard board, Move move) {
        if (validMovement(move)) {
            return canMakeMove(board, move);
        } else {
            return false;
        }
    }

    /**
     * Checks to see if the move is a valid move for the chess piece
     * 
     * @param move
     * @return boolean true if the move is allowed
     */
    abstract boolean validMovement(Move move);

    /**
     * Checks to see if the move can be completed, if there are no ChessPieces
     * in the way of the move and if there is a ChessPiece in the end Square of
     * the move
     * 
     * @param board
     * @param move
     *            Move can be a move or a capture
     * @return true if the move can be made
     */
    abstract boolean canMakeMove(ChessBoard board, Move move);

    /**
     * @see ChessPiece#moved()
     */
    @Override
    public void moved() {
        this.hasMoved = true;

    }

    /**
     * @see ChessPiece#hasMoved()
     */
    @Override
    public boolean hasMoved() {
        return this.hasMoved;
    }
    

    
    /**
     * Checks to see if the opposing Player's ChessPiece is in the end Square
     * @param board
     * @param move
     * @return true if there is an opposing Player's ChessPiece in the end Square
     */
    boolean isEndSquareOccupiedByOpponentsPiece(ChessBoard board, Move move) {
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getIndex(),
                        endSquare.getNumber());
        if (piece.getColour().equals(getColour())) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Find all possible move locations. If the piece isn't on the board the method will 
     * add the @param Location to the Set which isn't desirable
     * 
     * @param board ChessBoard on which the ChessPiece is on
     * @param location Location of the ChessPiece
     * @return HashSet of Locations
     */
    public Set<Location> getPossibleMoveLocations(ChessBoard board,Location location){
        Set<Location> locations=new HashSet<>();
        for(Coordinate coord:Coordinate.values()){
            for(int i=1;i<=board.BOARD_WIDTH;i++){
                Location moveLocation=new Location(coord,i);
                Move move=new Move(location,moveLocation);
                if(isValidMove(board, move)){
                    locations.add(moveLocation);
                }
            }
        }
        return locations;
    }
}
