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
import org.junit.Before;
import org.junit.Test;

class ServerChessGameDAOGetPlayerGameList {
    
    DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    ServerChessGameTestDatabaseEntity scgDbEntity;
    ServerChessGameDAO dao;
    Player player;
    Player opponent;
    Player otherPlayer;
    DAO<Player> playerDAO;
    static final Integer NO_OF_ENTRIES = 10;

    @Before
    void setup() {
        fixture.setUp();
        scgDbEntity = new ServerChessGameTestDatabaseEntity(NO_OF_ENTRIES);
        dao = new ServerChessGameDAO();
        playerDAO = new DAO<Player>(HumanPlayer);
        
        player = playerDAO.findEntities("userName", "laura")?.get(0);
        opponent = playerDAO.findEntities("userName", "nobby")?.get(0);
        otherPlayer = playerDAO.findEntities("userName", "stephen")?.get(0);
    }
    
    @After
    void tearDown() {
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
        AbstractServerChessGame s = new TwoViewServerChessGame(12324L, opponent);
        
        s.setChessGameFactory(new StandardChessGameFactory());
        
        s.addOpponent(player);
        scgDbEntity.addServerChessGameToDataBase(s);
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 2;
    }
    
    @Test
    void testGetServerChessGamesGivenVirtualPlayer() {
        AbstractServerChessGame s = new OneViewServerChessGame(12324L, player);
        
        s.setChessGameFactory(new StandardChessGameFactory());
        Player virtualPlayer = new HumanPlayer("robot");
        
        s.addOpponent(virtualPlayer);
        scgDbEntity.addServerChessGameToDataBase(s);
        
        def games = dao.getGamesForPlayer(player);
        assert games?.isEmpty() == false;
        assert games.size() == NO_OF_ENTRIES + 2;
    }
    
}
