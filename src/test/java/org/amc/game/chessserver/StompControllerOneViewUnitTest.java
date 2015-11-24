package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.amc.game.chessserver.StompConstants.MESSAGE_HEADER_TYPE;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class StompControllerOneViewUnitTest {

    private StompController controller;
    
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    
    private ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer("Stephen"), Colour.WHITE);

    private ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);

    private long gameUUID = 1234L;

    private ServerChessGame scg;

    private SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    private ArgumentCaptor<String> userArgument;

    private ArgumentCaptor<String> destinationArgument;

    private ArgumentCaptor<String> payoadArgument;

    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Map> headersArgument;
    
    private ChessGameFactory chessGameFactory;

    private Principal principal = new Principal() {

        @Override
        public String getName() {
            return "Stephen";
        }
    };

    @Before
    public void setUp() {
        this.controller = new StompController();
        chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        scg = new OneViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(chessGameFactory);
        gameMap = new ConcurrentHashMap<Long, ServerChessGame>();
        gameMap.put(gameUUID, scg);
        controller.setGameMap(gameMap);

        this.controller.setTemplate(template);
        userArgument = ArgumentCaptor.forClass(String.class);
        destinationArgument = ArgumentCaptor.forClass(String.class);
        payoadArgument = ArgumentCaptor.forClass(String.class);
        headersArgument = ArgumentCaptor.forClass(Map.class);

    }

    @Test
    public void testMove() {
        scg.addOpponent(blackPlayer);
        String move = "A2-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals("", payoadArgument.getValue());
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @SuppressWarnings("unchecked")
    private void verifySimpMessagingTemplateCallToUser() {
        verify(template).convertAndSendToUser(userArgument.capture(),
                        destinationArgument.capture(), payoadArgument.capture(),
                        headersArgument.capture());
    }

    @Test
    public void testInvalidMove() {
        scg.addOpponent(blackPlayer);
        String move = "A1-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals("Error:Not a valid move", payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testChessGameNotInitialised() {
        String move = "A1-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which hasn't got two players", gameUUID),
                        payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testChessGameFinished() {
        scg.addOpponent(blackPlayer);
        scg.setCurrentStatus(ServerChessGame.ServerGameStatus.FINISHED);
        String move = "A1-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which has finished", gameUUID),
                        payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }
    
    /**
     * JIRA http://192.168.1.5:8081/browse/CG-49
     */
    @Test
    public void testUnParsableMove()
    {
        scg.addOpponent(blackPlayer);
        String move = "-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
    }
    
    @Test
    public void testNotOneViewServerChessGame() {
        ServerChessGame scg = new TwoViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(chessGameFactory);
        this.gameMap.put(gameUUID, scg);
        scg.addOpponent(blackPlayer);
        String move = "A2-A3";
        controller.registerOneViewMoveMove(principal, gameUUID, move);
        verify(template,never()).convertAndSendToUser(anyString(),
                        anyString(), anyObject(),anyMap());
    }

}
