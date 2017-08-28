package org.amc.game.chess.computer;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chessserver.ComputerServerChessGame;
import org.apache.log4j.Logger;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.KingInCheck;
import org.amc.game.chess.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimplePlayerStrategy implements ComputerPlayerStrategy {
	
	static final String ERROR_NO_MOVE = "There are no moves available";
	
	private static final KingInCheck kingInCheck = KingInCheck.getInstance();
	
	private static final Logger LOGGER = Logger.getLogger(SimplePlayerStrategy.class); 

	final static ThreadLocalRandom random = ThreadLocalRandom.current(); 
	@Override
	public void makeMove(AbstractChessGame chessGame) throws IllegalMoveException {
		Move move = getNextMove(chessGame);
		chessGame.move(chessGame.getCurrentPlayer(), move);
	}

	@Override
	public Move getNextMove(AbstractChessGame chessGame) {
		List<Move> possibleMoves = new ArrayList<>();
		List<ChessPieceLocation> pieceLoc = chessGame.getChessBoard().getListOfPieces(chessGame.getCurrentPlayer());
		
		pieceLoc.forEach(piece -> {
			piece.getPiece().getPossibleMoveLocations(chessGame.getChessBoard(), piece.getLocation()).forEach(loc -> {
				possibleMoves.add(new Move(piece.getLocation(), loc));
			});
		});
		
		LOGGER.info("No of possible moves is " + possibleMoves.size());
		
		return getNextMove(chessGame, possibleMoves);
	}
	
	private Move getNextMove(AbstractChessGame chessGame, List<Move> possibleMoves) {
		if(possibleMoves.isEmpty()) {
			throw new AssertionError(ERROR_NO_MOVE);
		}
		
		int index = random.nextInt(0, possibleMoves.size());
		
		Move move = possibleMoves.get(index);
		
		ChessBoard testBoard = new ChessBoard(chessGame.getChessBoard());
		
		testBoard.move(move);
		
		if(kingInCheck.isPlayersKingInCheck(chessGame.getCurrentPlayer(), chessGame.getOpposingPlayer(chessGame.getCurrentPlayer()), testBoard)) {
			possibleMoves.remove(index);
			return getNextMove(chessGame, possibleMoves);
		}
		return move;
	};

}
