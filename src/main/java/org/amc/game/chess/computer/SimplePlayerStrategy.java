package org.amc.game.chess.computer;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.EmptyMove;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.NoChessPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SimplePlayerStrategy implements ComputerPlayerStrategy {
	
	final static int NO_OF_TRIES = 50;

	final static ThreadLocalRandom random = ThreadLocalRandom.current(); 
	@Override
	public void makeMove(AbstractChessGame chessGame) throws IllegalMoveException {
		Move move = getNextMove(chessGame);
		chessGame.move(chessGame.getCurrentPlayer(), move);
	}

	@Override
	public Move getNextMove(AbstractChessGame chessGame) {
		return getNextMove(chessGame, NO_OF_TRIES);
	}
	
	public Move getNextMove(AbstractChessGame chessGame, int index) {
		ChessPieceLocation pieceLocation = getChessPiece(chessGame);
		
		if(NoChessPiece.NO_CHESSPIECE == pieceLocation.getPiece()) {
			return EmptyMove.EMPTY_MOVE;
		}
		
		ChessPiece piece = pieceLocation.getPiece();
		
		Location startOfMove = pieceLocation.getLocation();
		
		Set<Location> locations = piece.getPossibleMoveLocations(chessGame.getChessBoard(), startOfMove);
		
		if(locations.isEmpty()) {
			if (index > 0 ) {
				return getNextMove(chessGame, index -1);
			} else {
				throw new AssertionError("No move can be found");
			}
		}
		
		return new Move(startOfMove, new ArrayList<>(locations).get(random.nextInt(0, locations.size())));
	}
	
	ChessPieceLocation getChessPiece(AbstractChessGame chessGame) {
		List<ChessPieceLocation> pieceLoc = chessGame.getChessBoard().getListOfPieces(chessGame.getCurrentPlayer());
		
		if(pieceLoc.isEmpty()) {
			return new ChessBoard.ChessPieceLocation(NoChessPiece.NO_CHESSPIECE, new Location("A1"));
		}
		
		return pieceLoc.get(random.nextInt(0, pieceLoc.size()));
	}

}
