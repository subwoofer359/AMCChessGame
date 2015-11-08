package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.util.List;
import java.util.Set;

public class PlayerInStalement {
    
    private final ChessGamePlayer player;
    private final ChessGamePlayer opponent;
    private final ChessBoard board;
    private final PlayerKingInCheckCondition inCheck;
    
    public PlayerInStalement(ChessGamePlayer player,ChessGamePlayer opponent,ChessBoard board) {
        inCheck = PlayerKingInCheckCondition.getInstance();
        this.player=player;
        this.opponent=opponent;
        this.board=board;
    }
    
    public boolean isStalemate(){
        if(inCheck.isPlayersKingInCheck(player, opponent, board)){
            return false;
        }
        List<ChessPieceLocation> playersPieces=board.getListOfPlayersPiecesOnTheBoard(player);
        for(ChessPieceLocation cpl:playersPieces){
            Set<Location> possibleMoveLocations=cpl.getPiece().getPossibleMoveLocations(board, cpl.getLocation());
            for(Location moveLocation:possibleMoveLocations){
                Move move = new Move(cpl.getLocation(),moveLocation);
                if(willPlayerBeInCheck(move)){
                    continue;
                }else{
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean willPlayerBeInCheck(Move move){
        ChessBoard testBoard=new ChessBoard(board);
        testBoard.move(move);
        return isPlayersKingInCheck(testBoard);
    }
    
    boolean isPlayersKingInCheck(ChessBoard board) {
        return this.inCheck.isPlayersKingInCheck(player, opponent, board);
    }

}
