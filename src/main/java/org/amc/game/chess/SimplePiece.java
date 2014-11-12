package org.amc.game.chess;

/**
 * Shared implementation between ChessPieces
 * @author Adrian Mclaughlin
 *
 */
abstract class SimplePiece implements ChessPiece
{
	private Colour colour;
	private Boolean hasMoved=false;
	
	public SimplePiece(Colour colour){
		this.colour=colour;
	}
	
	public final Colour getColour(){
		return colour;
	}
	
	@Override
    public final boolean isValidMove(ChessBoard board, Move move)
    {
        if(validMovement(move)){
            return canMakeMove(board, move);
        }else{
            return false;
        }
    }
	
	
	
	abstract boolean validMovement(Move move);
	
	abstract boolean canMakeMove(ChessBoard board,Move move);

    @Override
    public void moved() {
        this.hasMoved=true;
        
    }

    @Override
    public boolean hasMoved() {
        // TODO Auto-generated method stub
        return this.hasMoved;
    }
}
