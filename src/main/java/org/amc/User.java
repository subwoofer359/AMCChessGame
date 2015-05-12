package org.amc;

import org.amc.game.chess.Player;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@IdClass(User.UserId.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Id
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String userName;
    
    @Column(name = "password", length = 60, nullable = false)
    private char[] password;
    
    @Column(nullable = false)
    private boolean enabled = true;
   
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player")
    private Player player;
    
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authorities> authorities;

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

    public List<Authorities> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authorities> authorities) {
        this.authorities = authorities;
    }
    
    public static class UserId {
        public int id;
        public String userName;
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + ((userName == null) ? 0 : userName.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            UserId other = (UserId) obj;
            if (id != other.id)
                return false;
            if (userName == null) {
                if (other.userName != null)
                    return false;
            } else if (!userName.equals(other.userName))
                return false;
            return true;
        }
        
        
    }
    
    
}
