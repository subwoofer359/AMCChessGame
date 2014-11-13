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
public class KingPiece extends SimplePiece {

    public KingPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        return move.getAbsoluteDistanceX() <= 1 && 
                        move.getAbsoluteDistanceY() <= 1;
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        ChessPiece piece = board.getPieceFromBoardAt(move.getEnd().getLetter().getName(), 
                        move.getEnd().getNumber());
        if (piece == null) {
            return true;
        } else {
            return endSquareNotOccupiedByPlayersOwnPiece(piece);
        }
    }

    private boolean endSquareNotOccupiedByPlayersOwnPiece(ChessPiece piece) {
        return !piece.getColour().equals(getColour());
    }
}
