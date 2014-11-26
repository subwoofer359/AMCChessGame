package org.amc.game.chess;

/**
 * Encapsulate the Pawn Promotion rule
 * Automatically promotions the pawn to a queen.
 * Needs to be improved
 * 
 * @author Adrian Mclaughlin
 *
 */
public class PawnPromotionRule extends PawnPieceRule {
    private static final int BLACK_PROMOTION_RANK=1;
    private static final int WHITE_PROMOTION_RANK=8;
    
    /**
     * @see ChessRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isRuleApplicable(board, move)){
            board.move(move);
            Colour colour=board.getPieceFromBoardAt(move.getEnd()).getColour();
            ChessPiece queenPiece=new QueenPiece(colour);
            board.putPieceOnBoardAt(queenPiece, move.getEnd());
        }

    }

    /**
     * Rule can't be unapplied
     */
    @Override
    public void unapplyRule(ChessBoard board, Move move) {
        // Do nothing
    }

    /**
     * @see ChessRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
        if(isPawnChessPiece(piece)){
            if(piece.getColour().equals(Colour.BLACK)){
                return move.getEnd().getNumber()==BLACK_PROMOTION_RANK;
            }else{
                return move.getEnd().getNumber()==WHITE_PROMOTION_RANK;
            }
        }
        return false;
        
    }

}
