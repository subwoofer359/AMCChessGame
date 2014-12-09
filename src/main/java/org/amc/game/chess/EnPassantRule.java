package org.amc.game.chess;

/**
 * Encapsulates the en passant capture rule of chess
 * @author Adrian Mclaughlin
 *
 */
public class EnPassantRule extends PawnPieceRule{
    
    private ReversibleMove pawnMove;
    private ChessPiece capturedPawn;
    private Location capturedPawnLocation;
    /**
     * @see ChessMoveRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isEnPassantCapture(board,move)){
            Location endSquare=move.getEnd();
            ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
            pawnMove=new ReversibleMove(board, move);
            pawnMove.move();
            removeCapturedPawnFromTheChessBoard(board, piece, endSquare);
                
        }
    }
    
    private void removeCapturedPawnFromTheChessBoard(ChessBoard board,ChessPiece piece,Location endSquare){
        Location capturedPawnLocation;
        if(piece.getColour().equals(Colour.WHITE)){
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
            
        }
        else{
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
        }
        this.capturedPawn=board.getPieceFromBoardAt(capturedPawnLocation);
        this.capturedPawnLocation=capturedPawnLocation;
        board.removePieceOnBoardAt(capturedPawnLocation);
    }
    
    /**
     * En passant move can't be undone
     * 
     * @see ChessMoveRule#unapplyRule(ChessBoard, Move)
     */
    @Override
    public void unapplyRule(ChessBoard board, Move move) {
        if(board.getTheLastMove().equals(move)){
            try{
                pawnMove.undoMove();
                board.putPieceOnBoardAt(capturedPawn, capturedPawnLocation);
            }catch(InvalidMoveException ime){
                throw new RuntimeException("An exception has occurred after trying to undo an en passant capture move");
            }
        }
        
    }
    
    /**
     * Checks to see if the pawn does an en passant capture move
     * 
     * @param board
     * @param move
     * @return true if it's a valid en passant move
     */
    boolean isEnPassantCapture(ChessBoard board, Move move) {
        Move opponentsMove = board.getTheLastMove();
        ChessPiece opponentsPiece = getOpponentsChessPieceThatMovedLast(board, opponentsMove);
        if (isEndSquareNotEmpty(board, move)) {
            return false;
        }
        return isPawnChessPiece(opponentsPiece) && 
                        opponentsMove.getAbsoluteDistanceY() == 2
                        && isMoveToSameFile(move, opponentsMove);
        
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

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        return isEnPassantCapture(board, move);
    }
}
