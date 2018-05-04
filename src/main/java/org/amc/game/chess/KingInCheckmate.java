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
final class KingInCheckmate {

	private final KingInCheck kingIsChecked = KingInCheck.getInstance();
	private final ChessGamePlayer player;
	private final ChessGamePlayer opponent;
	private final ChessBoard board;
	private List<ChessPieceLocation> enemyLocations;
	private List<ChessPieceLocation> attackingPieces;
	private List<ChessPieceLocation> playersPieces;
	
	private Location kingLocation;
	private ChessPiece kingPiece;

	public KingInCheckmate(ChessGamePlayer player, ChessGamePlayer opponent, ChessBoard board) {
		this.player = player;
		this.opponent = opponent;
		this.board = board;
		
		findThePlayersKing();
	}
	
	private void findThePlayersKing() {
		this.kingLocation = board.getKingLocation(player);
		this.kingPiece = board.get(kingLocation);
	}
	
	private List<ChessPieceLocation> getEnemyLocations() {
		if (this.enemyLocations == null) {
			this.enemyLocations = board.getListOfPieces(opponent);
		}
		return this.enemyLocations;
	}
	
	/**
	 * Find all opponents pieces directly attacking the king
	 *
	 * @return List of ChessPieceLocation of attacking pieces
	 */
	List<ChessPieceLocation> getAttackingPieces() {
		if(this.attackingPieces == null) {
			this.attackingPieces = new ArrayList<>();
			for (ChessPieceLocation cpl : getEnemyLocations()) {
				Move move = new Move(cpl.getLocation(), kingLocation);
				if (cpl.getPiece().isValidMove(board, move)) {
					attackingPieces.add(cpl);
				}
			}
		} 
		return attackingPieces;
	}
	
	private List<ChessPieceLocation> getPlayerPieces() {
		if(this.playersPieces == null) {
			this.playersPieces = board.getListOfPieces(player);
		}
		return this.playersPieces;
	}

	/**
	 * Checks to see if the player's king is checkmated.
	 *
	 * @return Boolean true if checkmate has occurred
	 */
	boolean isCheckMate() {
		boolean result = isPlayersKingInCheck() && 
				isKingNotAbleToMoveOutOfCheck() && 
				canAttackingPieceNotBeCaptured() &&
				canAttackingPieceNotBeBlocked();
		
		clearLists();
		return result;
	}
	
	private void clearLists() {
		if(attackingPieces != null) {
			attackingPieces.clear();
		}
		if(enemyLocations != null) {
			enemyLocations.clear();
		}
		if(playersPieces != null) {
			playersPieces.clear();
		}
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
		return findAllSafeMoveLocations().isEmpty();
	}

	private Set<Location> findAllSafeMoveLocations() {
		ChessBoard testBoard = new ChessBoard(board);
		Set<Location> possibleMoveLocations = kingPiece.getPossibleMoveLocations(testBoard, kingLocation);
		
		//Remove the king
		testBoard.remove(kingLocation);
		Set<Location> squaresUnderAttack = getSquaresUnderAttack(testBoard, possibleMoveLocations);

		possibleMoveLocations.removeAll(squaresUnderAttack);
		return possibleMoveLocations;
	}
	
	/**
	 * Find Squares that are under attack by other Player Can use
	 * ChessPiece.isValidMove(ChessBoard, Move) on all pieces except PawnPiece.
	 * Must use PawnPiece.validMovement(Move) due to the Pawn special capturing
	 * move
	 * 
	 * @param possibleMoveLocations
	 *            Set of Squares to be checked
	 * @return Set of Squares that can be attacked
	 */
	private Set<Location> getSquaresUnderAttack(ChessBoard board, Set<Location> possibleMoveLocations) {
		Set<Location> squaresUnderAttack = new HashSet<>();
		for (Location location : possibleMoveLocations) {
			for (ChessPieceLocation enemyPiece : getEnemyLocations()) {
				Move move = new Move(enemyPiece.getLocation(), location);
				ChessPiece piece = enemyPiece.getPiece();

				ChessPiece occupyPiece = board.get(location);
				board.remove(location);

				if (canBeCapturedByPawn(piece, move)
						|| (!(piece instanceof PawnPiece) && piece.isValidMove(board, move))) {
					squaresUnderAttack.add(location);
					board.put(occupyPiece, location);
					break;
				}

				board.put(occupyPiece, location);
			}
		}
		return squaresUnderAttack;
	} 
	
	private boolean canBeCapturedByPawn(ChessPiece piece, Move move) {
		return piece instanceof PawnPiece && ((PawnPiece) piece).isMovingForwardDiagonallyOneSquare(move);
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
		Location attackingPieceLocation = getAttackingPieces().get(0).getLocation();
		for (ChessPieceLocation cpl : getPlayerPieces()) {
			ChessPiece piece = cpl.getPiece();
			Move move = new Move(cpl.getLocation(), attackingPieceLocation);
			if (piece.isValidMove(board, move) && willPlayerNotBeInCheck(move)) {
					return false;
			}
		}
		return true;
	}

	private boolean willPlayerNotBeInCheck(Move move) {
		ChessBoard testBoard = new ChessBoard(board);
		testBoard.move(move);
		return !isPlayersKingInCheck(testBoard);
	}

	private boolean isThereMoreThanOneAttacker() {
		return getAttackingPieces().size() != 1;
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
		Location attackingPieceLocation = getAttackingPieces().get(0).getLocation();
		ChessPiece attacker = board.get(attackingPieceLocation);

		Move move = new Move(attackingPieceLocation, kingLocation);
		Set<Location> blockingSquares = getAllSquaresInAMove(attacker, move);
		for (Location blockingSquare : blockingSquares) {
			for (ChessPieceLocation cpl : getPlayerPieces()) {
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
	 * 
	 * @param piece {@link ChessPiece}
	 *
	 * @param move Move
	 * @return Set of Locations
	 */
	private Set<Location> getAllSquaresInAMove(ChessPiece piece, Move move) {
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
}
