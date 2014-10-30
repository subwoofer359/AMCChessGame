package org.amc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adrian McLaughlin
 * @version 1.1
 */

public class DefaultSubject implements Subject {
    private List<MyObserver> observers;

    /**
     * 
     * Constructor for DefaultSubject.java
     */
    public DefaultSubject() {
        super();
        observers = new ArrayList<MyObserver>();

    }

    /**
     * @see org.amc.util.Subject#attachObserver(org.amc.util.MyObserver)
     */
    public void attachObserver(MyObserver observer) {
        observers.add(observer);
    }

    /**
     * @see org.amc.util.Subject#notifyObservers(java.lang.Object)
     */
    public void notifyObservers(Object message) {
        for (MyObserver observer:observers) {
            observer.update(this, message);
        }

    }

    public void removeObserver(MyObserver O) {
        observers.remove(O);
    }
}
