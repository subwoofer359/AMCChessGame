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
    
    static DatabaseFixture fixture = new DatabaseFixture();
    def FIRST = 0;
    def userNames = DatabaseFixture.userNames;
	def userDAO;
	def playerDAO;

    @BeforeClass
    static void setUpBeforeClass() {
        fixture.setUp();
    }
	
	@Before
	void setUp() {
		userDAO = new DAO<>(User);
        userDAO.entityManager = fixture.entityManager;
		playerDAO = new DAO<>(HumanPlayer);
        playerDAO.entityManager = fixture.entityManager;
	}

    @AfterClass
    static void tearDown() {
        fixture.tearDown();
    }

    @Test
    void testUsers() {
        userNames.eachWithIndex(
            { item, index ->
                def userName = item;
                
                List users = userDAO.findEntities("userName", userName);
              
                assert users?.size() == 1;
                
                User user = users[FIRST];
                
                assert user?.authorities[FIRST]?.authority == "ROLE_USER";
                
                assert user?.emailAddress == DatabaseFixture.emailAddresses[index];
                
                assert user?.name == DatabaseFixture.fullNames[index];
                
                assert String.valueOf(user?.password) == DatabaseFixture.password;
                
                assert user?.player?.userName == userName;
                

            }
        );
    }
    
    @Test
    void testPlayers() {
        def dao = new DAO(HumanPlayer.class);
        dao.entityManager = fixture.entityManager;
        
        userNames.eachWithIndex ( 
            { item, index -> 
                def userName = item;
                List players = dao.findEntities("userName", userName);
                
                assert players?.size() == 1;
                
                Player player = players[FIRST];
                
                assert player?.userName == userName;
                assert player?.name == DatabaseFixture.fullNames[index];        
                assert player?.id != 0;
            }
        );
    }
    
    @Test
    void testGetEntityManager() {
        assert fixture.getEntityManagerFactory() instanceof EntityManagerFactory;
        assert fixture.getEntityManagerFactory().isOpen() == true;
    }
	
	@Test
	void clearTables() {
		fixture.clearTables();
		List users = userDAO.findEntities();
		
        assert users.size() == 0;
		List players = playerDAO.findEntities();
		
        assert players.size() == 0;
		setUpBeforeClass();
	}
}
