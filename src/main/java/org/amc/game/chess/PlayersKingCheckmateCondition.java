package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Checks to see if the player is checkmated.
 * 
 * @author Adrian Mclaughlin
 *
 */
public class PlayersKingCheckmateCondition {

    private PlayerKingInCheckCondition kingIsChecked = new PlayerKingInCheckCondition();
    private Player player;
    private Player opponent;
    private ChessBoard board;
    private ChessPieceLocation playersKingLocation;
    private List<ChessPieceLocation> enemyLocations;
    private List<ChessPieceLocation> attackingPieces;
    private List<ChessPieceLocation> playersPieces;

    public PlayersKingCheckmateCondition(Player player, Player opponent, ChessBoard board) {
        this.player = player;
        this.opponent = opponent;
        this.board = board;

        this.playersKingLocation = findThePlayersKing();
        this.enemyLocations = board.getListOfPlayersPiecesOnTheBoard(opponent);
        this.attackingPieces = getAllPiecesAttackingTheKing();
        this.playersPieces = board.getListOfPlayersPiecesOnTheBoard(player);
    }

    private ChessPieceLocation findThePlayersKing() {
        Location kingLocation = board.getPlayersKingLocation(player);
        return new ChessPieceLocation(board.getPieceFromBoardAt(kingLocation), kingLocation);
    }
    
    /**
     * Checks to see if the player's king is checkmated.
     * 
     * @param player
     *            Player whos king ChessPiece is checkmated
     * @param board
     *            ChessBoard
     * @return Boolean true if checkmate has occurred
     */
    boolean isCheckMate() {
        return isPlayersKingInCheck() && isKingNotAbleToMoveOutOfCheck()
                        && canAttackingPieceNotBeCaptured() && canAttackingPieceNotBeBlocked();
    }
    
    boolean isPlayersKingInCheck() {
        return this.kingIsChecked.isPlayersKingInCheck(player, opponent, board);
    }

    /**
     * Player's king has no safe squares to move to
     * 
     * @return Boolean
     */
    boolean isKingNotAbleToMoveOutOfCheck() {
        Set<Location> possibleSafeMoveLocations = findAllSafeMoveLocations(playersKingLocation);
        return possibleSafeMoveLocations.isEmpty();
    }

    

    private Set<Location> findAllSafeMoveLocations(ChessPieceLocation kingsLocation) {
        Set<Location> possibleMoveLocations = getAllTheKingsPossibleMoveLocations(kingsLocation);
        removeKingFromTheBoard(kingsLocation);
        Set<Location> squaresUnderAttack = getSquaresUnderAttack(possibleMoveLocations);

        possibleMoveLocations.removeAll(squaresUnderAttack);
        replaceKingOnTheBoard(kingsLocation);
        return possibleMoveLocations;
    }

    private Set<Location> getAllTheKingsPossibleMoveLocations(ChessPieceLocation kingsLocation) {
        return ((KingPiece) kingsLocation.getPiece()).getPossibleMoveLocations(board,
                        kingsLocation.getLocation());
    }
    
    private void removeKingFromTheBoard(ChessPieceLocation kingsLocation){
        board.removePieceOnBoardAt(kingsLocation.getLocation());
    }
    
    private Set<Location> getSquaresUnderAttack(Set<Location> possibleMoveLocations) {
        Set<Location> squaresUnderAttack = new HashSet<>();
        for (Location location : possibleMoveLocations) {
            for (ChessPieceLocation enemyPiece : enemyLocations) {
                Move move = new Move(enemyPiece.getLocation(), location);
                ChessPiece piece = enemyPiece.getPiece();
                
                ChessPiece occupyPiece = board.getPieceFromBoardAt(location);
                board.removePieceOnBoardAt(location);
                
                if (piece.isValidMove(board, move)) {
                    squaresUnderAttack.add(location);
                    board.putPieceOnBoardAt(occupyPiece, location);
                    break;
                }
                board.putPieceOnBoardAt(occupyPiece, location);
            }
        }
        return squaresUnderAttack;
    }
    
    private void replaceKingOnTheBoard(ChessPieceLocation kingsLocation){
        board.putPieceOnBoardAt(kingsLocation.getPiece(), kingsLocation.getLocation());
    }
    
    /**
     * Checks to see if the Player can capture the attacking ChessPiece Only if
     * the capture doesn't lead to the King still being checked
     * 
     * @param player
     *            Player
     * @param board
     *            ChessBoard
     * @return Boolean
     */
    boolean canAttackingPieceNotBeCaptured() {
        if (isThereMoreThanOneAttacker()) {
            return true;
        }
        Location attackingPieceLocation = attackingPieces.get(0).getLocation();
        for (ChessPieceLocation cpl : playersPieces) {
            ChessPiece piece = cpl.getPiece();
            Move move = new Move(cpl.getLocation(), attackingPieceLocation);
            if (piece.isValidMove(board, move)) {
                if (willPlayerBeInCheck(move)) {
                    continue;
                } else {
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
    
    private boolean isThereMoreThanOneAttacker() {
        return attackingPieces.size() != 1;
    }

    /**
     * Checks to see if the attacking ChessPiece can be blocked
     * 
     * @param player
     * @param board
     * @return Boolean true if the attacking ChessPiece can be blocked.
     */
    boolean canAttackingPieceNotBeBlocked() {
        if (isThereMoreThanOneAttacker()) {
            return true;
        }
        Location attackingPieceLocation = attackingPieces.get(0).getLocation();
        ChessPiece attacker = board.getPieceFromBoardAt(attackingPieceLocation);

        Move move = new Move(attackingPieceLocation, playersKingLocation.getLocation());
        Set<Location> blockingSquares = getAllSquaresInAMove(attacker, move);
        for (Location blockingSquare : blockingSquares) {
            for (ChessPieceLocation cpl : playersPieces) {
                Move blockingMove = new Move(cpl.getLocation(), blockingSquare);
                ChessPiece piece = cpl.getPiece();
                if (!(piece instanceof KingPiece) && piece.isValidMove(board, blockingMove)) {
                    if (willPlayerBeInCheck(blockingMove)) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * To analyse the ChessBoard configuration for the condition of check 
     * 
     * @param board ChessBoard
     * @return boolean true if Player is in check
     */
    boolean isPlayersKingInCheck(ChessBoard board) {
        return this.kingIsChecked.isPlayersKingInCheck(player, opponent, board);
    }

    /**
     * Returns a Set of Squares covered in a move Not including the start and
     * end squares
     * 
     * @param move
     *            Move
     * @return Set of Locations
     */
    private Set<Location> getAllSquaresInAMove(ChessPiece piece, Move move) {
        Set<Location> squares = new HashSet<>();
        if (piece.canSlide()) {
            int distance = Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
            int positionX = move.getStart().getLetter().getIndex();
            int positionY = move.getStart().getNumber();

            for (int i = 0; i < distance - 1; i++) {
                positionX = positionX + 1 * (int) Math.signum(move.getDistanceX());
                positionY = positionY + 1 * (int) Math.signum(move.getDistanceY());
                squares.add(new Location(Coordinate.values()[positionX], positionY));
            }
        }
        return squares;
    }

    /**
     * Find all opponents pieces directly attacking the king
     * 
     * @param player
     *            Player
     * @param opponent
     *            Player
     * @param board
     *            ChessBoard
     * @return List of ChessPieceLocation of attacking pieces
     */
    List<ChessPieceLocation> getAllPiecesAttackingTheKing() {
        List<ChessPieceLocation> attackingPieces = new ArrayList<>();
        for (ChessPieceLocation cpl : enemyLocations) {
            Move move = new Move(cpl.getLocation(), playersKingLocation.getLocation());
            if (cpl.getPiece().isValidMove(board, move)) {
                attackingPieces.add(cpl);
            }
        }
        return attackingPieces;
    }

}
