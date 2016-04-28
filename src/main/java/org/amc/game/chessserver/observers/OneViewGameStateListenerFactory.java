package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;

public abstract class OneViewGameStateListenerFactory extends MessagingGameObserverFactory {

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return OneViewGameStateListener.class;
    }
}
