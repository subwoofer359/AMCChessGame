package org.amc.game.chessserver.spring;

import org.amc.game.GameObserver;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.ObserverFactory;

public abstract class OfflineChessGameMessagerFactory implements ObserverFactory {

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return OfflineChessGameMessager.class;
    }

}
