package org.amc;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
@IdClass(Authorities.AuthoritiesPK.class)
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false),
            @JoinColumn(name = "username", referencedColumnName = "username", nullable = false, updatable = false) })
    private User user;

    @Column()
    private String authority;

    public Authorities() {
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public static class AuthoritiesPK implements Serializable {
        private static final long serialVersionUID = 1L;
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
            AuthoritiesPK other = (AuthoritiesPK) obj;
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
