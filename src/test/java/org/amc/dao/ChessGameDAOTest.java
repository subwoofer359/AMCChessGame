package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.MoveEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessGameDAOTest {

    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<ChessGame> chessGameDAO;
    private ChessGameFixture cgFixture;
    private Player laura;
    private Player nobby;
    private DAO<Player> playerDAO;
    private ChessGame game;
 
    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        chessGameDAO = new DAO<>(ChessGame.class);
        cgFixture = new ChessGameFixture();
        
        playerDAO = new DAO<Player>(HumanPlayer.class);
        laura = playerDAO.findEntities("userName", "laura").get(0);
        nobby = playerDAO.findEntities("userName", "nobby").get(0);
        game = new ChessGame(cgFixture.getBoard(), new ChessGamePlayer(nobby, Colour.WHITE),
                        new ChessGamePlayer(laura, Colour.BLACK));
        
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void test() throws Exception {
        
        cgFixture.getBoard().initialise();
        game.move(game.getCurrentPlayer(),);
        chessGameDAO.addEntity(game);
    }

}
