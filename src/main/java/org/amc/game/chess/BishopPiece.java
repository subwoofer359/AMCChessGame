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
		Location start=move.getStart();
		Location end=move.getEnd();
		System.out.printf("(%d,%d)%n",start.getLetter().getName()-end.getLetter().getName(),start.getNumber()-end.getNumber());
		
		int moveInX=start.getLetter().getName()-end.getLetter().getName();
		int moveInY=start.getNumber()-end.getNumber();
		if(Math.abs(moveInX)!=Math.abs(moveInY)){
			System.out.println("inValid move");
			return false;
		}else{
			int positionX=start.getLetter().getName();
			int positionY=start.getNumber();
			for(int i=0;i<Math.abs(moveInX);i++){
				positionX=positionX-1*(int)Math.signum(moveInX);
				positionY=positionY-1*(int)Math.signum(moveInY);
				
				if(i!=Math.abs(moveInX)-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
					return false;
				}else if(i==Math.abs(moveInX)-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
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
}
