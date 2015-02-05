package org.amc.game.chess;

import org.amc.util.Observer;
import org.amc.util.Subject;

/**
 * Extends ChessGame and adds the observer design pattern
 * @author Adrian Mclaughlin
 *
 */
public class ObservableChessGame extends ChessGame implements Subject {

    public ObservableChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        super(board, playerOne, playerTwo);
    }

    @Override
    public void attachObserver(Observer observer) {
        getChessBoard().attachObserver(observer);
    }

    @Override
    public void notifyObservers(Object message) {
        getChessBoard().notifyObservers(message);

    }

    @Override
    public void removeObserver(Observer observer) {
        getChessBoard().removeObserver(observer);
    }

}
