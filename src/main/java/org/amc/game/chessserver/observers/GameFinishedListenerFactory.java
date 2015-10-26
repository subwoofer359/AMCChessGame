package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;


public class GameFinishedListenerFactory implements ObserverFactory {

    @Override
    public GameObserver createObserver() {
        return new GameFinishedListener();
    }

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return GameFinishedListener.class;
    }
    
    
}
