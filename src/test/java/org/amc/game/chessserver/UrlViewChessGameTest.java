package org.amc.game.chessserver;

import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

public class UrlViewChessGameTest {
    private UrlViewChessGameController urlController;
    private static final long GAME_UID = 4321L;
    private ChessGameFixture cgFixture; 
    
    @Mock
    private TwoViewServerChessGame scgGame;
    
    @Mock
    private OneViewServerChessGame obscgGame;
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        cgFixture = new  ChessGameFixture();
        urlController = new UrlViewChessGameController();
        urlController.setServerChessGameDAO(serverChessGameDAO);
        
        when(scgGame.getPlayer()).thenReturn(cgFixture.getWhitePlayer());
        when(scgGame.getOpponent()).thenReturn(cgFixture.getBlackPlayer());        
    }

    @Test
    public void test() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.IN_PROGRESS);
        when(scgGame.getPlayer(eq(cgFixture.getWhitePlayer()))).thenReturn(cgFixture.getWhitePlayer());
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME_UUID, GAME_UID);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME, scgGame);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.CHESSPLAYER, 
                        cgFixture.getWhitePlayer());
    }
    
    @Test
    public void opponentViewTest() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.IN_PROGRESS);
        when(scgGame.getPlayer(eq(cgFixture.getBlackPlayer()))).thenReturn(cgFixture.getBlackPlayer());
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getBlackPlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME_UUID, GAME_UID);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.GAME, scgGame);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.CHESSPLAYER, 
                        cgFixture.getBlackPlayer());
    }
    
    @Test
    public void serverChessGameNotInProcessStateTest() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.AWAITING_PLAYER);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, 
                        String.format(UrlViewChessGameController.CANT_VIEW_CHESSGAME, GAME_UID)); 
    }
    
    @Test
    public void serverChessGameInFinishedStateTest() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.FINISHED);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, 
                        String.format(UrlViewChessGameController.CANT_VIEW_CHESSGAME, GAME_UID)); 
    }
    
    @Test
    public void serverChessGameIsNullTest() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(null);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, 
                        String.format(UrlViewChessGameController.CANT_VIEW_CHESSGAME, GAME_UID)); 
    }
    
    @Test
    public void serverChessGameIsNotTwoViewGameTest() throws DAOException {
        when(obscgGame.getCurrentStatus()).thenReturn(ServerGameStatus.IN_PROGRESS);
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(obscgGame);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, 
                        String.format(UrlViewChessGameController.CANT_VIEW_CHESSGAME, GAME_UID)); 
    }

    @Test
    public void wrongPlayerTest() throws DAOException {
        Player villian = new HumanPlayer("Villian"); 
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.IN_PROGRESS);
        when(scgGame.getPlayer(eq(cgFixture.getWhitePlayer()))).thenReturn(cgFixture.getWhitePlayer());
        
        ModelAndView mav = urlController.viewChessGame(villian, GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
    }
}
