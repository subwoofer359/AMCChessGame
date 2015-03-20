package org.amc.game.chess;

import org.amc.util.Observer;
import org.amc.util.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends ChessGame and adds the observer design pattern
 * @author Adrian Mclaughlin
 *
 */
public class ObservableChessGame extends ChessGame implements Subject, Observer {
    private List<Observer> observers;
    
    public ObservableChessGame(ChessBoard board, Player playerOne, Player playerTwo) {
        super(board, playerOne, playerTwo);
        observers = new ArrayList<Observer>();
        board.attachObserver(this);
        
    }

    @Override
    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Object message) {
        for (Observer observer:observers) {
            observer.update(this, message);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update(Subject subject, Object message) {
        if(message instanceof ChessBoard){
            notifyObservers(this);
        }
    }
}
