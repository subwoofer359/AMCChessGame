package org.amc.game.chess;

public class PawnPiece extends SimplePiece {

    private Boolean initialMove=true;
    public PawnPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        if(move.getAbsoluteDistanceX()==0){
            if(initialMove){
                return isMovingForwardOneOrTwoSquares(move);
            }else{
                return isMovingForwardOneSquareOnly(move);
            }
        }
        else{
            return isMovingForwardDiagonallyOneSquare(move);
        }
    }

    @Override
    boolean canMakeMove(ChessBoard board, Move move) {
        return true;
    }

    public void moved(){
        this.initialMove=false;
    }
    
    private boolean isMovingForwardOneOrTwoSquares(Move move){
        return isMovingForward(move) && move.getAbsoluteDistanceY()>0 && move.getAbsoluteDistanceY()<=2;
    }
    
    private boolean isMovingForwardOneSquareOnly(Move move){
        return isMovingForward(move) && move.getAbsoluteDistanceY()==1;
    }
    
    private boolean isMovingForwardDiagonallyOneSquare(Move move){
        return isMovingForward(move) && Move.isDiagonalMove(move) && move.getAbsoluteDistanceX()==1;
    }
    
    private boolean isMovingForward(Move move){
        int moveInYDirection=move.getDistanceY();
        if(this.getColour().equals(Colour.WHITE)){
            return moveInYDirection>0;
        }
        else{
            return moveInYDirection<0;
        }
    }
    
}
