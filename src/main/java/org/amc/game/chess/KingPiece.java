package org.amc.game.chess;

public class KingPiece extends SimplePiece
{

	public KingPiece(Colour colour){
		super(colour);
	}
	
	@Override
	public boolean isValidMove(ChessBoard board, Move move)
	{
		if(validMovement(move.getAbsoluteDistanceX(),move.getAbsoluteDistanceY())){
			return canMakeMove(board, move);
		}else{
			return false;
		}
	}
	
	/**
	 * King can only move one space in any direction
	 * @param distanceX
	 * @param distanceY
	 * @return if true if a valid move
	 */
	private boolean validMovement(int distanceX,int distanceY){
		return distanceX<=1 && distanceY<=1;
	}
	
	private boolean canMakeMove(ChessBoard board,Move move){
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
