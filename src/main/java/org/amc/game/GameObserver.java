package org.amc.game;

import org.amc.util.Observer;

public abstract class GameObserver implements Observer {
    
    /**
     *  Register this observer on the Subject
     * @param subject Game the Observer wants to receive updates from
     */
    public void setGameToObserver(GameSubject subject) {
        subject.attachObserver(this);
    }
}
