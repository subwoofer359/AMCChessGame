package org.amc.game.chess;

public class EnPassantRule implements ChessRule {


    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isMoveEnPassantCapture(board,move)){
            Location endSquare=move.getEnd();
            ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
            if(piece.getColour().equals(Colour.WHITE)){
                Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
                board.removePieceOnBoardAt(capturedPawn);
            }
            else{
                Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
                board.removePieceOnBoardAt(capturedPawn);
            }    
        }
    }
    
    /**
     * Checks to see if the move is en passant
     * calls the PawnPiece.isEnPassantCapture
     * 
     * @param move
     * @return boolean
     */
    boolean isMoveEnPassantCapture(ChessBoard board,Move move){
        ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
        if(piece instanceof PawnPiece){
            return ((PawnPiece)piece).isEnPassantCapture(board, move);
        }else{
            return false;
        }
    }
}
