package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class StartPageControllerCreateGameTest {
    private Model model;
    private ChessGamePlayer whitePlayer;
    private StartPageController controller;
    private static final String OPPONENT = "Kate Bush";
    private ArgumentCaptor<AbstractServerChessGame> gameCaptor = ArgumentCaptor
                    .forClass(AbstractServerChessGame.class);
    @Mock
    private ServerChessGameDAO sCGDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ServerChessGameFactory scgFactory = new ServerChessGameFactory();
        model = new ExtendedModelMap();
        whitePlayer = new RealChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        controller = new StartPageController();
        scgFactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
        controller.setServerChessGameFactory(scgFactory);
        controller.setServerChessGameDAO(sCGDAO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTwoViewServerGame() throws DAOException {
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.NETWORK_GAME, null);

        assertGameIsSavedInDatabase();
        assertPlayerIsAddedToChessGame();

        assertLongStoreInSessionAttribute();
        assertEquals(StartPageController.CHESS_APPLICATION_PAGE, viewName);
    }

    private void assertGameIsSavedInDatabase() throws DAOException {
        verify(sCGDAO, times(1)).saveServerChessGame(any(AbstractServerChessGame.class));
    }

    private void assertPlayerIsAddedToChessGame() throws DAOException {
        verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
        AbstractServerChessGame game = gameCaptor.getValue();
        assertTrue(ComparePlayers.comparePlayers(game.getPlayer(), whitePlayer));
    }

    @Test
    public void testOneViewServerGame() throws DAOException {
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME, OPPONENT);
        assertPlayerIsAddedToChessGame();

        assertLongStoreInSessionAttribute();
        assertEquals(StartPageController.ONE_VIEW_CHESS_PAGE, viewName);
        assertNotNull(model.asMap().get(ServerConstants.GAME));
        assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
    }

    @Test
    public void testPlayersNameIsEmptyString() throws DAOException {
        String invalidPlayersName = "";
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME,
                        invalidPlayersName);

        verify(sCGDAO, never()).saveServerChessGame(any(AbstractServerChessGame.class));
        assertEquals(StartPageController.TWOVIEW_FORWARD_PAGE, viewName);
        assertEquals(invalidPlayersName, model.asMap().get(StartPageController.PLAYERS_NAME_FIELD));

    }

    @Test
    public void testPlayersNameIsNull() throws DAOException {
        String invalidPlayersName = null;
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME,
                        invalidPlayersName);
        verify(sCGDAO, never()).saveServerChessGame(any(AbstractServerChessGame.class));
        assertEquals(StartPageController.TWOVIEW_FORWARD_PAGE, viewName);
        assertEquals(invalidPlayersName, model.asMap().get(StartPageController.PLAYERS_NAME_FIELD));

    }

    private void assertSessionAttributeNull() {
        assertNull(model.asMap().get(ServerConstants.GAME_UUID.toString()));
    }

    private void assertLongStoreInSessionAttribute() {
        assertEquals(model.asMap().get(ServerConstants.GAME_UUID.toString()).getClass(), Long.class);
    }
}