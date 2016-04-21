package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.Authorities;
import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class UserPlayerIT {

    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private static final String name = "adrian mclaughlin";
    private static final String userName = "adrian";
    private static final String password = "password";
    private static final String ROLE = "ROLE_USER";
    
    private DAO<User> userDAO;
    private DAO<Player> playerDAO;
    private DAO<Authorities> authoritiesDAO;
    private Player player;
    private User user;
    private Authorities authorities;
    
    @Before
    public void setUp() {
        this.signUpfixture.setUp();
        userDAO = new DAO<>(User.class);
        playerDAO = new DAO<>(HumanPlayer.class);
        authoritiesDAO = new DAO<>(Authorities.class);
        
        player = new HumanPlayer(name);
        player.setUserName(userName);
        
        user = new User();
        user.setName(name);
        user.setPassword(password.toCharArray());
        user.setUserName(userName);
        user.setPlayer(player);
        
        
        authorities = new Authorities();
        authorities.setAuthority(ROLE);
        authorities.setUser(user);
        
        user.setAuthorities(Arrays.asList(authorities));
    }
    
    @After
    public void tearDown() {
        this.signUpfixture.tearDown();
    }
    
    @Test
    public void testaddEntity() throws DAOException{ 
        userDAO.addEntity(user);
        
        assertEquals(user, userDAO.getEntity(user.getId()));
        assertEquals(player, playerDAO.getEntity(player.getId()));
        assertEquals(authorities, authoritiesDAO.getEntity(authorities.getId()));
    }
    
    @Test
    public void testDeleteEntity() throws DAOException {
        userDAO.addEntity(user);
        assertEquals(user, userDAO.getEntity(user.getId()));
        
        userDAO.deleteEntity(user);
        
        assertEquals(true, userDAO.findEntities("userName", userName).isEmpty());
        assertEquals(true, playerDAO.findEntities("userName", userName).isEmpty());
        assertEquals(true, authoritiesDAO.findEntities("user", user).isEmpty());
    }
}
