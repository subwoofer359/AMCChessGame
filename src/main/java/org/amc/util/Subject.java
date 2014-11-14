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
    void attachObserver(Observer observer);

    /**
     * notify Observers
     * 
     * @param message = Object passed to observers
     */
    void notifyObservers(Object message);

    /**
     * 
     * @param O Observer to be removed
     */
    void removeObserver(Observer observer);

}