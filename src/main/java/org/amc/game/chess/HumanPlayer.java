package org.amc.game.chess;

import java.io.Serializable;

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
public class HumanPlayer implements Player, Serializable {
    
    private static final long serialVersionUID = 9137658189552898690L;
    
    private static String DEFAULT_USERNAME = "no username";
    
    private static String DEFAULT_NAME = "no name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name", length = 50, nullable = false)
    private String name="name";
    
    @Column(name = "username" , length = 50, nullable = false)
    private String userName;
    
    public HumanPlayer(String name) {
        this();
    	this.name = name;
    }
    
    public HumanPlayer() {
        this.userName = DEFAULT_USERNAME;
        this.name = DEFAULT_NAME;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
        
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

	@Override
	public Class<?> getType() {
		return getClass();
	}
    
    
}
