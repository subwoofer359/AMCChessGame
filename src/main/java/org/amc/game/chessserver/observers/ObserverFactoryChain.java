package org.amc.game.chessserver.observers;

import org.amc.game.chessserver.ServerChessGame;

public abstract class ObserverFactoryChain {

    private ObserverFactoryChain successor;
    
    private ObserverFactory factory;
    
    /**
     * Set next link in the chain
     * 
     * @param successor {@link ObserverFactoryChain}
     */
    public void setSuccessor(ObserverFactoryChain successor) {
        this.successor = successor;
    }
    
    /**
     * Set the ObserverFactory to create the required GameObserver for this instance
     * @param factory {@link ObserverFactory}
     */
    public void setObserverFactory(ObserverFactory factory) {
        this.factory = factory;
    }
    
    
    protected ObserverFactoryChain getSuccessor() {
        return successor;
    }
    
    protected ObserverFactory getObserverFactory() {
        return factory;
    }
    
    /**
     * Pass control to the next link in the ObserverFactoryChain
     * 
     * @param observerList String A list of GameObservers
     * @param serverChessGame {@link ServerChessGame} instance to which to attach the observers to 
     */
    public final void doChain(String observerList, ServerChessGame serverChessGame) {
        if(getSuccessor() != null) {
            getSuccessor().addObserver(observerList, serverChessGame);
        }
    }
    
    /**
     * Checks the observerList to see if to add a GameObserver
     * 
     * @param observerList String list of GameObservers
     * @param serverChessGame {@link ServerChessGame}
     */
    public abstract void addObserver(String observerList, ServerChessGame serverChessGame);
  
}
