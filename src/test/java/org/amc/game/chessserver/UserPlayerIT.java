package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.Authorities;
import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.dao.DAOInterface;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class UserPlayerIT {

    private DatabaseFixture fixture = new DatabaseFixture();
    private static final String name = "adrian mclaughlin";
    private static final String userName = "adrian";
    private static final String password = "password";
    private static final String ROLE = "ROLE_USER";
    
    private DAOInterface<User> userDAO;
    private DAOInterface<HumanPlayer> playerDAO;
    private DAOInterface<Authorities> authoritiesDAO;
    private Player player;
    private User user;
    private Authorities authorities;
    
    @Before
    public void setUp() {
        this.fixture.setUp();
        userDAO = new DAO<>(User.class);
        userDAO.setEntityManager(fixture.getNewEntityManager());
        playerDAO = new DAO<>(HumanPlayer.class);
        playerDAO.setEntityManager(fixture.getNewEntityManager());
        authoritiesDAO = new DAO<>(Authorities.class);
        authoritiesDAO.setEntityManager(fixture.getNewEntityManager());
        
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
        this.fixture.tearDown();
    }
    
    @Test
    public void testaddEntity() throws DAOException{ 
        userDAO.addEntity(user);
        
        assertEquals(user, userDAO.getEntity(user.getId()));
        assertTrue("Players should be the same", ComparePlayers.isSamePlayer(
        		player, playerDAO.getEntity(player.getId())));
        
        final Authorities auth = authoritiesDAO.getEntity(authorities.getId());
        assertEquals("IDs' should be the same", authorities.getId(), auth.getId());
        assertEquals(authorities.getAuthority(), auth.getAuthority());
    }
    
    @Test
    public void testDeleteEntity() throws DAOException {
        userDAO.addEntity(user);
        assertEquals(user, userDAO.getEntity(user.getId()));
        
        userDAO.deleteEntity(user);
        
        assertTrue("Should be no users", userDAO.findEntities("userName", userName).isEmpty());
        assertTrue("Should be no players", playerDAO.findEntities("userName", userName).isEmpty());
        assertTrue("Should be no authorities", authoritiesDAO.findEntities("user", user).isEmpty());
    }
}
