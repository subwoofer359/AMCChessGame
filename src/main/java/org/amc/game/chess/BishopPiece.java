package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
/**
 * Represents a Bishop in the game of chess
 * @author Adrian Mclaughlin
 *
 */
public class BishopPiece implements ChessPiece
{
	private Colour colour;
	
	public BishopPiece(Colour colour){
		this.colour=colour;
	}
	
	@Override
	public boolean isValidMove(ChessBoard board,Coordinate letterCoordinateOne,int numberCoordinateOne,
			Coordinate letterCoordinateTwo,int numberCoordinateTwo)
	{
		System.out.printf("(%d,%d)%n",letterCoordinateOne.getName()-letterCoordinateTwo.getName(),numberCoordinateOne-numberCoordinateTwo);
		
		int moveInX=letterCoordinateOne.getName()-letterCoordinateTwo.getName();
		int moveInY=numberCoordinateOne-numberCoordinateTwo;
		if(Math.abs(moveInX)!=Math.abs(moveInY)){
			System.out.println("inValid move");
			return false;
		}else{
			int positionX=letterCoordinateOne.getName();
			int positionY=numberCoordinateOne;
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
	
	public Colour getColour(){
		return this.colour;
	}
}
