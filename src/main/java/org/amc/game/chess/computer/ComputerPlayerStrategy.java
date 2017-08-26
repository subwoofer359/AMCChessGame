package org.amc.game.chess.computer;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;

public interface ComputerPlayerStrategy {
	
	void makeMove(AbstractChessGame chessGame) throws IllegalMoveException;
	
	Move getNextMove(AbstractChessGame chessGame);
}
