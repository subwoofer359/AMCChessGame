package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;

public class SaveGameStompControllerUnitTest {
    private SaveGameStompController controller;

    private ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Stephen"), Colour.WHITE);

    private ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);
    
    private ChessGamePlayer unknownPlayer = new RealChessGamePlayer(new HumanPlayer("Villian"), Colour.BLACK);

    private static final long gameUUID = 1234L;

    private ServerChessGame scg;

    private Map<String, Object> sessionAttributes;

    private SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    private ArgumentCaptor<String> userArgument;

    private ArgumentCaptor<String> destinationArgument;

    private ArgumentCaptor<String> payloadArgument;

    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> headersArgument;
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private EntityTransaction transaction;

    private Principal principal = new Principal() {

        @Override
        public String getName() {
            return "Stephen";
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        scg = new TwoViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        
        this.controller = new SaveGameStompController();
        controller.setServerChessDAO(serverChessGameDAO);
        
        sessionAttributes = new HashMap<String, Object>();

        this.controller.setTemplate(template);
        userArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        payloadArgument = ArgumentCaptor.forClass(String.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);
            
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", whitePlayer);
        
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(serverChessGameDAO.getEntityManager()).thenReturn(entityManager);
    }
    
    @AfterClass
    public static void tearDownClass() {

    }
    
    @Test
    public void testSaveGame() throws DAOException {
        
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        when(serverChessGameDAO.saveServerChessGame(eq(scg))).thenReturn(scg);
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO,times(1)).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.GAME_SAVED_SUCCESS, payloadArgument.getValue());
        
    }
    
    @SuppressWarnings("unchecked")
    private void verifySimpMessagingTemplateCallToUser() {
        verify(template).convertAndSendToUser(userArgument.capture(),
                        destinationArgument.capture(), payloadArgument.capture(),
                        headersArgument.capture());
    }
    
    @Test
    public void testSaveGameServerChessGameDoesntExist() throws DAOException {
        Long invalidGameUID = 1L;
        controller.save(principal, sessionAttributes, invalidGameUID, "Save");
        verify(serverChessGameDAO, never()).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(String.format(SaveGameStompController.SAVE_ERROR_GAME_DOESNT_EXIST_ERROR, invalidGameUID), 
                        payloadArgument.getValue());
        
    }
    
    @Test
    public void testSaveFinishedServerChessGame() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        
        this.scg.setCurrentStatus(ServerGameStatus.FINISHED);
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, never()).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.SAVE_ERROR_GAME_IS_OVER, payloadArgument.getValue());
    }
    
    @Test
    public void testSaveServerChessGameDAOException() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        
        doThrow(new DAOException("Database connection closed"))
            .when(serverChessGameDAO).saveServerChessGame(eq(scg));
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, times(1)).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.SAVE_ERROR_CANT_BE_SAVED, payloadArgument.getValue());
    }
    
    @Test
    public void testGetServerChessGameDAOException() throws DAOException {
        
        doThrow(new DAOException("Database connection closed"))
            .when(serverChessGameDAO).getServerChessGame(eq(gameUUID));
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, times(0)).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(String.format(SaveGameStompController.SAVE_ERROR_GAME_DOESNT_EXIST_ERROR, gameUUID), 
                        payloadArgument.getValue());
    }
    
    @Test
    public void testSaveServerChessGameByUnknownPlayer() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        sessionAttributes.put("PLAYER", unknownPlayer);
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, never()).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.ERROR_UNKNOWN_PLAYER, payloadArgument.getValue());
    }
    
    @Test
    public void testSaveThrowsOptimisticLockingException() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        
        doThrow(new OptimisticLockException())
        .when(serverChessGameDAO).saveServerChessGame(eq(scg));
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, times(1)).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.SAVE_ERROR_CANT_BE_SAVED, payloadArgument.getValue());
    }
    
    @Test
    public void testSaveThrowsOptimisticLockingExceptionThenDAOException() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        
        doThrow(new OptimisticLockException())
        .when(serverChessGameDAO).saveServerChessGame(eq(scg));
        doThrow(new DAOException("Database connection closed"))
        .when(serverChessGameDAO).deleteEntity(eq(scg));
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        
        verify(serverChessGameDAO, times(1)).saveServerChessGame(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(SaveGameStompController.SAVE_ERROR_CANT_BE_SAVED, payloadArgument.getValue());
    }
}
