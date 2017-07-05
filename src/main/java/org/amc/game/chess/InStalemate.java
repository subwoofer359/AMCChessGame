package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

public class InStalemate {
	
	private static final InStalemate INSTANCE = new InStalemate();
	
	public static InStalemate getInstance() {
		return INSTANCE;
	}
	
    private final KingInCheck inCheck;
    
    private InStalemate() {
        inCheck = KingInCheck.getInstance();
    }
    
    public boolean isStalemate(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board){
        if(inCheck.isPlayersKingInCheck(player, opponent, board)){
            return false;
        }
       
        return !board.getListOfPieces(player).stream().filter(pieceLoc -> 
        isPieceNotInStalement(pieceLoc, player, opponent, board)).findFirst().isPresent();
    }
    
    private boolean isPieceNotInStalement(ChessPieceLocation pieceLoc, ChessGamePlayer player,
    		ChessGamePlayer opponent, ChessBoard board) {
    	return pieceLoc.getPiece().getPossibleMoveLocations(
				board, pieceLoc.getLocation()).stream()
				.filter(location -> willPlayerNotBeInCheck(
						player, opponent, board, new Move(
								pieceLoc.getLocation(), location)))
				.findAny().isPresent();
    }
    
	private boolean willPlayerNotBeInCheck(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board,
			Move move) {
		ChessBoard testBoard = new ChessBoard(board);
		testBoard.move(move);
		return !inCheck.isPlayersKingInCheck(player, opponent, testBoard);
	}
}
