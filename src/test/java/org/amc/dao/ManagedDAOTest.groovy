package org.amc.dao

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.ManagedDAOFactory.ManagedDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import groovy.transform.TypeChecked;

@TypeChecked
class ManagedDAOTest {

    ManagedDAO<User> dao;
    
    @Mock
    EntityManagerFactory entityManagerFactory;
    
    @Mock
    EntityManager entityManager;
    
    @Mock
    TypedQuery<User> query;
    
    @Mock
    EntityTransaction transaction;
    
    @Mock
    User user;
    
    DAOInterface<User> userDAO;
    
    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDAO = new DAO<>(User);
        dao = new ManagedDAO<User>(userDAO);
        dao.setEntityManagerFactory(entityManagerFactory);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(anyString(), eq(User))).thenReturn(query);
    }

    @Test
    void addEntityTest() {
        dao.addEntity(user);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void deleteTest() {
        dao.deleteEntity(user);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void findEntitiesTest() {
        dao.findEntities();
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void findEntitiesWithArgumentsTest() {
        dao.findEntities('userName', 'ted');
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void getEntityTest() {
        dao.getEntity(0);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void updateEntityTest() {
        dao.updateEntity(user);
        verify(entityManager, times(1)).close();
    }

    @Test
    void detachEntityTest() {
        dao.detachEntity(user);
        verify(entityManager, times(1)).close();
    }
    
    @Test
    void updateEntityTestThrowsExceptiopn() {
        try {
            when(entityManager.merge(user)).thenThrow(new PersistenceException('Test Exception'));
            dao.updateEntity(user);
            fail('DAOException not thrown');
        } catch(DAOException de) {
            // Caught exception;   
        } finally {
            verify(entityManager, times(1)).close();
        }
    }
    
    @Test
    void addEntityTestThrowsExceptiopn() {
        try {
            when(entityManager.persist(user)).thenThrow(new PersistenceException('Test Exception'));
            dao.addEntity(user);
            fail('DAOException not thrown');
        } catch(DAOException de) {
            // Caught exception;
        } finally {
            verify(entityManager, times(1)).close();
        }
    }
    
    @Test
    void deleteTestThrowsExceptiopn() {
        try {
            when(entityManager.remove(any())).thenThrow(new PersistenceException('Test Exception'));
            dao.deleteEntity(user);
            fail('DAOException not thrown');
        } catch(DAOException de) {
            // Caught exception;
        } finally {
            verify(entityManager, times(1)).close();
        }
    }
    
    @Test
    void findEntitiesTestThrowsExceptiopn() {
        try {
            when(query.getResultList()).thenThrow(new PersistenceException('Test Exception'));
            dao.findEntities();
            fail('DAOException not thrown');
        } catch(DAOException de) {
            // Caught exception;
        } finally {
            verify(entityManager, times(1)).close();
        }
    }
    
    @Test
    void findEntitiesWithArgumentsTestThrowsExceptiopn() {
        try {
            when(query.getResultList()).thenThrow(new PersistenceException('Test Exception'));
            dao.findEntities('userName', 'Ted');
            fail('DAOException not thrown');
        } catch(DAOException de) {
            // Caught exception;
        } finally {
            verify(entityManager, times(1)).close();
        }
    }
}
