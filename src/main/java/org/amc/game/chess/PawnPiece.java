package org.amc.game.chess;

import java.util.HashSet;
import java.util.Set;

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
public class PawnPiece implements ChessPiece {

    private static final PawnPiece PAWN_BLACK = new PawnPiece(Colour.BLACK);
    private static final PawnPiece PAWN_WHITE = new PawnPiece(Colour.WHITE);
    private static final PawnPiece PAWN_BLACK_MOVED = new PawnPiece(Colour.BLACK, true);
    private static final PawnPiece PAWN_WHITE_MOVED = new  PawnPiece(Colour.WHITE, true);


    private final Colour colour;
    private final boolean hasMoved;


    private PawnPiece(Colour colour, boolean hasMoved) {
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    private PawnPiece(Colour colour) {
        this(colour, false);
    }

    public static PawnPiece getPawnPiece(Colour colour) {
        if (colour == Colour.WHITE) {
            return PAWN_WHITE;
        } else {
            return PAWN_BLACK;
        }
    }

    /**
     * @see SimplePiece#validMovement(Move)
     */
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

    private boolean isMovingForwardDiagonallyOneSquare(Move move) {
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
    boolean canMakeMove(ChessBoard board, Move move) {
        if (Move.isFileOrRankMove(move)) {
            if (move.getAbsoluteDistanceY() == 2) {
                return endSquareIsEmpty(board, move) && canMoveTwoSquaresForward(board, move);
            } else {
                return endSquareIsEmpty(board, move);
            }
        } else {
            return canCapture(board, move);
        }
    }

    private boolean endSquareIsEmpty(ChessBoard board, Move move) {
        return board.isEndSquareEmpty(move.getEnd());
    }

    /**
     * @param board {@link ChessBoard}
     * @param move  {@link Move}
     * @return true if there are no other ChessPieces between the ChessPiece and
     * the end Square
     */
    private boolean canMoveTwoSquaresForward(ChessBoard board, Move move) {
        int positionX = move.getStart().getLetter().getIndex();
        int positionY = move.getStart().getNumber();
        positionX = positionX + (int) Math.signum(move.getDistanceX());
        positionY = positionY + (int) Math.signum(move.getDistanceY());
        return board.getPieceFromBoardAt(positionX, positionY) == null;
    }

    private boolean canCapture(ChessBoard board, Move move) {
        return isEndSquareNotEmpty(board, move) &&
                isEndSquareOccupiedByOpponentsPiece(board, move);
    }

    private boolean isEndSquareNotEmpty(ChessBoard board, Move move) {
        return !board.isEndSquareEmpty(move.getEnd());
    }

    /**
     * @see ChessPiece#canSlide()
     */
    @Override
    public boolean canSlide() {
        return true;
    }

    /**
     * @see ChessPiece#copy()
     */
    @Override
    public ChessPiece copy() {
        PawnPiece piece = getPawnPiece(this.getColour());
        if (this.hasMoved()) {
            return piece.moved();
        }
        return piece;
    }

    @Override
    public ChessPiece moved() {
        if(Colour.WHITE == getColour()) {
            return PAWN_WHITE_MOVED;
        } else {
            return PAWN_BLACK_MOVED;
        }
    }

    @Override
    public boolean isValidMove(ChessBoard board, Move move) {
        return validMovement(move) &&
                canMakeMove(board, move);
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Checks to see if the opposing Player's ChessPiece is in the end Square
     *
     * @param board
     * @param move
     * @return true if there is an opposing Player's ChessPiece in the end Square
     */
    boolean isEndSquareOccupiedByOpponentsPiece(ChessBoard board, Move move) {
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getIndex(),
                endSquare.getNumber());
        return !piece.getColour().equals(getColour());

    }

    @Override
    public Set<Location> getPossibleMoveLocations(ChessBoard board, Location location) {
        Set<Location> locations = new HashSet<>();
        for (ChessBoard.Coordinate coord : ChessBoard.Coordinate.values()) {
            for (int i = 1; i <= ChessBoard.BOARD_WIDTH; i++) {
                Location moveLocation = new Location(coord, i);
                Move move = new Move(location, moveLocation);
                if (isValidMove(board, move) && !moveLocation.equals(location)) {
                    locations.add(moveLocation);
                }
            }
        }
        return locations;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PawnPiece other = (PawnPiece) obj;
        if (colour != other.colour)
            return false;
        if (hasMoved != other.hasMoved)
            return false;
        return true;
    }


}
