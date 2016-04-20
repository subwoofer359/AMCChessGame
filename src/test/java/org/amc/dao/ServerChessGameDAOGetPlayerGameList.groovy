package org.amc.dao

import static org.junit.Assert.*;

import org.amc.game.chess.Player;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.OneViewServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ServerChessGameDAOGetPlayerGameList {
    
    private static DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    private static ServerChessGameTestDatabaseEntity scgDbEntity;
    ServerChessGameDAO dao;
    Player player;
    Player opponent;
    Player otherPlayer;
    DAO<Player> playerDAO;
    static final Integer NO_OF_ENTRIES = 10;

    @BeforeClass
    static void setUpBeforeClass() throws Exception {
        fixture.setUp();
        scgDbEntity = new ServerChessGameTestDatabaseEntity(NO_OF_ENTRIES);
    }
    
    @Before
    void setup() {
        
        
        dao = new ServerChessGameDAO();
        playerDAO = new DAO<Player>(HumanPlayer);
        
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
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 1;
    }
    
    @Test
    void testGetServerChessGamesGivenPlayerNoGames() {
        def games = dao.getGamesForPlayer(otherPlayer);
        assert games?.isEmpty() == true;
    }
    
    @Test
    void testGetServerChessGamesGivenPlayerInOpposition() {
        final def GAME_UID = 12324L;
        AbstractServerChessGame s = new TwoViewServerChessGame(GAME_UID, opponent);
        
        s.setChessGameFactory(new StandardChessGameFactory());
        
        s.addOpponent(player);
        scgDbEntity.addServerChessGameToDataBase(s);
        def games = dao.getGamesForPlayer(player);
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
        
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 2;
    }
    
}
