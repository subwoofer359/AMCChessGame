package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.view.ChessBoardView;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ChessGameDAOIT {

    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<ChessGame> chessGameDAO;

    private Player laura;
    private Player nobby;
    private ChessGame game;
    private int id;
    private static final String[] MOVES = {"A2-A3", "A7-A6", "B1-C3"};
 
    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        ServerChessGameDatabaseEntityFixture serverChessGamesfixture = new ServerChessGameDatabaseEntityFixture();
        laura = serverChessGamesfixture.getBlackPlayer();
        nobby = serverChessGamesfixture.getWhitePlayer();
        game = serverChessGamesfixture.getChessGame();
        chessGameDAO = new ChessGameDAO();
        
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
        
        EntityManagerThreadLocal.closeEntityManager();
        
        assertTrue(ComparePlayers.comparePlayers(game.getCurrentPlayer(), laura));
        assertTrue(ComparePlayers.comparePlayers(game.getBlackPlayer(), laura));
        assertTrue(ComparePlayers.comparePlayers(game.getWhitePlayer(), nobby));
        
    }
    
    @Test
    public void testChessBoardSaved() throws Exception {
        ChessGame game = chessGameDAO.getEntity(id);
        ChessBoard newBoard = new ChessBoard();
        newBoard.initialise();
        
        new ChessBoardView(game.getChessBoard());
        ChessBoardUtilities.compareBoards(newBoard, game.getChessBoard());
        
        for(String move : MOVES) {
            Move gameMove = new Move(move);
            game.move(game.getCurrentPlayer(), gameMove );
            game.changePlayer();
            newBoard.move(gameMove);
        }
        
        EntityManagerThreadLocal.closeEntityManager();
        
        game = chessGameDAO.getEntity(id);
        ChessBoardUtilities.compareBoards(newBoard, game.getChessBoard());
    }
    
    /**
     * Test DAO not for Production use
     * @author Adrian Mclaughlin
     *
     */
    public static class ChessGameDAO extends DAO<ChessGame> {

        public ChessGameDAO() {
            super(ChessGame.class);
        }
        
        
        /**
         * @see DAO#getEntity(int)
         * manually marks the field board of ChessBoard dirty so it's updated
         * when the EntityManager is closed. An implementation solution
         * @throws DAOException when Database query fails or not using an OpenJPAEntityManager
         * TODO Replace with an agnostic solution
         */
        @Override
        public ChessGame getEntity(int id) throws DAOException {
            ChessGame game = super.getEntity(id);
            if(getEntityManager() instanceof OpenJPAEntityManager) {
                if(game != null) {
                    ((OpenJPAEntityManager)getEntityManager()).dirty(game, "board");
                }
            } else {
                throw new DAOException("Not an OpenJPA entity manager: changes to chessboard won't be saved");
            }
            
            return game;
        }

        
    }
}
