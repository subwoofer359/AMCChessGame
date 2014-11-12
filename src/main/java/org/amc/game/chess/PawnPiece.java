package org.amc.game.chess;

public class PawnPiece extends SimplePiece {

   
    public PawnPiece(Colour colour) {
        super(colour);
    }

    @Override
    boolean validMovement(Move move) {
        if(move.getAbsoluteDistanceX()==0){
            if(!hasMoved()){
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
        if(Move.isUpOrDownMove(move)){
            if(move.getAbsoluteDistanceY()==2){
                int positionX=move.getStart().getLetter().getName();
                int positionY=move.getStart().getNumber();
                positionX=positionX-1*(int)Math.signum(move.getDistanceX());
                positionY=positionY-1*(int)Math.signum(move.getDistanceY());
                return board.getPieceFromBoardAt(move.getEnd())==null && 
                                board.getPieceFromBoardAt(positionX, positionY)==null;
            }else{
                return board.getPieceFromBoardAt(move.getEnd())==null;
            }
        }else if(Move.isDiagonalMove(move)){
            ChessPiece piece=board.getPieceFromBoardAt(move.getEnd());
            if(piece==null){
                return false;
            }else{
                return endSquareOccupiedByEnemyPiece(piece);
            }
        }
        else{
            return false;
        }
    }
    
    private boolean endSquareOccupiedByEnemyPiece(ChessPiece piece){
        return !piece.getColour().equals(getColour());
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
            return moveInYDirection<0;
        }
        else{
            return moveInYDirection>0;
        }
    }
    
}
