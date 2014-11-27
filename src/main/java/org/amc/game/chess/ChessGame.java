package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Contains the Rules of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessGame {
    private ChessBoard board;
    private Player currentPlayer;
    private Player playerOne;
    private Player playerTwo;
    List<ChessMoveRule> chessRules;
    
    public ChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        this.board = board;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.currentPlayer = this.playerOne;
        chessRules=new ArrayList<>();
        chessRules.add(new EnPassantRule());
        chessRules.add(new CastlingRule());
        chessRules.add(new PawnPromotionRule());
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Changes the current player
     */
    public void changePlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }
    
    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * 
     * @param player
     *            Player making the move
     * @param move
     *            Move
     * @throws InvalidMoveException
     *             if not a valid movement
     */
    public void move(Player player, Move move)throws InvalidMoveException{
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        checkChessPieceExistOnSquare(piece, move);
        checkItsthePlayersPiece(player, piece);
        moveThePlayersChessPiece(player, board, piece, move);
    }
    
    private void checkChessPieceExistOnSquare(ChessPiece piece, Move move)throws InvalidMoveException{
        if (piece == null) {
            throw new InvalidMoveException("No piece at " + move.getStart());
        }
    }
    
    private void checkItsthePlayersPiece(Player player,ChessPiece piece)throws InvalidMoveException{
        if (notPlayersChessPiece(player, piece)) {
            throw new InvalidMoveException("Player can only move their own pieces");
        } 
    }
   
    private void moveThePlayersChessPiece(Player player,ChessBoard board,ChessPiece piece,Move move) throws InvalidMoveException{
        if(isPlayersKingInCheck(player, board)){
            if(piece.isValidMove(board, move)){
                thenMoveChessPiece(player,move);
            }else{
                throw new InvalidMoveException("Not a valid move");
            }
        }else if(doesAGameRuleApply(board, move)){
            thenApplyGameRule(player, move);
        }else if(piece.isValidMove(board, move)){
                thenMoveChessPiece(player,move);
        }else{
            throw new InvalidMoveException("Not a valid move");
        }
    }
    
    private void thenApplyGameRule(Player player,Move move) throws InvalidMoveException{
        for(ChessMoveRule rule:chessRules){
            rule.applyRule(board, move);
            if(isPlayersKingInCheck(player, board)){
                rule.unapplyRule(board, move);
                throw new InvalidMoveException("King is checked");
            }
        }
    }

    private void thenMoveChessPiece(Player player,Move move) throws InvalidMoveException{
        ReversibleMove reversible=new ReversibleMove(board, move);
        reversible.move();
        if(isPlayersKingInCheck(player, board)){
            reversible.undoMove();
            throw new InvalidMoveException("King is checked");
        }
    }
    
    /**
     * Return true if the Player owns the chess piece
     * @param player
     * @param piece
     * @return boolean
     */
    private boolean notPlayersChessPiece(Player player,ChessPiece piece){
        return player.getColour() != piece.getColour();
    }
    
    /**
     * Checks to see if the game has reached it's completion
     * 
     * @param playerOne
     * @param playerTwo
     * @return Boolean
     */
    public boolean isGameOver(Player playerOne, Player playerTwo) {
        boolean playerOneHaveTheirKing = doesThePlayerStillHaveTheirKing(playerOne);
        boolean playerTwoHaveTheirKing = doesThePlayerStillHaveTheirKing(playerTwo);
        if (!playerOneHaveTheirKing || isCheckMate(playerOne, board)) {
            playerTwo.isWinner(true);
            return true;
        } else if (!playerTwoHaveTheirKing || isCheckMate(playerTwo,board)) {
            playerOne.isWinner(true);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks to see if the player's king is checkmated.
     * 
     * @param player Player whos king ChessPiece is checkmated
     * @param board ChessBoard
     * @return Boolean true if checkmate has occurred
     */
    boolean isCheckMate(Player player,ChessBoard board){
        return isPlayersKingInCheck(player, board) && 
        cantKingMoveOutOfCheck(player, board) && 
        !canAttackingPieceBeCaptured(player,board) &&
        !canAttackingPieceBeBlocked(player,board);
        
    }
    
    /**
     * Player's king has no safe squares to move to
     * 
     * @param player Player who's King is checkec
     * @param board ChessBoard
     * @return Boolean
     */
    boolean cantKingMoveOutOfCheck(Player player,ChessBoard board){
        Location kingLocation=board.getPlayersKingLocation(player);
        ChessPiece kingPiece=board.getPieceFromBoardAt(kingLocation);
        Set<Location> possibleMoveLocations=new KingPiece(player.getColour()).getPossibleMoveLocations(board,kingLocation);
        board.removePieceOnBoardAt(kingLocation);
        Set<Location> squaresUnderAttack=new HashSet<>();
        List<ChessPieceLocation> enemyLocations=board.getListOfPlayersPiecesOnTheBoard(player==playerOne?playerTwo:playerOne);
        for(Location loc:possibleMoveLocations){
            for(ChessPieceLocation cpl:enemyLocations){
                Move move=new Move(cpl.getLocation(), loc);
                ChessPiece piece=cpl.getPiece();
                if(piece.isValidMove(board, move)){
                    squaresUnderAttack.add(loc);
                    break;
                }
            }
        }
        possibleMoveLocations.removeAll(squaresUnderAttack);
        board.putPieceOnBoardAt(kingPiece, kingLocation);
        return possibleMoveLocations.isEmpty();
    }
    
    /**
     * Checks to see if the Player can capture the attacking ChessPiece
     * Only if the capture doesn't lead to the King still being checked 
     * 
     * @param player Player
     * @param board ChessBoard
     * @return Boolean
     */
    boolean canAttackingPieceBeCaptured(Player player,ChessBoard board){
        Location attackingPieceLocation=board.getTheLastMove().getEnd();
        List<ChessPieceLocation> myPieces=board.getListOfPlayersPiecesOnTheBoard(player);
        for(ChessPieceLocation cpl:myPieces){
            ChessPiece piece=cpl.getPiece();
            Move move=new Move(cpl.getLocation(),attackingPieceLocation);
            if(piece.isValidMove(board, move)){
                ReversibleMove checkMove =new ReversibleMove(board,move);
                checkMove.testMove();
                if(!isPlayersKingInCheck(player, board)){
                    checkMove.undoMove();
                    return true;
                }else{
                        checkMove.undoMove();
                }
            }
        }
        return false;
    }
    
    /**
     * Checks to see if the attacking ChessPiece can be blocked
     * 
     * @param player
     * @param board
     * @return Boolean true if the attacking ChessPiece can be blocked.
     */
    boolean canAttackingPieceBeBlocked(Player player,ChessBoard board){
        Location attackingPieceLocation=board.getTheLastMove().getEnd();
        Location playersKingLocation=board.getPlayersKingLocation(player);
        List<ChessPieceLocation> myPieces=board.getListOfPlayersPiecesOnTheBoard(player);
        ChessPiece attacker=board.getPieceFromBoardAt(attackingPieceLocation);
        if(!attacker.canSlide()){
            return false;
        }else{
            Move move=new Move(attackingPieceLocation,playersKingLocation);
            Set<Location> blockingSquares=board.getAllSquaresInAMove(move);
            for(Location blockingSquare:blockingSquares){
                for(ChessPieceLocation cpl:myPieces){
                    Move blockingMove=new Move(cpl.getLocation(),blockingSquare);
                    ChessPiece piece=cpl.getPiece();
                    if(!(piece instanceof KingPiece) && piece.isValidMove(board, blockingMove)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks to see if the Player still possesses their King
     * 
     * @param player
     * @return false if they lost their King ChessPiece
     */
    boolean doesThePlayerStillHaveTheirKing(Player player) {
        List<ChessPiece> allPlayersChessPieces = getAllPlayersChessPiecesOnTheBoard(player);
        for (ChessPiece piece : allPlayersChessPieces) {
            if (piece.getClass().equals(KingPiece.class)) {
                return true;
            }
        }
        return false;

    }

    /**
     * creates a List of all the Player's pieces still on the board
     * 
     * @param player
     * @return List of ChessPieces
     */
    List<ChessPiece> getAllPlayersChessPiecesOnTheBoard(Player player) {
        List<ChessPiece> pieceList = new ArrayList<ChessPiece>();
        for (Coordinate letter : Coordinate.values()) {
            for (int i = 1; i <= 8; i++) {
                ChessPiece piece = board.getPieceFromBoardAt(letter.getName(), i);
                if (piece == null) {
                    continue;
                } else {
                    if (piece.getColour().equals(player.getColour())) {
                        pieceList.add(piece);
                    }
                }
            }
        }
        return pieceList;
    }
    
    /**
     * Checks to see if the opponent's ChessPieces are attacking the Player's king
     * @param player Player who King might be under attack
     * @param board ChessBoard current ChessBoard
     * @return Boolean true if the opponent can capture the Player's king on the next turn
     */
    public boolean isPlayersKingInCheck(Player player,ChessBoard board){
        Location playersKingLocation=board.getPlayersKingLocation(player);
        List<ChessPieceLocation> listOfEnemysPieces=board.getListOfPlayersPiecesOnTheBoard(player==playerOne?playerTwo:playerOne);
        for(ChessPieceLocation pieceLocation:listOfEnemysPieces){
            Move move=new Move(pieceLocation.getLocation(),playersKingLocation);
            if(pieceLocation.getPiece().isValidMove(board, move)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks to see if a game rule applies to the Player's move
     * Only applies one rule per move
     * @param board
     * @param move
     * @return Boolean true if a Game rule applies to the Player's move
     */
    boolean doesAGameRuleApply(ChessBoard board, Move move){
        for(ChessMoveRule rule:chessRules){
            if(rule.isRuleApplicable(board, move)){
                return true;
            }
        }
        return false;
    }
    
    void setGameRules(List<ChessMoveRule> rules){
        this.chessRules=rules;
    }
    
    void setChessBoard(ChessBoard board){
        this.board=board;
    }
}
