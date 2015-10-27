package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;

import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleInputParser;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameDAOTest {

    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<ChessGame> chessGameDAO;

    private Player laura;
    private Player nobby;
    private ChessGame game;
    private SimpleInputParser mlParser;
    private int id;
    private static final String[] MOVES = {"A2A3", "A7A6"};
 
    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        this.mlParser = new SimpleInputParser();
        ServerChessGameTestDatabaseEntity serverChessGamesfixture = new ServerChessGameTestDatabaseEntity();
        laura = serverChessGamesfixture.getBlackPlayer();
        nobby = serverChessGamesfixture.getWhitePlayer();
        game = serverChessGamesfixture.getChessGame();
        chessGameDAO = new DAO<>(ChessGame.class);  
        
        id = game.getId();
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void test() throws Exception {
        ChessGame game = chessGameDAO.getEntity(id);
        game.changePlayer();
        assertTrue(ComparePlayers.comparePlayers(game.getCurrentPlayer(), laura));
        assertTrue(ComparePlayers.comparePlayers(game.getBlackPlayer(), laura));
        assertTrue(ComparePlayers.comparePlayers(game.getWhitePlayer(), nobby));
        
    }
    
    @Test
    public void testChessBoardSaved() throws Exception {
        ChessGame game = chessGameDAO.getEntity(id);
        ChessBoard newBoard = new ChessBoard();
        newBoard.initialise();
        
        ChessBoardUtilities.compareBoards(newBoard, game.getChessBoard());
        
        for(String move : MOVES) {
            Move gameMove = mlParser.parseMoveString(move);
            game.move(game.getCurrentPlayer(), gameMove );
            game.changePlayer();
            newBoard.move(gameMove);
        }
        
        ChessBoardUtilities.compareBoards(newBoard, game.getChessBoard());
    }
}
