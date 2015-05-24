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
	
	public final void addUser(User user) {
		synchronized (this) {
			userSet.add(user);
		}
	}
	
	public final void removeUser(User user) {
		synchronized (this) {
			userSet.remove(user);
		}
	}
	
	public final boolean isOnline(User user) {
		synchronized (this) {
			return userSet.contains(user);
		}
	}

}
