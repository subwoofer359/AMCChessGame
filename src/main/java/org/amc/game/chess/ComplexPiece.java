package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;

import org.amc.game.chess.ChessBoard.Coordinate;

/**
 * Represents a piece which can't lift off the board to complete it's move and
 * also travels more than one square
 * 
 * @author Adrian Mclaughlin
 *
 */
abstract class ComplexPiece extends SimplePiece {

    ComplexPiece(Colour colour) {
        super(colour);
    }

    ComplexPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    boolean canMakeMove(ChessBoard board, Move move) {
        int distance = Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
        int x = move.getStart().getLetter().getIndex();
        int y = move.getStart().getNumber();

        for (int i = 0; i < distance; i++) {
            x = x + (int) Math.signum(move.getDistanceX());
            y = y + (int) Math.signum(move.getDistanceY());
            
            if (i < distance - 1 && !board.isEndSquareEmpty(x, y)) {
                return false;
            } else if (i == distance - 1 && !board.isEndSquareEmpty(x, y)) {
                ChessPiece piece = board.getPieceFromBoardAt(x, y);
                if (piece.getColour().equals(getColour())) {
                    return false;
                }
            }
        }
        return true;
    }

}
