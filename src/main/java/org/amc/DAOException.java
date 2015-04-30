package org.amc;

/**
 * DAOException is raised by the DAO layer when there's are problems with JPA
 * 
 * @author Adrian Mclaughlin
 * @version 1
 *
 */
public class DAOException extends Exception {
    private static final long serialVersionUID = -1428358886074010054L;

    public DAOException() {
        super();
    }

    public DAOException(Throwable throwable) {
        super(throwable);
    }

    public DAOException(String message) {
        super(message);
    }
}
