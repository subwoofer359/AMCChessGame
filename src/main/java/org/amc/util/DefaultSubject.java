package org.amc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adrian McLaughlin
 * @version 1.1
 */

public class DefaultSubject implements Subject {
    private final List<Observer> observers;

    /**
     * 
     * Constructor for DefaultSubject.java
     */
    public DefaultSubject() {
        super();
        observers = new ArrayList<Observer>();

    }

    /**
     * @see org.amc.util.Subject#attachObserver(org.amc.util.MyObserver)
     */
    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * @see org.amc.util.Subject#notifyObservers(java.lang.Object)
     */
    public void notifyObservers(Object message) {
        for (Observer observer:observers) {
            observer.update(this, message);
        }

    }

    public void removeObserver(Observer O) {
        observers.remove(O);
    }
}
