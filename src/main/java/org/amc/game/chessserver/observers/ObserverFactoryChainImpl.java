package org.amc.game.chessserver.observers;

import org.amc.game.chessserver.AbstractServerChessGame;

public class ObserverFactoryChainImpl extends ObserverFactoryChain {

    /**
     * @see ObserverFactoryChain#addObserver(String, AbstractServerChessGame)
     */
    @Override
    public void addObserver(String observerList, AbstractServerChessGame serverChessGame) {
        if(observerList != null && observerList.contains(getObserverFactory().forObserverClass().getSimpleName())) {
            getObserverFactory().createObserver().setGameToObserver(serverChessGame);
        }
        doChain(observerList, serverChessGame);
    }

}
