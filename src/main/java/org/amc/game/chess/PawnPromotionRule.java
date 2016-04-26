package org.amc.game.chess;

/**
 * Encapsulate the Pawn Promotion rule
 * Automatically promotions the pawn to a queen.
 * Needs to be improved
 * 
 * @author Adrian Mclaughlin
 *
 */
final class PawnPromotionRule extends PawnPieceRule {

    private static final int BLACK_PROMOTION_RANK = 1;
    private static final int WHITE_PROMOTION_RANK = 8;

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
        if (isRuleApplicable(chessGame, move)) {
            chessGame.getChessBoard().move(move);
            chessGame.setPromotionState();
        }

    }

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessGame, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessGame game, Move move) {
        ChessPiece piece = game.getChessBoard().getPieceFromBoardAt(move.getStart());
        if (isPawnChessPiece(piece)) {
            if (Colour.BLACK.equals(piece.getColour())) {
                return move.getEnd().getNumber() == BLACK_PROMOTION_RANK;
            } else {
                return move.getEnd().getNumber() == WHITE_PROMOTION_RANK;
            }
        }
        return false;

    }
    
    public void promotePawnTo(ChessGame chessGame, Location location, ChessPiece piece) throws IllegalMoveException {
        ChessBoard board = chessGame.getChessBoard();
        ChessPiece pieceToBePromoted = board.getPieceFromBoardAt(location);
        validatePromotionMove(location, pieceToBePromoted, piece);
        board.putPieceOnBoardAt(piece, location);
        returnChessGameToRunningState(chessGame);
    }
    
    private void validatePromotionMove(Location location,ChessPiece pieceToBePromoted, ChessPiece piece) throws IllegalMoveException {
        if(isNotAPawn(pieceToBePromoted)) {
            throw new IllegalMoveException("Can't promote Chess pieces other than a pawn");
        } else if(isPieceNotInEndRank(location, pieceToBePromoted)) {
         throw new IllegalMoveException("Pawn can't be promoted");   
        } else if(pieceToBePromoted.getColour() != piece.getColour()) {
            throw new IllegalMoveException("Promoted piece must be replaced with piece of the same colour");  
        }
    }
    
    private boolean isNotAPawn(ChessPiece piece) {
        return piece == null || !(PawnPiece.class.equals(piece.getClass()));
    }
    
    private boolean isPieceNotInEndRank(Location location, ChessPiece pieceToBePromoted) {
        return pieceToBePromoted.getColour() == Colour.WHITE && location.getNumber() != 8 || 
                        pieceToBePromoted.getColour() == Colour.BLACK && location.getNumber() != 1;
    }
    
    private void returnChessGameToRunningState(ChessGame chessGame) {
        synchronized (chessGame) {
            chessGame.setRunningState();
            chessGame.changePlayer();
        }
    }

}
