package org.amc.util;

/**
 * @author Adrian McLaughlin
 * @version 1.1
 */

public interface MyObserver
{
	/**
	 * method called by subject to notify observer
	 * @param subject The Subject calling the update
	 * @param message Object passed by Subject
	 */
	public void update(Subject subject, Object message);
}
