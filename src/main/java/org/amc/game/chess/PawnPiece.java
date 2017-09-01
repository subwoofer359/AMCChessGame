package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

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
            if (this.hasMoved()) {
            	return isMovingForwardOneSquareOnly(move);
            } else {
            	return isMovingForwardOneOrTwoSquares(move);
            }
        } else {
            return isMovingForwardDiagonallyOneSquare(move);
        }
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
        if (Colour.WHITE == this.getColour()) {
            return move.getDistanceY() > 0;
        } else {
            return move.getDistanceY() < 0;
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
                return canMoveTwoSquaresForward(board, move);
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
        return isEndSquareEmpty(board, move) && 
        		board.isEndSquareEmpty(
        				move.getStart().getLetter().getIndex() + (int) Math.signum(move.getDistanceX()),
        				move.getStart().getNumber() + (int) Math.signum(move.getDistanceY()));
        }

    private boolean canCapture(ChessBoard board, Move move) {
          return isEndSquareOccupiedByOpponentsPiece(board, move);
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
	public Set<Location> getPossibleMoveLocations(ChessBoard board, Location location) {
		Set<Location> locations = new HashSet<>();
		int sign;
		
		if(Colour.WHITE == getColour()) {
			sign = 1;
		} else {
			sign = -1;
		}
		
		int rank = location.getNumber() + sign * 1;
		
		if (rank > 0 && rank <= ChessBoard.BOARD_WIDTH) {
			addSquareInFront(board, location, locations, rank);
			
			addSquareToTheLeft(board, location, locations, rank);
			
			addSquareToTheRight(board, location, locations, rank);
			
		}
		
		if(!hasMoved()) {
			rank = location.getNumber() + sign * 2;
			if (rank > 0 && rank <= ChessBoard.BOARD_WIDTH) {
				Location l = new Location(location.getLetter(), rank);
				addIfValidMove(locations, board, new Move(location, l));
			}
		}
		
		return locations;
	}
	
	private void addSquareInFront(ChessBoard board, Location location, Set<Location> locations, int rank) {
		Location l = new Location(location.getLetter(), rank);
		addIfValidMove(locations, board, new Move(location, l));
	}
	
	private void addSquareToTheLeft(ChessBoard board, Location location, Set<Location> locations, int rank) {
		if(location.getLetter().getIndex() > 0) {
			Location l = new Location(Coordinate.getCoordinate(location.getLetter().getIndex() - 1), rank);
			addIfValidMove(locations, board, new Move(location, l));
		}
	}
    
	private void addSquareToTheRight(ChessBoard board, Location location, Set<Location> locations, int rank) {
		if(location.getLetter().getIndex() < ChessBoard.BOARD_WIDTH) {
			Location l = new Location(Coordinate.getCoordinate(location.getLetter().getIndex() + 1), rank);
			addIfValidMove(locations, board, new Move(location, l));
		}
	}
	
    private void addIfValidMove(Set<Location> locations, ChessBoard board, Move move) {
    	if(isValidMove(board, move)) {
			locations.add(move.getEnd());
		}
    }
}