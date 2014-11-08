package org.amc.game.chess;

public abstract class ComplexPiece extends SimplePiece {

    public ComplexPiece(Colour colour) {
        super(colour);
    }
    

    boolean canMakeMove(ChessBoard board, Move move) {
        int distance=Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
        int positionX=move.getStart().getLetter().getName();
        int positionY=move.getStart().getNumber();
        
        for(int i=0;i<distance;i++){
            positionX=positionX-1*(int)Math.signum(move.getDistanceX());
            positionY=positionY-1*(int)Math.signum(move.getDistanceY());
            
            if(i!=distance-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
                return false;
            }else if(i==distance-1 && board.getPieceFromBoardAt(positionX,positionY)!=null){
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
