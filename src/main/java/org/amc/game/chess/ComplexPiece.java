package org.amc.game.chess;


/**
 * Represents a piece which can't lift off the board to complete it's move and
 * also travels more than one square
 * 
 * @author Adrian Mclaughlin
 *
 */
public abstract class ComplexPiece extends SimplePiece {

    public ComplexPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    boolean canMakeMove(ChessBoard board, Move move) {
        int distance = Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
        int positionX = move.getStart().getLetter().getIndex();
        int positionY = move.getStart().getNumber();

        for (int i = 0; i < distance; i++) {
            positionX = positionX + 1 * (int) Math.signum(move.getDistanceX());
            positionY = positionY + 1 * (int) Math.signum(move.getDistanceY());

            if (i < distance - 1 && board.getPieceFromBoardAt(positionX, positionY) != null) {
                return false;
            } else if (i == distance - 1 && board.getPieceFromBoardAt(positionX, positionY) != null) {
                ChessPiece piece = board.getPieceFromBoardAt(positionX, positionY);
                if (piece.getColour().equals(getColour())) {
                    return false;
                }
            }
        }
        return true;
    }

}
