package org.amc.game.chess;

/**
 * Represents a Human player in a game of chess
 * 
 * @author adrian
 *
 */
public class HumanPlayer implements Player {
    private final String name;
    private int uid;

    public HumanPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public int getUid() {
        return uid;
    }

    @Override
    public void setUid(int uid) {
        this.uid = uid;
        
    }
    
    
}
