package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;

public class GameStateListenerFactory extends MessagingGameObserverFactory { 

    @Override
    public GameObserver createObserver() {
        return new GameStateListener(getMessagingTemplate());
    }

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return GameStateListener.class;
    }
    
    
}
