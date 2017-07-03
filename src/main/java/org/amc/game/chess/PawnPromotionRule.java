package org.amc.game.chess;


/**
 * Encapsulate the Pawn Promotion rule
 * Automatically promotions the pawn to a queen.
 * Needs to be improved
 * 
 * @author Adrian Mclaughlin
 *
 */
public final class PawnPromotionRule extends PawnPieceRule {
	
	public static String ERROR_CAN_ONLY_PROMOTE_PAWN = "Can't promote Chess pieces other than a pawn";

    private static final int BLACK_PROMOTION_RANK = 1;
    private static final int WHITE_PROMOTION_RANK = 8;

    private static final PawnPromotionRule instance = new PawnPromotionRule();

    public static final PawnPromotionRule getInstance() {
        return instance;
    }

    private PawnPromotionRule() {
    }

    /**
     * @see ChessMoveRule#applyRule(AbstractChessGame, Move)
     */
    @Override
    public void applyRule(AbstractChessGame chessGame, Move move) {
        if (isRuleApplicable(chessGame, move)) {
            chessGame.getChessBoard().move(move);
            chessGame.setPromotionState();
        }

    }

    /**
     * @see ChessMoveRule#isRuleApplicable(AbstractChessGame, Move)
     * todo check for piece as nulls
     */
    @Override
    public boolean isRuleApplicable(AbstractChessGame game, Move move) {
        ChessPiece piece = game.getChessBoard().get(move.getStart());
        return isRuleApplicable(move.getEnd(), piece) && 
        		piece.isValidMove(game.getChessBoard(), move);  
        		
    }
    
    private boolean isRuleApplicable(Location location, ChessPiece pieceToBePromoted) {
        if (isPawnChessPiece(pieceToBePromoted)) {
            if (Colour.BLACK == pieceToBePromoted.getColour()) {
                return location.getNumber() == BLACK_PROMOTION_RANK;
            } else {
                return location.getNumber() == WHITE_PROMOTION_RANK;
            }
        }
        return false;
    }
    
    public void promotePawnTo(ChessGame chessGame, Location location, ChessPiece promotedPiece) throws IllegalMoveException {
        ChessBoard board = chessGame.getChessBoard();
        ChessPiece pieceToBePromoted = board.get(location);
        validatePromotionMove(location, pieceToBePromoted, promotedPiece);
        board.put(promotedPiece, location);
        returnChessGameToRunningState(chessGame);
    }
    
    private void validatePromotionMove(Location location,ChessPiece pieceToBePromoted, ChessPiece promotedPiece) throws IllegalMoveException {
        if(isNotAPawn(pieceToBePromoted)) {
            throw new IllegalMoveException(ERROR_CAN_ONLY_PROMOTE_PAWN);
        } else if(isKingOrPawn(promotedPiece)) {
            throw new IllegalMoveException("Pawn can't be promoted to " + promotedPiece);
        } else if(!isRuleApplicable(location, pieceToBePromoted)) {
            throw new IllegalMoveException("Pawn can't be promoted");   
        } else if(pieceToBePromoted.getColour() != promotedPiece.getColour()) {
            throw new IllegalMoveException("Promoted piece must be replaced with piece of the same colour");  
        }
    }
    
    private boolean isNotAPawn(ChessPiece piece) {
        return !(PawnPiece.class.equals(piece.getClass()));
    }
    
    private boolean isKingOrPawn(ChessPiece piece) {
        return KingPiece.class.equals(piece.getClass()) || 
        		PawnPiece.class.equals(piece.getClass());
           
    }
    
    private void returnChessGameToRunningState(ChessGame chessGame) {
            chessGame.setRunningState();
            chessGame.changePlayer();
    }
    
}
