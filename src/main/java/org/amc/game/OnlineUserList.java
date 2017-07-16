package org.amc.game;


import java.util.HashSet;
import java.util.Set;

import org.amc.User;
/**
 * A list of online users
 * 
 * @author adrian
 *
 */
public final class OnlineUserList {
	
	private Set<User> userSet;
	
	public OnlineUserList() {
		userSet = new HashSet<User>();
	}
	
	public void addUser(User user) {
		synchronized (this) {
			userSet.add(user);
		}
	}
	
	public void removeUser(User user) {
		synchronized (this) {
			userSet.remove(user);
		}
	}
	
	public boolean isOnline(User user) {
		synchronized (this) {
			return userSet.contains(user);
		}
	}

}
