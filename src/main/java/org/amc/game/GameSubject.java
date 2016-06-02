package org.amc.game;

import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.PersistentCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class GameSubject implements Subject  {

    @PersistentCollection(elementCascade=CascadeType.ALL, elementType=GameObserver.class, fetch = FetchType.EAGER)
    @Externalizer("GameSubject.saveObservers")
    @Factory("GameSubject.loadObservers")
    @Column(length=1000)
    private List<Observer> observers;
    
    
    public GameSubject() {
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
        List<Observer> copy = new ArrayList<>(observers);
        for (Observer observer:copy) {
            observer.update(this, message);
        }
        copy.clear();
    }
    
    public int getNoOfObservers() {
        return this.observers.size();
    }

    public void removeObserver(Observer O) {
        observers.remove(O);
    }

    @Override
    public void removeAllObservers() {
        observers.clear();
    }
    
    public static String saveObservers(List<Observer> observers) {
        StringBuilder sb = new StringBuilder();
        Iterator<Observer> observer = observers.iterator();
        while(observer.hasNext()) {
            sb.append(observer.next().getClass().getSimpleName());
            if(observer.hasNext()) {
                sb.append("|");
            }
        }
        return sb.toString();
    }
    
    public static List<Observer> loadObservers(String observers) {
        return new ArrayList<Observer>();
    }
}
