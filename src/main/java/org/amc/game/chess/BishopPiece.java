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
	
	@Override
	public boolean isValidMove(ChessBoard board,Move move)
	{
		if(validMovement(move.getDistanceX(),move.getDistanceY())){
			return canMakeMove(board,move);
		}else{
			System.out.println("inValid move");
			return false;
		}
	}
	
	private boolean validMovement(int distanceX,int distanceY){
		return Math.abs(distanceX)==Math.abs(distanceY);
	}
	
	private boolean canMakeMove(ChessBoard board,Move move){
		Location start=move.getStart();
		Location end=move.getEnd();
		System.out.printf("(%d,%d)%n",start.getLetter().getName()-end.getLetter().getName(),start.getNumber()-end.getNumber());
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
