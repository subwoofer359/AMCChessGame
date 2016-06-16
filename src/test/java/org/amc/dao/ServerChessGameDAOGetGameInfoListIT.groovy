package org.amc.dao

import org.amc.game.chess.HumanPlayer
import org.amc.game.chess.Player
import org.amc.game.chess.StandardChessGameFactory
import org.amc.game.chessserver.AbstractServerChessGame
import org.amc.game.chessserver.DatabaseFixture
import org.amc.game.chessserver.OneViewServerChessGame
import org.amc.game.chessserver.TwoViewServerChessGame
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import javax.persistence.EntityManager

class ServerChessGameDAOGetGameInfoListIT {
    
    private static DatabaseFixture fixture = new DatabaseFixture();
    private ServerChessGameDatabaseEntityFixture scgDbEntity;
    EntityManager entityManager;
    SCGDAOInterface dao;
    Player player;
    Player opponent;
    Player otherPlayer;
    DAOInterface<Player> playerDAO;
    static final Integer NO_OF_ENTRIES = 10;

    @BeforeClass
    static void setUpBeforeClass() throws Exception {
        fixture.setUp();      
    }
    
    @Before
    void setup() {
        fixture.clearTables();
        fixture.addUsers();
        scgDbEntity = new ServerChessGameDatabaseEntityFixture(fixture.newEntityManager, NO_OF_ENTRIES);
            
        entityManager = scgDbEntity.entityManager;
        dao = new ServerChessGameDAO();
        dao.setEntityManager(entityManager);
        playerDAO = new DAO<Player>(HumanPlayer);
        playerDAO.setEntityManager(entityManager);
        
        player = playerDAO.findEntities("userName", "laura")?.get(0);
        opponent = playerDAO.findEntities("userName", "nobby")?.get(0);
        otherPlayer = playerDAO.findEntities("userName", "stephen")?.get(0);
    }
    
    @AfterClass
    static void tearDownAfterClass() {
        fixture.tearDown();
    }
    
    @Test
    void testGetServerChessGamesGivenPlayer() {
        def games = dao.getGameInfoForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 1;
    }
    

    @Test
    void testGetServerChessGamesGivenPlayerNoGames() {
        def games = dao.getGameInfoForPlayer(otherPlayer);
        assert games?.isEmpty();
    }
    

    @Test
    void testGetServerChessGamesGivenPlayerInOpposition() {
        final def GAME_UID = 12324L;
        AbstractServerChessGame s = new TwoViewServerChessGame(GAME_UID, opponent);
        
        s.setChessGameFactory(new StandardChessGameFactory());
        
        s.addOpponent(player);
        scgDbEntity.addServerChessGameToDataBase(s);
        def games = dao.getGameInfoForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 2;
    }
    
    @Test
    void testGetServerChessGamesGivenVirtualPlayer() {
        final def GAME_UID = 412324L;
        AbstractServerChessGame s = new OneViewServerChessGame(GAME_UID, player);
        
        s.setChessGameFactory(new StandardChessGameFactory());
        Player virtualPlayer = new HumanPlayer("robot");
        
        s.addOpponent(virtualPlayer);
        scgDbEntity.addServerChessGameToDataBase(s);
        
        def games = dao.getGameInfoForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 2;
    }
    
}
