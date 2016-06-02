package org.amc.dao;

import org.amc.DAOException;
import org.amc.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ManagedUserDAOFactory {
    
    private EntityManagerFactory emFactory;
    
    public DAOInterface<?> getUserDAO() {
        EntityManager entityManager = emFactory.createEntityManager();
        DAOInterface<User> userDAO = new DAO<>(User.class);
        userDAO.setEntityManager(entityManager);
        return userDAO;
    }
    
    static class ManagedDAO<T> implements DAOInterface<T> {
        private EntityManagerFactory entityManagerFactory;
        private EntityManager entityManager;
        private DAOInterface<T> dao;
        
        public ManagedDAO(DAOInterface<T> dao) {
            this.dao = dao;
            
        }

        public void addEntity(T entity) throws DAOException {
            dao.setEntityManager(getEntityManager());
            dao.addEntity(entity);
            entityManager.close();
        }


        public void deleteEntity(T entity) throws DAOException {
            dao.setEntityManager(getEntityManager());
            dao.deleteEntity(entity);
            entityManager.close();
        }

        public List<T> findEntities() throws DAOException {
            dao.setEntityManager(getEntityManager());
            List<T> entityList = dao.findEntities();
            entityManager.close();
            return entityList;
        }

        public List<T> findEntities(String col, Object value) throws DAOException {
            dao.setEntityManager(getEntityManager());
            List<T> entityList = dao.findEntities(col, value);
            entityManager.close();
            return entityList;
        }

        public T getEntity(int id) throws DAOException {
            dao.setEntityManager(getEntityManager());
            T entity = dao.getEntity(id);
            entityManager.close();
            return entity;
        }

        public T updateEntity(T entity) throws DAOException {
            dao.setEntityManager(getEntityManager());
            T entityTemp = dao.updateEntity(entity);
            entityManager.close();
            return entityTemp;
        }

        public void detachEntity(T entity) {
            dao.setEntityManager(getEntityManager());
            dao.detachEntity(entity);
            entityManager.close();
        }
        
        public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        public EntityManager getEntityManager() {
            entityManager = entityManagerFactory.createEntityManager();
            dao.setEntityManager(entityManager);
            return dao.getEntityManager();
        }

        public void setEntityManager(EntityManager entityManager) {
            dao.setEntityManager(entityManager);
        }

        @Override
        public Class<?> getEntityClass() {
            return dao.getEntityClass();
        }
        
        
    }
       
}
