package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;

public abstract class DatabaseUpdateListenerFactory implements ObserverFactory {
    
    public DatabaseUpdateListenerFactory() {
    }

    @Override
    public abstract GameObserver createObserver();

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return DatabaseUpdateListener.class;
    }
}
