package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OneViewChessGamePlayerDeletion {
    private DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    private ServerChessGameDAO serverChessGameDao;
    private static final long GAME_UID = 1234L;
    private final ChessGameFactory chessGameFactory = new StandardChessGameFactory(); 

    private DAO<Player> playerDao;
    private Player whitePlayer;
    private Player blackPlayer;
    
    @Before
    public void setUp() throws Exception {
        fixture.setUp();
        serverChessGameDao = new ServerChessGameDAO();
        playerDao = new DAO<>(HumanPlayer.class);
        setUpPlayers();
    }
    
    private void setUpPlayers() {
        whitePlayer = new HumanPlayer("White Player");
        whitePlayer.setUserName("whitePlayer");
        
        blackPlayer = new HumanPlayer("Black Player");
        blackPlayer.setUserName("blackPlayer");
    }

    @After
    public void tearDown() throws Exception {
        fixture.tearDown();
    }

    @Test
    public void testRealPlayers() throws DAOException {
        ServerChessGame serverChessGame = new TwoViewServerChessGame(GAME_UID, whitePlayer);
        serverChessGame.setChessGameFactory(chessGameFactory);
        serverChessGame.addOpponent(blackPlayer);
        serverChessGame = serverChessGameDao.saveServerChessGame(serverChessGame);
        serverChessGameDao.deleteEntity(serverChessGame);
        assertPlayersExist();
    }
    
    private void assertPlayersExist() throws DAOException {
        List<Player> playerList = playerDao.findEntities("userName", whitePlayer.getUserName());
        assertNotNull(playerList);
        assertEquals(1, playerList.size());
        
        playerList = playerDao.findEntities("userName", blackPlayer.getUserName());
        assertNotNull(playerList);
        assertEquals(1, playerList.size());
    }
    
    @Test
    public void testVirtualPlayers() throws DAOException {
        ServerChessGame serverChessGame = new OneViewServerChessGame(GAME_UID, whitePlayer);
        serverChessGame.setChessGameFactory(chessGameFactory);
        serverChessGame.addOpponent(blackPlayer);
        serverChessGame = serverChessGameDao.saveServerChessGame(serverChessGame);
        serverChessGameDao.deleteEntity(serverChessGame);
        assertPlayersDontExist();
    }
    
    private void assertPlayersDontExist() throws DAOException {
        List<Player> playerList = playerDao.findEntities("userName", whitePlayer.getUserName());
        assertNotNull(playerList);
        assertEquals(1, playerList.size());
        
        playerList = playerDao.findEntities("userName", blackPlayer.getUserName());
        assertNotNull(playerList);
        assertEquals(0, playerList.size());
    }

}
