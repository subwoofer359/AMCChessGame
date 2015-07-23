package org.amc.dao;

/**
 * Container for some of the User's information
 * @author Adrian Mclaughlin
 *
 */
public class UserDetails {
    private String userName;
    private String fullName;

    public UserDetails(String userName, String fullName) {
        this.fullName = fullName;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

}