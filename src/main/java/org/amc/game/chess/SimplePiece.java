package org.amc.game.chess;

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
    public final Colour getColour() {
        return colour;
    }

    /**
     * @see ChessPiece#isValidMove(ChessBoard, Move) Using Template design
     *      pattern
     */
    @Override
    public final boolean isValidMove(ChessBoard board, Move move) {
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
     * Checks to see if the end Square is empty
     * @param board
     * @param move
     * @return true if empty
     */
    boolean isEndSquareEmpty(ChessBoard board, Move move){
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getName(),
                        endSquare.getNumber());
        return piece==null;
    }
    
    /**
     * Checks to see if the opposing Player's ChessPiece is in the end Square
     * @param board
     * @param move
     * @return true if there is an opposing Player's ChessPiece in the end Square
     */
    boolean isEndSquareOccupiedByOpponentsPiece(ChessBoard board, Move move) {
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getName(),
                        endSquare.getNumber());
        if (piece.getColour().equals(getColour())) {
            return false;
        } else {
            return true;
        }
    }
}
