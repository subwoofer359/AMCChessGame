package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;

/**
 * A factory for GameObserver instances
 * 
 * @author Adrian Mclaughlin
 *
 */
public interface ObserverFactory {

    /**
     * Creates a GameObserver instance
     * 
     * @return GameObserver
     */
    GameObserver createObserver();
    
    /**
     * @return Class of GameObserver for this factory
     */
    Class<? extends GameObserver> forObserverClass();

}
