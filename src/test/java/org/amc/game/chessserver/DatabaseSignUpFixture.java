package org.amc.game.chessserver;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.amc.EntityManagerThreadLocal;

/**
 * 
 * @author Adrian McLaughlin
 * 
 *         Create Entities required to test the different elements of the spc
 *         package
 *
 */
public class DatabaseSignUpFixture {
    private EntityManager em;
    private EntityManagerFactory factory;

    public DatabaseSignUpFixture() {

    }

    public void setUpEntitiyManagerFactory() {
        factory = Persistence.createEntityManagerFactory("myDatabaseTest");
        EntityManagerThreadLocal.setEntityManagerFactory(factory);
        em = EntityManagerThreadLocal.getEntityManager();
    }
    
    public void setUp() {
        setUpEntitiyManagerFactory();
        deleteUserPlayerTables();
        EntityManagerThreadLocal.closeEntityManager();
        setUpEntitiyManagerFactory();   
    }
    
    public void tearDown() {
        deleteUserPlayerTables();
        EntityManagerThreadLocal.closeEntityManager();
    }
    
    private void deleteUserPlayerTables() {
        Query q1 = em.createNativeQuery("drop table users");
        Query q2 = em.createNativeQuery("drop table players");
        Query q3 = em.createNativeQuery("drop table authorities");
        boolean authoritiesExist = tableExists("authorities");
        boolean playersExist = tableExists("players");
        boolean usersExist = tableExists("users");
        
        em.getTransaction().begin();
        if(authoritiesExist){
            q3.executeUpdate();
        }
        
        if(playersExist){
            q2.executeUpdate();
        }
        
        if(usersExist){
            q1.executeUpdate();
        }
        em.getTransaction().commit();
    }

    public boolean tableExists(String tableName) {
        Query tableExists = em.createNativeQuery("SHOW TABLES");
        em.getTransaction().begin();
        tableExists.executeUpdate();
        List<?> tables = tableExists.getResultList();
        boolean result = false;
        if (tables.contains(tableName)) {
            result = true;
        }
        em.getTransaction().commit();

        return result;
    }
//    
//
//    // Todo Create method setUpSPCMeasurementTable
}
