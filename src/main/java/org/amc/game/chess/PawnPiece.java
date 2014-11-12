package org.amc.game.chess;

/**
 * Represents a Pawn in a game of Chess
 * 
 * The pawn may move forward to the unoccupied square immediately in front of it
 * on the same file without capturing, or on its first move it may advance two
 * squares along the same file without capturing or the pawn may capture an
 * opponent's piece on a square diagonally in front of it on an adjacent file,
 * by moving to that square.
 * 
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 *
 */
public class PawnPiece extends SimplePiece {

    public PawnPiece(Colour colour) {
        super(colour);
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
    @Override
    boolean validMovement(Move move) {
        if (move.getAbsoluteDistanceX() == 0) {
            if (pieceHasNotMoved()) {
                return isMovingForwardOneOrTwoSquares(move);
            } else {
                return isMovingForwardOneSquareOnly(move);
            }
        } else {
            return isMovingForwardDiagonallyOneSquare(move);
        }
    }

    private boolean pieceHasNotMoved() {
        return !hasMoved();
    }

    private boolean isMovingForwardOneOrTwoSquares(Move move) {
        return isMovingForward(move) && move.getAbsoluteDistanceY() > 0
                        && move.getAbsoluteDistanceY() <= 2;
    }

    private boolean isMovingForwardOneSquareOnly(Move move) {
        return isMovingForward(move) && move.getAbsoluteDistanceY() == 1;
    }

    private boolean isMovingForwardDiagonallyOneSquare(Move move) {
        return isMovingForward(move) && Move.isDiagonalMove(move)
                        && move.getAbsoluteDistanceX() == 1;
    }

    /**
     * Is dependent on the colour of ChessPiece to know if it's moving forward
     * or not
     * 
     * @param move
     *            Move to check to see if it's a forward move
     * @return true if the ChessPiece is moving forward
     */
    private boolean isMovingForward(Move move) {
        int moveInYDirection = move.getDistanceY();
        if (this.getColour().equals(Colour.WHITE)) {
            return moveInYDirection < 0;
        } else {
            return moveInYDirection > 0;
        }
    }

    /**
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        if (Move.isUpOrDownMove(move)) {
            if (move.getAbsoluteDistanceY() == 2) {
                return endSquareIsEmpty(board, move) && canMoveTwoSquaresForward(board, move);
            } else {
                return endSquareIsEmpty(board, move);
            }
        } else if (Move.isDiagonalMove(move)) {
            return canCapture(board, move);
        } else {
            return false;
        }
    }

    private boolean endSquareIsEmpty(ChessBoard board, Move move) {
        return board.getPieceFromBoardAt(move.getEnd()) == null;
    }

    /**
     * @param board
     * @param move
     * @return true if there are no other ChessPieces between the ChessPiece and
     *         the end Square
     */
    private boolean canMoveTwoSquaresForward(ChessBoard board, Move move) {
        int positionX = move.getStart().getLetter().getName();
        int positionY = move.getStart().getNumber();
        positionX = positionX - 1 * (int) Math.signum(move.getDistanceX());
        positionY = positionY - 1 * (int) Math.signum(move.getDistanceY());
        return board.getPieceFromBoardAt(positionX, positionY) == null;
    }

    private boolean canCapture(ChessBoard board, Move move) {
        ChessPiece piece = board.getPieceFromBoardAt(move.getEnd());
        if (piece == null) {
            return false;
        } else {
            return endSquareOccupiedByEnemyPiece(piece);
        }
    }

    private boolean endSquareOccupiedByEnemyPiece(ChessPiece piece) {
        return !piece.getColour().equals(getColour());
    }
}
