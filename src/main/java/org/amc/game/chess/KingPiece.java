package org.amc.game.chess;

/**
 * Represents a King in the game of Chess
 * 
 * The king moves one square in any direction
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 * 
 */
public final class KingPiece extends SimplePiece {

    private static final KingPiece KING_WHITE = new KingPiece(Colour.WHITE);
    private static final KingPiece KING_BLACK = new KingPiece(Colour.BLACK);
    private static final KingPiece KING_WHITE_MOVED = new KingPiece(Colour.WHITE, true);
    private static final KingPiece KING_BLACK_MOVED = new KingPiece(Colour.BLACK, true);

    private KingPiece(Colour colour) {
        super(colour);
    }

    private KingPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    public static KingPiece getKingPiece(Colour colour) {
        if(Colour.WHITE == colour) {
            return KING_WHITE;
        } else {
            return KING_BLACK;
        }
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return isMoveOneSquareInAnyDirection(move);
    }
    
    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
       return board.isEndSquareEmpty(move.getEnd()) ||
               this.isEndSquareOccupiedByOpponentsPiece(board, move);
    }
    
    boolean isMoveOneSquareInAnyDirection(Move move){
        return move.getAbsoluteDistanceX() <= 1 && 
                        move.getAbsoluteDistanceY() <= 1;
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return KING_WHITE_MOVED;
        } else {
            return KING_BLACK_MOVED;
        }
    }
}
