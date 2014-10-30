package org.amc.util;

/**
 * @author Adrian McLaughlin
 * @version 1.1
 */

public interface Subject {
    /**
     * Method to register Observers
     * 
     * @param observer The Object observing
     */
    public abstract void attachObserver(MyObserver observer);

    /**
     * notify Observers
     * 
     * @param message = Object passed to observers
     */
    public abstract void notifyObservers(Object message);

    /**
     * 
     * @param O Observer to be removed
     */
    public void removeObserver(MyObserver observer);

}