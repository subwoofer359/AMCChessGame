package org.amc.util;

/**
 * @author Adrian McLaughlin
 * @version 1.1
 */

public interface Observer
{
	/**
	 * method called by subject to notify observer
	 * @param subject The Subject calling the update
	 * @param message Object passed by Subject
	 */
	void update(Subject subject, Object message);
}
