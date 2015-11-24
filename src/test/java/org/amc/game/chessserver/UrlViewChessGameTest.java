package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
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
    private ServerChessGameDAO serverChessGameDAO; 
    
    @Mock
    private ServerChessGame scgGame;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        cgFixture = new  ChessGameFixture();
        urlController = new UrlViewChessGameController();
        urlController.setServerChessGameDAO(serverChessGameDAO);
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(scgGame);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws DAOException {
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
    public void ServerChessGameNotInProcessStatetest() throws DAOException {
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.AWAITING_PLAYER);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_FORWARD_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, ServerJoinChessGameController.ERROR_GAME_HAS_NO_OPPONENT); 
    }
    
    @Test
    public void ServerChessGameInFinishedStatetest() throws DAOException {
        when(scgGame.getCurrentStatus()).thenReturn(ServerGameStatus.FINISHED);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_FORWARD_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, ServerJoinChessGameController.ERROR_GAMEOVER); 
    }
    
    @Test
    public void ServerChessGameIsNulltest() throws DAOException {
        reset(serverChessGameDAO);
        when(serverChessGameDAO.getServerChessGame(eq(GAME_UID))).thenReturn(null);
        
        ModelAndView mav = urlController.viewChessGame(cgFixture.getWhitePlayer(), GAME_UID);
        
        verify(serverChessGameDAO, times(1)).getServerChessGame(GAME_UID);
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_FORWARD_PAGE);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, 
                        String.format(UrlViewChessGameController.CANT_VIEW_CHESSGAME, GAME_UID)); 
    }

}
