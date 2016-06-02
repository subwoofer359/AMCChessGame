package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;

import org.amc.DAOException;
import org.amc.dao.SCGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class GameActionsStompControllerTest {
    
    private GameActionsStompController controller;

    private ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Stephen"), Colour.WHITE);

    private ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);

    private long gameUUID = 1234L;

    private AbstractServerChessGame scg;
    
    private OfflineChessGameMessager messager = mock (OfflineChessGameMessager.class);
    
    private static final String DESTINATION_BOTH_PLAYERS = "/topic/updates";

    private Map<String, Object> sessionAttributes;

    private SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    private ArgumentCaptor<String> userArgument;

    private ArgumentCaptor<String> destinationArgument;

    private ArgumentCaptor<String> payoadArgument;
    
    @Mock
    private SCGameDAO serverChessGameDAO;

    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> headersArgument;

    private Principal principal = new Principal() {

        @Override
        public String getName() {
            return "Stephen";
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.controller = new GameActionsStompController();
        
        scg = new TwoViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        
        controller.setServerChessDAO(serverChessGameDAO);
        sessionAttributes = new HashMap<String, Object>();

        this.controller.setTemplate(template);
        userArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        payoadArgument = ArgumentCaptor.forClass(String.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQuitChessGame() throws DAOException {
        
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", whitePlayer);
        scg.attachObserver(messager);
        
        when(serverChessGameDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
        controller.quitChessGame(principal, sessionAttributes, gameUUID, "Quit");
        
        verifySimpMessagingTemplateCall();
        
        verify(messager,times(1)).update(eq(scg), eq(whitePlayer));
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, scg.getCurrentStatus());
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(String.format(GameActionsStompController.MSG_PLAYER_HAS_QUIT, principal.getName()),
                        payoadArgument.getValue());
        assertEquals(String.format("%s/%d", DESTINATION_BOTH_PLAYERS, gameUUID),
                        destinationArgument.getValue());
    }
     
    @Test
    public void testQuitFinishedChessGame() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(anyLong())).thenReturn(scg);
        scg.addOpponent(blackPlayer);
        scg.setCurrentStatus(ServerGameStatus.FINISHED);
        sessionAttributes.put("PLAYER", whitePlayer);
        controller.quitChessGame(principal, sessionAttributes, gameUUID, "Quit");
        
        verify(messager,never()).update(eq(scg), eq(whitePlayer));
        verifySimpMessagingTemplateCall();
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, scg.getCurrentStatus());
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(GameActionsStompController.MSG_GAME_ALREADY_OVER,
                        payoadArgument.getValue());
        assertEquals(String.format("%s/%d", DESTINATION_BOTH_PLAYERS, gameUUID),
                        destinationArgument.getValue());
    }
    
    @Test
    public void getChessBoardTest() throws DAOException {
        when(serverChessGameDAO.getServerChessGame(anyLong())).thenReturn(scg);
        
        scg.addOpponent(blackPlayer);
        
        controller.getChessBoard(principal, sessionAttributes, gameUUID, "chessboard");
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals(principal.getName(), userArgument.getValue());
        assertEquals(StompController.MESSAGE_USER_DESTINATION, destinationArgument.getValue());
        assertEquals(MessageType.UPDATE, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertNotNull(payoadArgument.getValue());
        assertTrue(payoadArgument.getValue().length() > 0);
        
    }
    
    @Test
    public void getNullChessBoardTest() {
        
        controller.getChessBoard(principal, sessionAttributes, gameUUID, "chessboard");
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals(principal.getName(), userArgument.getValue());
        assertEquals(StompController.MESSAGE_USER_DESTINATION, destinationArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(GameActionsStompController.ERROR_CHESSBOARD_DOESNT_EXIST, payoadArgument.getValue()); 
    }
    
    @Test
    public void getChessBoardNoServerChessGameTest() {
        //gameMap.remove(gameUUID);
        controller.getChessBoard(principal, sessionAttributes, gameUUID, "chessboard");
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals(principal.getName(), userArgument.getValue());
        assertEquals(StompController.MESSAGE_USER_DESTINATION, destinationArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
        assertEquals(GameActionsStompController.ERROR_CHESSBOARD_DOESNT_EXIST, payoadArgument.getValue());
        
    }
    
    @SuppressWarnings("unchecked")
    private void verifySimpMessagingTemplateCallToUser() {
        verify(template).convertAndSendToUser(userArgument.capture(),
                        destinationArgument.capture(), payoadArgument.capture(),
                        headersArgument.capture());
    }
    
    @SuppressWarnings("unchecked")
    private void verifySimpMessagingTemplateCall() {
        verify(template).convertAndSend(destinationArgument.capture(), payoadArgument.capture(),
                        headersArgument.capture());
    }
}
