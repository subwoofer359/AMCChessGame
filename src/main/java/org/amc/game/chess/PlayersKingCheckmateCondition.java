package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import edu.emory.mathcs.backport.java.util.Collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Player's king has no safe squares to move to
     * 
     * @return Boolean
     */
    boolean isKingNotAbleToMoveOutOfCheck() {
        Set<Location> possibleSafeMoveLocations = findAllSafeMoveLocations(playersKingLocation);
        return possibleSafeMoveLocations.isEmpty();
    }

    private ChessPieceLocation findThePlayersKing() {
        Location kingLocation = board.getPlayersKingLocation(player);
        return new ChessPieceLocation(board.getPieceFromBoardAt(kingLocation), kingLocation);
    }

    private Set<Location> findAllSafeMoveLocations(ChessPieceLocation kingsLocation) {
        Set<Location> possibleMoveLocations = getAllTheKingsPossibleMoveLocations(kingsLocation);
        board.removePieceOnBoardAt(kingsLocation.getLocation());
        Set<Location> squaresUnderAttack = new HashSet<>();

        for (Location location : possibleMoveLocations) {
            for (ChessPieceLocation enemyPiece : enemyLocations) {
                Move move = new Move(enemyPiece.getLocation(), location);
                ChessPiece piece = enemyPiece.getPiece();
                if (piece.isValidMove(board, move)) {
                    squaresUnderAttack.add(location);
                    break;
                }
            }
        }

        possibleMoveLocations.removeAll(squaresUnderAttack);
        board.putPieceOnBoardAt(kingsLocation.getPiece(), kingsLocation.getLocation());
        return possibleMoveLocations;
    }

    private Set<Location> getAllTheKingsPossibleMoveLocations(ChessPieceLocation kingsLocation) {
        return ((KingPiece) kingsLocation.getPiece()).getPossibleMoveLocations(board,
                        kingsLocation.getLocation());
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
                ReversibleMove checkMove = new ReversibleMove(board, move);
                checkMove.testMove();
                if (isPlayersKingInCheck()) {
                    undoMove(checkMove);
                } else {
                    undoMove(checkMove);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isThereMoreThanOneAttacker() {
        return attackingPieces.size() != 1;
    }

    private void undoMove(ReversibleMove move) {
        try {
            move.undoMove();
        } catch (InvalidMoveException ime) {
            throw new RuntimeException(
                            "In canAttackingPieceBeCaptured method:Chessboard in inconsistent state");
        }
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
                    ReversibleMove checkMove = new ReversibleMove(board, move);
                    checkMove.testMove();
                    if (isPlayersKingInCheck()) {
                        undoMove(checkMove);
                    } else {
                        undoMove(checkMove);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean isPlayersKingInCheck() {
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
