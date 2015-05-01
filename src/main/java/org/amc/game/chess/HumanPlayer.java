package org.amc.game.chess;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a Human player in a game of chess
 * 
 * @author adrian
 *
 */
@Entity
@Table(name="players")
public class HumanPlayer implements Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Column(name = "username" , length =50, unique = true, nullable = false)
    private String userName;
    
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

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
    
}
