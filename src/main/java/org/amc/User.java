package org.amc;

import org.amc.game.chess.Player;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents a User to be authenicated by Spring Security
 * There is one to one relationship between User and Player
 * @author adrian
 *
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String userName;
    
    @Column(name = "password", length = 50, nullable = false)
    private char[] password;
    
    @Column
    private boolean enabled = true;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Player player;

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public Player getPlayer() {
        return player;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
