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
	
	private static final long serialVersionUID = 841805006071732686L;
	private static final int BLACK_PROMOTION_RANK=1;
    private static final int WHITE_PROMOTION_RANK=8;
    
    private static final PawnPromotionRule instance = new PawnPromotionRule();
    
    public static final PawnPromotionRule getInstance() {
    	return instance;
    }
    
    private PawnPromotionRule() {
	}
    
    /**
     * @see ChessMoveRule#applyRule(ChessGame, Move)
     */
    @Override
    public void applyRule(ChessGame chessGame, Move move) {
        if(isRuleApplicable(chessGame, move)){
            chessGame.getChessBoard().move(move);
            Colour colour=chessGame.getChessBoard().getPieceFromBoardAt(move.getEnd()).getColour();
            ChessPiece queenPiece=new QueenPiece(colour);
            chessGame.getChessBoard().putPieceOnBoardAt(queenPiece, move.getEnd());
            chessGame.getChessBoard().notifyObservers(null);
        }

    }

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessGame, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessGame game, Move move) {
        ChessPiece piece=game.getChessBoard().getPieceFromBoardAt(move.getStart());
        if(isPawnChessPiece(piece)){
            if(Colour.BLACK.equals(piece.getColour())){
                return move.getEnd().getNumber()==BLACK_PROMOTION_RANK;
            }else{
                return move.getEnd().getNumber()==WHITE_PROMOTION_RANK;
            }
        }
        return false;
        
    }

}
