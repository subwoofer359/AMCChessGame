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
    boolean validMovement(Move move) {
        int distanceX = move.getAbsoluteDistanceX();
        int distanceY = move.getAbsoluteDistanceY();
        return distanceX <= 1 && distanceY <= 1;
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    boolean canMakeMove(ChessBoard board, Move move) {
        ChessPiece piece = board.getPieceFromBoardAt(move.getEnd().getLetter().getName(), move
                        .getEnd().getNumber());
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
