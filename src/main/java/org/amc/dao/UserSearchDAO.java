package org.amc.dao;

import org.amc.DAOException;
import org.amc.User;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * DAO used for retrieving User information without retrieving sensitive details
 * 
 * @author Adrian Mclaughlin
 *
 */
public class UserSearchDAO extends DAO<User> {

    public UserSearchDAO() {
        super(User.class);
    }

    @SuppressWarnings("unchecked")
    public List<UserDetails> findUsers(Object value) throws DAOException {
        try {
            Query query = getEntityManager().createQuery(
                            "Select NEW org.amc.dao.UserDetails(x.userName, x.name) from " + getEntityClass().getSimpleName() + " x where x.userName LIKE ?1");
            query.setParameter(1, value);
            
            List<UserDetails> resultList = query.getResultList();
            return resultList;
        } catch (PersistenceException pe) {
            
            throw new DAOException(pe);
        }

    }
}
