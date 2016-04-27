package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.AfterClass
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.annotation.Repeat;

import javax.persistence.EntityManagerFactory;

class DatabaseSignUpFixtureIT {
    
    static DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    def FIRST = 0;
    def userNames = DatabaseSignUpFixture.userNames;
	def userDAO;
	def playerDAO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        fixture.setUp();
    }
	
	@Before
	public void setUp() throws Exception {
		userDAO = new DAO<>(User);
		playerDAO = new DAO<>(HumanPlayer);
	}

    @AfterClass
    public static void tearDown() throws Exception {
        fixture.tearDown();
    }

    @Test
    public void testUsers() {
        userNames.eachWithIndex(
            { item, index ->
                def userName = item;
                
                List users = userDAO.findEntities("userName", userName);
              
                assert users?.size() == 1;
                
                User user = users[FIRST];
                
                assert user?.authorities[FIRST]?.authority == "ROLE_USER";
                
                assert user?.emailAddress == DatabaseSignUpFixture.emailAddresses[index];
                
                assert user?.name == DatabaseSignUpFixture.fullNames[index];
                
                assert String.valueOf(user?.password) == DatabaseSignUpFixture.password;
                
                assert user?.player?.userName == userName;
                

            }
            );
    }
    
    @Test
    public void testPlayers() {
        def dao = new DAO(HumanPlayer.class);
        
        userNames.eachWithIndex ( 
            { item, index -> 
                def userName = item;
                List players = dao.findEntities("userName", userName);
                
                assert players?.size() == 1;
                
                Player player = players[FIRST];
                
                assert player?.userName == userName;
                
                assert player?.name == DatabaseSignUpFixture.fullNames[index];
                
                assert player?.id != 0;
            }
            );
    }
    
    @Test
    public void testGetEntityManager() {
        assert fixture.getEntityManagerFactory() instanceof EntityManagerFactory;
        assert fixture.getEntityManagerFactory().isOpen() == true;
    }
	
	@Test
	public void clearTables() {
		fixture.clearTables();
		List users = userDAO.findEntities();
		assert users.size() == 0;
		List players = playerDAO.findEntities();
		assert players.size() == 0;
		setUpBeforeClass();
	}

}
