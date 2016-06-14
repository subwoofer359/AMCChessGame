package org.amc.game.chess;

/**
 * Represents a Knight Chess Piece
 * 
 * The knight moves to any of the closest squares that are not on the same rank,
 * file, or diagonal, thus the move forms an "L"-shape: two squares vertically
 * and one square horizontally, or two squares horizontally and one square
 * vertically
 * 
 * possible moves (x-2,y-1),(x-2,y+1),(x-1,y+2),(x+1,y+2),
 * (x+2,y-1),(x+2,y+1),(x-1,y-2),(x+1,y-2)
 *
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class KnightPiece extends SimplePiece {

    private static final KnightPiece KNIGHT_WHITE = new KnightPiece(Colour.WHITE);
    private static final KnightPiece KNIGHT_BLACK = new KnightPiece(Colour.BLACK);
    private static final KnightPiece KNIGHT_WHITE_MOVED = new KnightPiece(Colour.WHITE, true);
    private static final KnightPiece KNIGHT_BLACK_MOVED = new KnightPiece(Colour.BLACK, true);

    public static KnightPiece getKnightPiece(Colour colour) {
        if(Colour.WHITE == colour) {
            return KNIGHT_WHITE;
        } else {
            return KNIGHT_BLACK;
        }
    }

    private KnightPiece(Colour colour) {
        super(colour);
    }

    private KnightPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }
    /**
     * @see SimplePiece#isValidMove(ChessBoard, Move)
     */
    @Override
    boolean validMovement(Move move) {
        return move.getAbsoluteDistanceX() == 2 && move.getAbsoluteDistanceY() == 1
                        || move.getAbsoluteDistanceX() == 1 && move.getAbsoluteDistanceY() == 2;
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        return board.isEndSquareEmpty(move.getEnd()) ||
            isEndSquareOccupiedByOpponentsPiece(board, move);
    }
    
    /**
     * @see ChessPiece#canSlide()
     * 
     * @return false ChessPiece doesn't slide but moves over pieces to
     * it's end square
     */
    @Override
    public boolean canSlide() {
        return false;
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return KNIGHT_WHITE_MOVED;
        } else {
            return KNIGHT_BLACK_MOVED;
        }
    }
}
