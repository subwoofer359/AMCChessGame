package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.Player;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChessBoardDAOTest {
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<ChessBoard> chessBoardDAO;
    private ChessBoardFactory cbFactory;

    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        cbFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        chessBoardDAO = new DAO<>(ChessBoard.class);
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void test() throws Exception {
        ChessBoard board = cbFactory.getChessBoard("Kf8:bf7:kf6");;
        chessBoardDAO.addEntity(board);
    }

}