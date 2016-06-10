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
class PlayersKingCheckmateCondition {

    private final PlayerKingInCheckCondition kingIsChecked = PlayerKingInCheckCondition.getInstance();
    private final ChessGamePlayer player;
    private final ChessGamePlayer opponent;
    private final ChessBoard board;
    private final ChessPieceLocation playersKingLocation;
    private final List<ChessPieceLocation> enemyLocations;
    private final List<ChessPieceLocation> attackingPieces;
    private final List<ChessPieceLocation> playersPieces;

    public PlayersKingCheckmateCondition(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board) {
        this.player = player;
        this.opponent = opponent;
        this.board = board;

        this.playersKingLocation = findThePlayersKing();
        this.enemyLocations = board.getListOfPlayersPiecesOnTheBoard(opponent);
        this.attackingPieces = getAllPiecesAttackingTheKing();
        this.playersPieces = board.getListOfPlayersPiecesOnTheBoard(player);
    }

    private final ChessPieceLocation findThePlayersKing() {
        Location kingLocation = board.getPlayersKingLocation(player);
        return new ChessPieceLocation(board.getPieceFromBoardAt(kingLocation), kingLocation);
    }
    
    /**
     * Checks to see if the player's king is checkmated.
     *
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

    

    private final Set<Location> findAllSafeMoveLocations(ChessPieceLocation kingsLocation) {
        Set<Location> possibleMoveLocations = getAllTheKingsPossibleMoveLocations(kingsLocation);
        removeKingFromTheBoard(kingsLocation);
        Set<Location> squaresUnderAttack = getSquaresUnderAttack(possibleMoveLocations);

        possibleMoveLocations.removeAll(squaresUnderAttack);
        replaceKingOnTheBoard(kingsLocation);
        return possibleMoveLocations;
    }

    private final Set<Location> getAllTheKingsPossibleMoveLocations(ChessPieceLocation kingsLocation) {
        return kingsLocation.getPiece().getPossibleMoveLocations(board,
                        kingsLocation.getLocation());
    }
    
    private final void removeKingFromTheBoard(ChessPieceLocation kingsLocation){
        board.removePieceOnBoardAt(kingsLocation.getLocation());
    }
    
    /**
     * Find Squares that are under attack by other Player Can use
     * ChessPiece.isValidMove(ChessBoard, Move) on all pieces except PawnPiece.
     * Must use PawnPiece.validMovement(Move) due to the Pawn special capturing
     * move
     * 
     * @param possibleMoveLocations Set of Squares to be checked
     * @return Set of Squares that can be attacked
     */
    private final Set<Location> getSquaresUnderAttack(Set<Location> possibleMoveLocations) {
        Set<Location> squaresUnderAttack = new HashSet<>();
        for (Location location : possibleMoveLocations) {
            for (ChessPieceLocation enemyPiece : enemyLocations) {
                Move move = new Move(enemyPiece.getLocation(), location);
                ChessPiece piece = enemyPiece.getPiece();

                ChessPiece occupyPiece = board.getPieceFromBoardAt(location);
                board.removePieceOnBoardAt(location);

                if (piece.isValidMove(board, move) || piece instanceof PawnPiece
                                && ((PawnPiece) piece).validMovement(move)) {
                    squaresUnderAttack.add(location);
                    board.putPieceOnBoardAt(occupyPiece, location);
                    break;
                }

                board.putPieceOnBoardAt(occupyPiece, location);
            }
        }
        return squaresUnderAttack;
    }
    
    private final void replaceKingOnTheBoard(ChessPieceLocation kingsLocation){
        board.putPieceOnBoardAt(kingsLocation.getPiece(), kingsLocation.getLocation());
    }
    
    /**
     * Checks to see if the Player can capture the attacking ChessPiece Only if
     * the capture doesn't lead to the King still being checked
     *
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
                if (willPlayerNotBeInCheck(move)) {
                    return false;
                }
            }
        }
        return true;
    }

    private final boolean willPlayerNotBeInCheck(Move move){
        ChessBoard testBoard=new ChessBoard(board);
        testBoard.move(move);
        return !isPlayersKingInCheck(testBoard);
    }
    
    private final boolean isThereMoreThanOneAttacker() {
        return attackingPieces.size() != 1;
    }

    /**
     * Checks to see if the attacking ChessPiece can be blocked
     *
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
                    if (willPlayerNotBeInCheck(blockingMove)) {
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
     * @param piece {@link ChessPiece}
     *
     * @param move
     *            Move
     * @return Set of Locations
     */
    private final Set<Location> getAllSquaresInAMove(ChessPiece piece, Move move) {
        Set<Location> squares = new HashSet<>();
        if (piece.canSlide()) {
            int distance = Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
            int positionX = move.getStart().getLetter().getIndex();
            int positionY = move.getStart().getNumber();

            for (int i = 0; i < distance - 1; i++) {
                positionX = positionX + (int) Math.signum(move.getDistanceX());
                positionY = positionY + (int) Math.signum(move.getDistanceY());
                squares.add(new Location(Coordinate.values()[positionX], positionY));
            }
        }
        return squares;
    }

    /**
     * Find all opponents pieces directly attacking the king
     *
     * @return List of ChessPieceLocation of attacking pieces
     */
    final List<ChessPieceLocation> getAllPiecesAttackingTheKing() {
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
