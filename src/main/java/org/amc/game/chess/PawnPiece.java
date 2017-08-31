package org.amc.game.chess;

/**
 * Represents a Pawn in a game of Chess
 * <p>
 * The pawn may move forward to the unoccupied square immediately in front of it
 * on the same file without capturing, or on its first move it may advance two
 * squares along the same file without capturing or the pawn may capture an
 * opponent's piece on a square diagonally in front of it on an adjacent file,
 * by moving to that square.
 *
 * @author Adrian Mclaughlin
 * @see <a href="http://en.wikipedia.org/wiki/Chess">Chess</a>
 */
public final class PawnPiece extends SimplePiece {

    private static final PawnPiece PAWN_BLACK = new PawnPiece(Colour.BLACK);
    private static final PawnPiece PAWN_WHITE = new PawnPiece(Colour.WHITE);
    private static final PawnPiece PAWN_BLACK_MOVED = new PawnPiece(Colour.BLACK, true);
    private static final PawnPiece PAWN_WHITE_MOVED = new  PawnPiece(Colour.WHITE, true);

    private PawnPiece(Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    private PawnPiece(Colour colour) {
        this(colour, false);
    }

    public static PawnPiece getPiece(Colour colour) {
        if (colour == Colour.WHITE) {
            return PAWN_WHITE;
        } else {
            return PAWN_BLACK;
        }
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
        return isMovingForward(move) && move.getAbsoluteDistanceY() <= 2;
    }

    private boolean isMovingForwardOneSquareOnly(Move move) {
        return isMovingForward(move) && move.getAbsoluteDistanceY() == 1;
    }

    boolean isMovingForwardDiagonallyOneSquare(Move move) {
        return isMovingForward(move) && Move.isDiagonalMove(move)
                && move.getAbsoluteDistanceX() == 1;
    }

    /**
     * Is dependent on the colour of ChessPiece to know if it's moving forward
     * or not
     *
     * @param move Move to check to see if it's a forward move
     * @return true if the ChessPiece is moving forward
     */
    private boolean isMovingForward(Move move) {
        int moveInYDirection = move.getDistanceY();
        if (Colour.WHITE.equals(this.getColour())) {
            return moveInYDirection > 0;
        } else {
            return moveInYDirection < 0;
        }
    }

    /**
     * Checks the Pawn's move
     * Checks to see if the pawn's move is a move forward
     * if not it's assumes the move is diagonal and checks for capture
     * Diagonal move is checked in {@link PawnPiece#isValidMove(ChessBoard, Move)}
     *
     * @see SimplePiece#canMakeMove(ChessBoard, Move)
     */
    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        if (Move.isFileOrRankMove(move)) {
            if (move.getAbsoluteDistanceY() == 2) {
                return isEndSquareEmpty(board, move)
                        && canMoveTwoSquaresForward(board, move);
            } else {
                return isEndSquareEmpty(board, move);
            }
        } else {
            return canCapture(board, move);
        }
    }



    /**
     * @param board {@link ChessBoard}
     * @param move  {@link Move}
     * @return true if there are no other ChessPieces between the ChessPiece and
     * the end Square
     */
    private boolean canMoveTwoSquaresForward(ChessBoard board, Move move) {
        int x = move.getStart().getLetter().getIndex();
        int y = move.getStart().getNumber();
        x = x + (int) Math.signum(move.getDistanceX());
        y = y + (int) Math.signum(move.getDistanceY());
        return board.isEndSquareEmpty(x, y);
    }

    private boolean canCapture(ChessBoard board, Move move) {
        return isEndSquareNotEmpty(board, move) &&
                isEndSquareOccupiedByOpponentsPiece(board, move);
    }

    private boolean isEndSquareNotEmpty(ChessBoard board, Move move) {
        return !board.isEndSquareEmpty(move.getEnd());
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return PAWN_WHITE_MOVED;
        } else {
            return PAWN_BLACK_MOVED;
        }
    }
}