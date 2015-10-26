package org.amc.game.chessserver.observers;

import org.amc.game.chessserver.ServerChessGame;

public class ObserverFactoryChainImpl extends ObserverFactoryChain {

    @Override
    public void addObserver(String observerList, ServerChessGame serverChessGame) {
        if(observerList != null && observerList.contains(getObserverFactory().forObserverClass().getSimpleName())) {
            getObserverFactory().createObserver().setGameToObserver(serverChessGame);
        }
        doChain(observerList, serverChessGame);
    }

}
