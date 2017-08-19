package org.amc.game.chessserver;

import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

public class ServerJoinControllerJoinGameTest {
    private ServerJoinChessGameController controller;
    private Player whitePlayer;
    private Player blackPlayer;
    private static final long gameUUID = 1234L;
    private static final long oneViewGameUUID = 4321L;
    private AbstractServerChessGame chessGame;
    private AbstractServerChessGame oneViewChessGame;
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new ServerJoinChessGameController();
        controller.setServerChessGameDAO(serverChessGameDAO);
        
        whitePlayer = new HumanPlayer("Ted");
        blackPlayer = new HumanPlayer("Chris");
        
        ChessGameFactory f = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        
        chessGame = new TwoViewServerChessGame(gameUUID, whitePlayer);
        chessGame.setChessGameFactory(f);
        
        oneViewChessGame = new OneViewServerChessGame(oneViewGameUUID, whitePlayer);
        oneViewChessGame.setChessGameFactory(f);
        
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(chessGame);
        when(serverChessGameDAO.getServerChessGame(eq(oneViewGameUUID))).thenReturn(oneViewChessGame);
    }

    @Test
    public void test() throws DAOException {
        when(serverChessGameDAO.updateEntity(eq(chessGame))).thenReturn(chessGame);
        
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getPlayer(), whitePlayer));
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        assertModelAndViewAttributesOnSuccess(mav, blackPlayer);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME_TYPE, ServerConstants.TWO_VIEW);
    }
    
    @Test
    public void testOneServerViewChessGame() throws DAOException {
        when(serverChessGameDAO.updateEntity(eq(oneViewChessGame))).thenReturn(oneViewChessGame);
        
        ModelAndView mav = controller.joinGame(blackPlayer, oneViewGameUUID);
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(oneViewGameUUID);
        
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getPlayer(), whitePlayer));
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        assertModelAndViewAttributesOnSuccess(mav, blackPlayer);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME_TYPE, ServerConstants.ONE_VIEW);
    }

    @Test
    public void testPlayerJoinsOwnGame() throws DAOException {
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        assertEquals(AbstractServerChessGame.ServerGameStatus.AWAITING_PLAYER, chessGame.getCurrentStatus());
        assertEquals(NO_PLAYER, chessGame.getOpponent());
        assertModelAndViewAttributesOnFail(mav,
                        ServerJoinChessGameController.ERROR_GAME_HAS_NO_OPPONENT);
    }

    @Test
    public void testPlayerReJoinsGameAlreadyInProgress() throws DAOException {
        when(serverChessGameDAO.updateEntity(eq(chessGame))).thenReturn(chessGame);
        
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "chessGamePortal");
        assertModelAndViewAttributesOnSuccess(mav, blackPlayer);

    }

    @Test
    public void testPlayerJoinsGameAlreadyInProgress() throws DAOException {
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        assertNotEquals(AbstractServerChessGame.ServerGameStatus.AWAITING_PLAYER,
                        chessGame.getCurrentStatus());
        assertFalse(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav,
                        ServerJoinChessGameController.ERROR_PLAYER_NOT_OPPONENT);

    }

    @Test
    public void testPlayerJoinsGameWhichHasFinished() throws DAOException {
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, chessGame.getCurrentStatus());
        assertFalse(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav, ServerJoinChessGameController.ERROR_GAMEOVER);
    }

    @Test
    public void testPlayerJoinsCurrentGameWhichHasFinished() throws DAOException {
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        chessGame.addOpponent(blackPlayer);
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav, ServerJoinChessGameController.ERROR_GAMEOVER);
    }

    @Test
    public void testPlayerJoinsOwnGameAlreadyInProgress() throws DAOException {
        AbstractServerChessGame chessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getOpponent(), blackPlayer));
        assertTrue(ComparePlayers.isSamePlayer(chessGame.getPlayer(), whitePlayer));
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        assertModelAndViewAttributesOnSuccess(mav, whitePlayer);

    }

    private void assertModelAndViewAttributesOnSuccess(ModelAndView mav, Player player) {
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAME_UUID);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAME);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.CHESSPLAYER);
        Player actualPlayer = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, ServerConstants.CHESSPLAYER, Player.class);
        assertTrue(ComparePlayers.isSamePlayer(player, actualPlayer));
    }

    private void assertModelAndViewAttributesOnFail(ModelAndView mav, String errorMessage) {
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_FORWARD_PAGE);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.ERRORS);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, errorMessage);
    }

}
