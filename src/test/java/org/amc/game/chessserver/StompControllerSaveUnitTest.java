package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompConstants.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class StompControllerSaveUnitTest {
    private StompController controller;

    private ChessGamePlayer whitePlayer = new ChessGamePlayer(new HumanPlayer("Stephen"), Colour.WHITE);

    private ChessGamePlayer blackPlayer = new ChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);
    
    private ChessGamePlayer unknownPlayer = new ChessGamePlayer(new HumanPlayer("Villian"), Colour.BLACK);

    private long gameUUID = 1234L;

    private ServerChessGame scg;
    
    private OfflineChessGameMessager messager = mock (OfflineChessGameMessager.class);
    
    private static final String DESTINATION_BOTH_PLAYERS = "/topic/updates";

    private Map<String, Object> sessionAttributes;

    private SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    private ArgumentCaptor<String> userArgument;

    private ArgumentCaptor<String> destinationArgument;

    private ArgumentCaptor<String> payoadArgument;

    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> headersArgument;
    
    private ServerChessGameDAO serverChessGameDAO;

    private Principal principal = new Principal() {

        @Override
        public String getName() {
            return "Stephen";
        }
    };

    @Before
    public void setUp() throws Exception {
        this.controller = new StompController();
        scg = new ServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        
        Map<Long, ServerChessGame> gameMap = new HashMap<Long, ServerChessGame>();
        gameMap.put(gameUUID, scg);
        
        controller.setGameMap(gameMap);
        sessionAttributes = new HashMap<String, Object>();

        this.controller.setTemplate(template);
        userArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        payoadArgument = ArgumentCaptor.forClass(String.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);
        
        this.serverChessGameDAO = mock(ServerChessGameDAO.class);
        controller.setServerChessDAO(serverChessGameDAO);
        
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", whitePlayer);
    }
    
    @Test
    public void testSaveGame() throws DAOException {
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO,times(1)).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(StompController.GAME_SAVED_SUCCESS, payoadArgument.getValue());
    }
    
    @SuppressWarnings("unchecked")
    private void verifySimpMessagingTemplateCallToUser() {
        verify(template).convertAndSendToUser(userArgument.capture(),
                        destinationArgument.capture(), payoadArgument.capture(),
                        headersArgument.capture());
    }
    
    @Test
    public void testSaveGameServerChessGameDoesntExist() throws DAOException {
        Long invalidGameUID = 1L;
        controller.save(principal, sessionAttributes, invalidGameUID, "Save");
        verify(serverChessGameDAO, never()).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(String.format(StompController.SAVE_ERROR_GAME_DOESNT_EXIST_ERROR, invalidGameUID), 
                        payoadArgument.getValue());
        
    }
    
    @Test
    public void testSaveFinishedServerChessGame() throws DAOException {
        this.scg.setCurrentStatus(ServerGameStatus.FINISHED);
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, never()).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(StompController.SAVE_ERROR_GAME_IS_OVER, payoadArgument.getValue());
    }
    
    @Test
    public void testSaveServerChessGameDAOException() throws DAOException {
        doThrow(new DAOException("Database connection closed"))
            .when(serverChessGameDAO).addEntity(eq(scg));
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, times(1)).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(StompController.SAVE_ERROR_CANT_BE_SAVED, payoadArgument.getValue());
    }
    
    @Test
    public void testSaveServerChessGameByUnknownPlayer() throws DAOException {
        sessionAttributes.put("PLAYER", unknownPlayer);
        controller.save(principal, sessionAttributes, gameUUID, "Save");
        verify(serverChessGameDAO, never()).addEntity(eq(scg));
        verifySimpMessagingTemplateCallToUser();
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(StompController.ERROR_UNKNOWN_PLAYER, payoadArgument.getValue());
    }
}
