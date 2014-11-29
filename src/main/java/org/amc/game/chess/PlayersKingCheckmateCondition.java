package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayersKingCheckmateCondition {

    private PlayerKingInCheckCondition kingIsChecked = new PlayerKingInCheckCondition();

    /**
     * Checks to see if the player's king is checkmated.
     * 
     * @param player
     *            Player whos king ChessPiece is checkmated
     * @param board
     *            ChessBoard
     * @return Boolean true if checkmate has occurred
     */
    boolean isCheckMate(Player player, Player opponent, ChessBoard board) {
        return isPlayersKingInCheck(player, opponent, board)
                        && cantKingMoveOutOfCheck(player, opponent, board)
                        && !canAttackingPieceBeCaptured(player, opponent, board)
                        && !canAttackingPieceBeBlocked(player, opponent, board);

    }

    /**
     * Player's king has no safe squares to move to
     * 
     * @param player
     *            Player who's King is checked
     * @param board
     *            ChessBoard
     * @return Boolean
     */
    boolean cantKingMoveOutOfCheck(Player player, Player opponent, ChessBoard board) {
        Location kingLocation = board.getPlayersKingLocation(player);
        ChessPiece kingPiece = board.getPieceFromBoardAt(kingLocation);
        
        Set<Location> possibleMoveLocations = new KingPiece(player.getColour())
                        .getPossibleMoveLocations(board, kingLocation);
        board.removePieceOnBoardAt(kingLocation);
        
        Set<Location> squaresUnderAttack = new HashSet<>();
        List<ChessPieceLocation> enemyLocations = board.getListOfPlayersPiecesOnTheBoard(opponent);
        
        for (Location loc : possibleMoveLocations) {
            for (ChessPieceLocation cpl : enemyLocations) {
                Move move = new Move(cpl.getLocation(), loc);
                ChessPiece piece = cpl.getPiece();
                if (piece.isValidMove(board, move)) {
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
     * Checks to see if the Player can capture the attacking ChessPiece Only if
     * the capture doesn't lead to the King still being checked
     * 
     * @param player
     *            Player
     * @param board
     *            ChessBoard
     * @return Boolean
     */
    boolean canAttackingPieceBeCaptured(Player player, Player opponent, ChessBoard board) {
        Location attackingPieceLocation = board.getTheLastMove().getEnd();
        List<ChessPieceLocation> myPieces = board.getListOfPlayersPiecesOnTheBoard(player);
        for (ChessPieceLocation cpl : myPieces) {
            ChessPiece piece = cpl.getPiece();
            Move move = new Move(cpl.getLocation(), attackingPieceLocation);
            if (piece.isValidMove(board, move)) {
                ReversibleMove checkMove = new ReversibleMove(board, move);
                checkMove.testMove();

                if (!isPlayersKingInCheck(player, opponent, board)) {
                    undoMove(checkMove);
                    return true;
                } else {
                    undoMove(checkMove);
                }

            }
        }
        return false;
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
    boolean canAttackingPieceBeBlocked(Player player, Player opponent, ChessBoard board) {
        Location attackingPieceLocation = board.getTheLastMove().getEnd();
        Location playersKingLocation = board.getPlayersKingLocation(player);
        List<ChessPieceLocation> myPieces = board.getListOfPlayersPiecesOnTheBoard(player);
        ChessPiece attacker = board.getPieceFromBoardAt(attackingPieceLocation);
        if (!attacker.canSlide()) {
            return false;
        } else {
            Move move = new Move(attackingPieceLocation, playersKingLocation);
            Set<Location> blockingSquares = getAllSquaresInAMove(move);
            for (Location blockingSquare : blockingSquares) {
                for (ChessPieceLocation cpl : myPieces) {
                    Move blockingMove = new Move(cpl.getLocation(), blockingSquare);
                    ChessPiece piece = cpl.getPiece();
                    if (!(piece instanceof KingPiece) && piece.isValidMove(board, blockingMove)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean isPlayersKingInCheck(Player player, Player opponent, ChessBoard board) {
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
    private Set<Location> getAllSquaresInAMove(Move move) {
        Set<Location> squares = new HashSet<>();
        int distance = Math.max(move.getAbsoluteDistanceX(), move.getAbsoluteDistanceY());
        int positionX = move.getStart().getLetter().getIndex();
        int positionY = move.getStart().getNumber();

        for (int i = 0; i < distance - 1; i++) {
            positionX = positionX - 1 * (int) Math.signum(move.getDistanceX());
            positionY = positionY - 1 * (int) Math.signum(move.getDistanceY());
            squares.add(new Location(Coordinate.values()[positionX], positionY));
        }

        return squares;
    }

}
