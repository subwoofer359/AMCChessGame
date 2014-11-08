package org.amc.game.chess;

public class RookPiece extends ComplexPiece {

    public RookPiece(Colour colour) {
        super(colour);
        // TODO Auto-generated constructor stub
    }

    @Override
    boolean validMovement(Move move) {
        System.out.println("validMovement:"+move.getAbsoluteDistanceX()+" "+move.getAbsoluteDistanceY());
        return move.getAbsoluteDistanceX()==0 && move.getAbsoluteDistanceY()>0||
                        move.getAbsoluteDistanceX()>0 && move.getAbsoluteDistanceY()==0;
    }
}
