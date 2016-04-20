package org.amc.game.chessserver;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import org.amc.Authorities;
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
 
    private EntityManagerFactory factory;
    static final String[] userNames = { "nobby", "laura", "stephen" };
    static final String[] fullNames = { "Nobby Squeal", "Laura O'Neill", "Stephen Moran" };
    static final String[] emailAddresses = {"subwoofer359@gmail.com", "laura@adrianmclaughlin.ie",
                    "subwoofer359@gmail.com" };
    static final String password = "C4096cr";

    public DatabaseSignUpFixture() {

    }

    public void setUpEntitiyManagerFactory() {
        factory = Persistence.createEntityManagerFactory("myDatabaseTest");
        EntityManagerThreadLocal.setEntityManagerFactory(factory);
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }
    
    public void setUp() {
        setUpEntitiyManagerFactory();
        addUsers();
    }
    
    public void tearDown() {
        EntityManagerThreadLocal.closeEntityManager();
        try {
            DriverManager.getConnection("jdbc:derby:memory:amcchessgametest;drop=true");
        } catch(SQLException sqle) {
            logger.info(sqle);
        }
    }
    
    private void addUsers() {
        for(int i = 0; i < fullNames.length; i++) {
            User user = getUser(i);
            addAuthortities(user);
            addPlayer(user);
            createUserEntry(user);
        }
    }
    
    private User getUser(int index) {
        User user = new User();
        
        user.setName(fullNames[index]);
        user.setUserName(userNames[index]);
        user.setEmailAddress(emailAddresses[index]);
        user.setPassword(password.toCharArray());
        
        return user;
    }
    
    private void addAuthortities(User user) {
        List<Authorities> authorities = new ArrayList<>();
        Authorities authority = new Authorities();
        authority.setAuthority("ROLE_USER");
        authority.setUser(user);
        authorities.add(authority);
        user.setAuthorities(authorities);
    }

    private void addPlayer(User user) {
        Player player = new HumanPlayer(user.getName());
        player.setUserName(user.getUserName());
        user.setPlayer(player);
    }
    
    private void createUserEntry(User user) {
        DAO<User> userDAO = new DAO<>(User.class);
        try {
            userDAO.addEntity(user);
        } catch (DAOException e) {
            logger.error("user:" + user.getName()+" not added to datbase");
            e.printStackTrace();
        }
    } 
}
