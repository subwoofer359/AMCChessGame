package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;


public abstract class GameFinishedListenerFactory implements ObserverFactory {

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return GameFinishedListener.class;
    }
    
    
}
