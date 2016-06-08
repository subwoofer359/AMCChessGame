package org.amc.game.chess;

/**
 * Encapsulates the en passant capture rule of chess
 * @author Adrian Mclaughlin
 *
 */
final class EnPassantRule extends PawnPieceRule {
	
	private static final EnPassantRule instance = new EnPassantRule();
	
	public static final EnPassantRule getInstance() {
		return instance;
	}
	
	private EnPassantRule() {
		
	}

	/**
     * @see ChessMoveRule#applyRule(ChessGame, Move)
     */
    @Override
    public void applyRule(AbstractChessGame chessGame, Move move) {
        if(isEnPassantCapture(chessGame,move)){
            Location endSquare=move.getEnd();
            ChessPiece piece=chessGame.getChessBoard().getPieceFromBoardAt(move.getStart());
            chessGame.getChessBoard().move(move);
            removeCapturedPawnFromTheChessBoard(chessGame.getChessBoard(), piece, endSquare);
                
        }
    }
    
    private void removeCapturedPawnFromTheChessBoard(ChessBoard board,ChessPiece piece,Location endSquare){
        Location capturedPawnLocation;
        if(Colour.WHITE.equals(piece.getColour())){
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
            
        }
        else{
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
        }
        board.removePieceOnBoardAt(capturedPawnLocation);
    }
    
    /**
     * Checks to see if the pawn does an en passant capture move
     * 
     * @param board
     * @param move
     * @return true if it's a valid en passant move
     */
    boolean isEnPassantCapture(AbstractChessGame game, Move move) {
        Move opponentsMove = game.getTheLastMove();
        ChessPiece opponentsPiece = getOpponentsChessPieceThatMovedLast(game.getChessBoard(), opponentsMove);
        if (isEndSquareNotEmpty(game.getChessBoard(), move)) {
            return false;
        }
        return isPawnChessPiece(opponentsPiece) && 
                        opponentsMove.getAbsoluteDistanceY() == 2
                        && isMoveToSameFile(move, opponentsMove)
                        && isMoveBehindPieceToBeCaptured(opponentsPiece, move, opponentsMove);
        
    }
    
    private ChessPiece getOpponentsChessPieceThatMovedLast(ChessBoard board,Move opponentsMove){
        return board.getPieceFromBoardAt(opponentsMove.getEnd());
    }
    
    private boolean isEndSquareNotEmpty(ChessBoard board,Move move){
        return !board.isEndSquareEmpty(move.getEnd());
    }
   
    private boolean isMoveToSameFile(Move myMove, Move lastOpposingMove) {
        return myMove.getEnd().getLetter().equals(lastOpposingMove.getEnd().getLetter());
    }
    
    private boolean isMoveBehindPieceToBeCaptured(ChessPiece enemyPiece, Move myMove, Move lastOpposingMove) {
        if(Colour.WHITE.equals(enemyPiece.getColour())) {
            return lastOpposingMove.getEnd().getNumber() - myMove.getEnd().getNumber() == 1;
        } else {
            return myMove.getEnd().getNumber() - lastOpposingMove.getEnd().getNumber() == 1;
        }
    }

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessGame, Move)
     */
    @Override
    public boolean isRuleApplicable(AbstractChessGame game, Move move) {
        return isEnPassantCapture(game, move);
    }
}
