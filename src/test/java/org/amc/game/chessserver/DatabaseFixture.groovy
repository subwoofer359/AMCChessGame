package org.amc.game.chessserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.amc.Authorities;
import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.User.AuthorityComparator;

/**
 * 
 * @author Adrian McLaughlin
 * 
 *         Create Entities required to test the different elements of the spc
 *         package
 *
 */
class DatabaseFixture {
    private static final Logger logger = Logger.getLogger(DatabaseFixture);
    
    EntityManagerFactory factory;
    
    EntityManager entityManager;
    
    def entityManagerList;
    
    static final def userNames = ['nobby', 'laura', 'stephen'];
    static final def fullNames = ['Nobby Squeal', 'Laura O\'Neill', 'Stephen Moran'];
    static final def emailAddresses = ['subwoofer359@gmail.com', 'laura@adrianmclaughlin.ie',
                    'subwoofer359@gmail.com'];
    static final def password = 'C4096cr';
                

    public void setUpEntitiyManagerFactory() {
        factory = Persistence.createEntityManagerFactory("myDatabaseTest");
        entityManager = factory.createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public void setUp() {
        setUpEntitiyManagerFactory();
        entityManagerList = [];
        
        addUsers();
    }

    public void tearDown() {
        closeEntityManagers();
        try {
            DriverManager.getConnection("jdbc:derby:memory:amcchessgametest;drop=true");
        } catch(SQLException sqle) {
            logger.info(sqle);
        }
    }
    
    private void closeEntityManagers() {
        entityManagerList.each {
            if(it?.isOpen()) {
                it.close();
            }
        };
    }
    
    public EntityManager getNewEntityManager() {
        def em  = factory.createEntityManager();
        entityManagerList.add(em);
        return em;
    }

    public void clearTables() throws SQLException {
        Connection c = entityManager.unwrap(Connection.class);
        List<String> tables = new ArrayList<>();
        try {
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("SELECT * FROM sys.systables");
            while(r.next()) {
                String tableName = r.getString(2);
                String tableType = r.getString(3);
                if("T".equals(tableType)) {
                    tables.add(tableName);
                }
            }

            for(String tableName : tables) {
                s.execute("DELETE FROM " + tableName);
            }
        }
        finally {
            
        }
    }
    
    void addUsers() {
        fullNames.eachWithIndex {name, index ->
            User user = createUser(index);
            addAuthorities(user);
            addPlayer(user);
            createUserEntry(user);
        };
    }
    
    private User createUser(int index) {
        User user = new User(
            name: fullNames[index],
            userName: userNames[index],
            emailAddress: emailAddresses[index],
            password: password.toCharArray()
        );       
        return user;
    }
    
    private void addAuthorities(User user) {
        List<Authorities> authorities = [];
        Authorities authority = new Authorities(authority: 'ROLE_USER');
        authority.user = user;
        authorities.add(authority);
        user.authorities = authorities;
    }
    
    private void addPlayer(User user) {
        Player player = new HumanPlayer(userName: user.userName, name: user.name);
        user.player = player;
    }
    
    private void createUserEntry(User user) {
        def userDAO = new DAO(User.class);
        userDAO.entityManager = entityManager;
        try {
             userDAO.addEntity(user);  
        } catch (DAOException e) {
            logger.error("user: ${user.name} not added to datbase");
        }
        
    }
}
