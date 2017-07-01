package org.amc.game.chess;

import java.util.Collections;
import java.util.Set;

public final class NoChessPiece implements ChessPiece
{
	public static final NoChessPiece NO_CHESSPIECE = new NoChessPiece();
	
	
	private NoChessPiece() {}

	@Override
	public boolean isValidMove(ChessBoard board, Move move) {
		return false;
	}

	@Override
	public Colour getColour() {
		return Colour.NONE;
	}

	@Override
	public ChessPiece moved() {
		return this;
	}

	@Override
	public boolean hasMoved() {
		return false;
	}

	@Override
	public boolean canSlide() {
		return false;
	}

	@Override
	public Set<Location> getPossibleMoveLocations(ChessBoard board, Location location) {
		return Collections.emptySet();
	}

	@Override
	public ChessPiece copy() {
		return this;
	}

}
