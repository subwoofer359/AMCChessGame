package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * Shared implementation between ChessPieces
 * 
 * @author Adrian Mclaughlin
 *
 */
abstract class SimplePiece implements ChessPiece {
	private final Colour colour;
	private final boolean hasMoved;

	SimplePiece(Colour colour) {

		this.colour = colour;
		this.hasMoved = false;
	}

	SimplePiece(Colour colour, boolean hasMoved) {
		this.colour = colour;
		this.hasMoved = hasMoved;
	}

	/**
	 * @see ChessPiece#getColour()
	 */
	@Override
	public Colour getColour() {
		return colour;
	}

	/**
	 * @see ChessPiece#isValidMove(ChessBoard, Move) Using Template design
	 *      pattern Should be final not to be overridden by subclasses
	 */
	@Override
	public boolean isValidMove(ChessBoard board, Move move) {
		return isChessPieceAtStart(board, move) && validMovement(move) && canMakeMove(board, move);
	}

	private boolean isChessPieceAtStart(ChessBoard board, Move move) {
		return board.get(move.getStart()) == this;
	}

	/**
	 * Checks to see if the move is a valid move for the chess piece
	 * 
	 * @param move {@link Move}
	 * @return boolean true if the move is allowed
	 */
	abstract boolean validMovement(Move move);

	/**
	 * Checks to see if the move can be completed, if there are no ChessPieces
	 * in the way of the move and if there is a ChessPiece in the end Square of
	 * the move
	 * 
	 * @param board {@link ChessBoard}
	 * @param move {@link Move} Move can be a move or a capture
	 * @return true if the move can be made
	 */
	abstract boolean canMakeMove(ChessBoard board, Move move);

	/**
	 * @see ChessPiece#hasMoved()
	 */
	@Override
	public boolean hasMoved() {
		return this.hasMoved;
	}

	/**
	 * Checks to see if the opposing Player's ChessPiece is in the end Square
	 * 
	 * @param board {@link ChessBoard}
	 * @param move  {@link Move}
	 * @return true if there is an opposing Player's ChessPiece in the end
	 *         Square
	 */
	boolean isEndSquareOccupiedByOpponentsPiece(ChessBoard board, Move move) {
		Location endSquare = move.getEnd();
		ChessPiece piece = board.get(endSquare.getLetter().getIndex(), endSquare.getNumber());
		return NoChessPiece.NO_CHESSPIECE != piece && piece.getColour() != getColour();
	}

	/**
	 * @see ChessPiece#getPossibleMoveLocations(ChessBoard, Location)
	 */
	public Set<Location> getPossibleMoveLocations(ChessBoard board, Location location) {
		Set<Location> locations = new HashSet<>();
		for (Coordinate coord : Coordinate.values()) {
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
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s (%s)", this.getClass().getSimpleName(), getColour());
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + (hasMoved ? 1231 : 1237);
		return result;
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
		SimplePiece other = (SimplePiece) obj;
		if (colour != other.colour)
			return false;
		return hasMoved == other.hasMoved;
	}

	@Override
	public boolean canSlide() {
		return true;
	}

	@Override
	public ChessPiece copy() {
		return this;

	}

	boolean isEndSquareEmpty(ChessBoard board, Move move) {
		return board.isEndSquareEmpty(move.getEnd());
	}

	/**
	 * Default property is for the ChessPiece to slide
	 * 
	 * @return true so ChessPiece can slide
	 */

}
