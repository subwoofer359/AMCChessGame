package org.amc.game.chess;

public class KingPiece extends SimplePiece
{

	public KingPiece(Colour colour){
		super(colour);
	}

	/**
	 * King can only move one space in any direction
	 * @param distanceX
	 * @param distanceY
	 * @return if true if a valid move
	 */
	boolean validMovement(Move move){
	    int distanceX=move.getAbsoluteDistanceX();
	    int distanceY=move.getAbsoluteDistanceY();
		return distanceX<=1 && distanceY<=1;
	}
	
	boolean canMakeMove(ChessBoard board,Move move){
		ChessPiece piece=board.getPieceFromBoardAt(
				move.getEnd().getLetter().getName(), move.getEnd().getNumber());
		if(piece==null){
			return true;
		}else{
			return endSquareNotOccupiedByPlayersOwnPiece(piece);
		}
	}
	
	private boolean endSquareNotOccupiedByPlayersOwnPiece(ChessPiece piece){
		return !piece.getColour().equals(getColour());
	}
}
