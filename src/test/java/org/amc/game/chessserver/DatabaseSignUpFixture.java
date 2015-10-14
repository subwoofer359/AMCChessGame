package org.amc.game.chessserver;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;

/**
 * 
 * @author Adrian McLaughlin
 * 
 *         Create Entities required to test the different elements of the spc
 *         package
 *
 */
public class DatabaseSignUpFixture {
    private static final Logger logger = Logger.getLogger(DatabaseSignUpFixture.class);
    
    private EntityManager em;
    private EntityManagerFactory factory;
    private static final String[] userNames = { "nobby", "laura", "stephen" };
    private static final String[] fullNames = { "Nobby Squeal", "Laura O'Neill", "Stephen Moran" };
    private static final String[] emailAddresses = {"nobby@adrianmclaughlin.ie", "laura@adrianmclaughlin.ie",
                    "stephen@adrianmclaughlin.ie" };
    private static final String password = "C4096cr";
    
    private static final String[] TABLES= { "users", "players", "authorities", "serverChessGames", 
        "chessGames", "chessBoards" };

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
        addUsers();
    }
    
    public void tearDown() {
        deleteUserPlayerTables();
        EntityManagerThreadLocal.closeEntityManager();
    }
    
    private void deleteUserPlayerTables() {
        if(em.getTransaction().isActive()) {
            em.joinTransaction();
        } else
        {
            em.getTransaction().begin();
        }
        for(String table : TABLES) {
            if(tableExists(table)) {
                Query q1 = em.createNativeQuery("drop table " + table);
                q1.executeUpdate();
            }
        }
        em.getTransaction().commit();
    }
    
    private void addUsers() {
        for(int i = 0; i < fullNames.length; i++) {
            User user = new User();
            user.setName(fullNames[i]);
            user.setUserName(userNames[i]);
            user.setEmailAddress(emailAddresses[i]);
            user.setPassword(password.toCharArray());
            DAO<User> userDAO = new DAO<>(User.class);
            Player player = new HumanPlayer(user.getName());
            player.setUserName(user.getUserName());
            user.setPlayer(player);
            try {
                userDAO.addEntity(user);
            } catch (DAOException e) {
                logger.error("user:" + user.getName()+" not added to datbase");
                e.printStackTrace();
            }
        }
    }

    public boolean tableExists(String tableName) {
        Query tableExists = em.createNativeQuery("SHOW TABLES");
        tableExists.executeUpdate();
        List<?> tables = tableExists.getResultList();
        boolean result = false;
        if (tables.contains(tableName)) {
            result = true;
        }
        return result;
    }

}
