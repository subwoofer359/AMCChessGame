package org.amc.game.chess;

/**
 * Represents a Bishop in the game of chess
 * @author Adrian Mclaughlin
 *
 */
public class BishopPiece extends SimplePiece
{
	public BishopPiece(Colour colour){
		super(colour);
	}
	
	boolean validMovement(Move move){
		return move.getAbsoluteDistanceX()==move.getAbsoluteDistanceY();
	}
	
	boolean canMakeMove(ChessBoard board,Move move){
		Location start=move.getStart();
		
		int positionX=start.getLetter().getName();
		int positionY=start.getNumber();
		
		for(int i=0;i<move.getAbsoluteDistanceX();i++){
			positionX=positionX-1*(int)Math.signum(move.getDistanceX());
			positionY=positionY-1*(int)Math.signum(move.getDistanceY());
			
			if(i!=move.getAbsoluteDistanceX()-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
				return false;
			}else if(i==move.getAbsoluteDistanceX()-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
				ChessPiece piece=board.getPieceFromBoardAt(positionX,positionY);
				if(piece.getColour().equals(getColour())){
					return false;
				}
			}
			System.out.printf("(%d,%d)%n",positionX,positionY);
		}
		System.out.println("Valid move");
		return true;
	}
}
