package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class NewNetworkChessGameControllerCreateGameTest {
    private Model model;
    private ChessGamePlayer whitePlayer;
    private NewNetworkChessGameController controller;
    
    private GameControllerTestHelper helper;
  
    @Mock
    private ServerChessGameDAO sCGDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ServerChessGameFactory scgFactory = new ServerChessGameFactory();
        model = new ExtendedModelMap();
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        controller = new NewNetworkChessGameController();
        scgFactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
        controller.setServerChessGameFactory(scgFactory);
        controller.setServerChessGameDAO(sCGDAO);
        helper = new GameControllerTestHelper(model, sCGDAO);
    }

    @Test
    public void testTwoViewServerGame() throws DAOException {
        helper.assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.NETWORK_GAME);

        helper.assertGameIsSavedInDatabase();
        helper.assertPlayerIsAddedToChessGame(whitePlayer);

        helper.assertLongStoreInSessionAttribute();
        assertEquals(GameControllerHelper.CHESS_APPLICATION_PAGE, viewName);
    }
}