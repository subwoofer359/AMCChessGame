package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompConstants.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.view.ChessGameTextView;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/SpringTestConfig.xml","/GameServerSecurity.xml",  "/GameServerWebSockets.xml"})

public class StompControllerIntegrationTest {
    
    private static final String SESSION_ID = "20";

    private static final String SUBSCRIPTION_ID = "20";

    private static final String SESSION_ATTRIBUTE = "PLAYER";

    private static final String MESSAGE_DESTINATION = "/app/move/";
    
    private static final String ONEVIEW_MESSAGE_DESTINATION = "/app/oneViewMove/";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;

    @Autowired
    private AbstractSubscribableChannel clientOutboundChannel;

    @Autowired
    private AbstractSubscribableChannel brokerChannel;
    
    private TestChannelInterceptor clientOutboundChannelInterceptor;

    private TestChannelInterceptor brokerChannelInterceptor;


    private ChessGamePlayer whitePlayer = new ChessGamePlayer(new HumanPlayer("Stephen"),
                    Colour.WHITE);

    private ChessGamePlayer blackPlayer = new ChessGamePlayer(new HumanPlayer("Chris"),
                    Colour.BLACK);

    private long gameUUID = 1234L;
    
    private long oneViewChessGameUUID = 4321L;

    private ServerChessGame scg;
    
    private ServerChessGame oneViewChessGame;
    
    private String[] moves = {"A2-A3", "A7-A6", "B1-C3", "E7-E6"};

    @Before
    public void setup() {
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannelInterceptor.setIncludedDestinations("/user/**");
        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
        
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor.setIncludedDestinations(MESSAGE_DESTINATION);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
        
        @SuppressWarnings("unchecked")
        Map<Long, ServerChessGame> gameMap = (Map<Long, ServerChessGame>) wac.getBean("gameMap");
          
        ServerChessGameFactory scgfactory = new ServerChessGameFactory();
        OfflineChessGameMessagerFactory ocgFactory = new OfflineChessGameMessagerFactory() {
            
            @Override
            public OfflineChessGameMessager createOfflineChessGameMessager() {
                return mock(OfflineChessGameMessager.class);
            }
        };
        
        scgfactory.setOfflineChessGameMessagerFactory(ocgFactory);
        
        
        scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME, gameUUID, whitePlayer);
        scg.addOpponent(blackPlayer);
        gameMap.put(gameUUID, scg);
        
        oneViewChessGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, oneViewChessGameUUID, whitePlayer);
        oneViewChessGame.addOpponent(blackPlayer);
        gameMap.put(oneViewChessGameUUID, oneViewChessGame);
    }

    @Test
    public void TwoViewChessGameMove() throws Exception {
        subscribe();
        
        for(int i = 0; i < moves.length; i++) {
            twoViewMove(scg.getChessGame().getCurrentPlayer(), gameUUID, moves[i]);
            testInfoMessageSent();
            verifyMove(scg, moves[i]);
        }
    }
    
    private void verifyMove(ServerChessGame scg, String moveString) {
        Move actualMove = scg.getChessGame().getChessBoard().getTheLastMove();
        MoveEditor editor = new MoveEditor();
        editor.setAsText(moveString);
        Move expectedMove = (Move)editor.getValue();
        assertEquals(expectedMove.getStart(), actualMove.getStart());
        assertEquals(expectedMove.getEnd(), actualMove.getEnd());
    }
    
    private void testInfoMessageSent() throws Exception {
        testMessageSent(MessageType.INFO);
    }
    
    private void testErrorMessageSent() throws Exception {
        testMessageSent(MessageType.ERROR);
    }
    
    private void testMessageSent(MessageType type) throws Exception {
        Message<?> message = this.brokerChannelInterceptor.awaitMessage(5);
        assertNotNull(message);
        assertNotEquals(type, message.getHeaders().get(MESSAGE_HEADER_TYPE));
    }
    
    
    @Test
    public void OneViewChessGameMove() throws Exception {
        subscribe();
        
        for(int i = 0; i < moves.length; i++) {
            oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID, moves[i]);
            testInfoMessageSent();
            verifyMove(oneViewChessGame, moves[i]);
        }
    }
    
    @Test
    public void OneViewInvalidMove() throws Exception {
        subscribe();
        oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID, "A2-A5");
        testErrorMessageSent();
        assertEmptyMove(oneViewChessGame);
    }
    
    private void assertEmptyMove(ServerChessGame scg) {
        assertEquals(Move.EMPTY_MOVE, scg.getChessGame().getChessBoard().getTheLastMove());
    }
    
    @Test
    public void TwoViewInvalidMove() throws Exception {
        subscribe();
        twoViewMove(scg.getChessGame().getCurrentPlayer(), gameUUID, "A2-A5");
        testErrorMessageSent();
        assertEmptyMove(scg);
    }

    private void twoViewMove(Player player, long gameUid, String moveString) throws Exception {
        move(player, gameUid, MESSAGE_DESTINATION, moveString);
    }
    
    private void oneViewMove(Player player, long gameUid, String moveString) throws Exception {
        move(player, gameUid, ONEVIEW_MESSAGE_DESTINATION, moveString);
    }
    
    private void move(Player player, long gameUid, String messageDestination, String moveString) throws Exception {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId(SUBSCRIPTION_ID);
        headers.setDestination(messageDestination + gameUid);
        headers.setSessionId(SESSION_ID);
        headers.setUser(getTestPrincipal());
        Map<String, Object> sessionAttributes = new HashMap<String, Object>();
        sessionAttributes.put(SESSION_ATTRIBUTE, player);
        headers.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = MessageBuilder.createMessage(moveString.getBytes(),
                        headers.getMessageHeaders());
        this.clientInboundChannel.send(message);   
    }

    private void subscribe() {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        
        headers.setUser(getTestPrincipal());
        headers.setSubscriptionId(SUBSCRIPTION_ID);
        headers.setDestination("/user/queue/updates");
        headers.setSessionId(SESSION_ID);
        
        headers.setSessionAttributes(new HashMap<String, Object>());
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
                        headers.getMessageHeaders());
        
        this.clientInboundChannel.send(message);
    }

    private Principal getTestPrincipal() {
        Principal p = new Principal() {

            @Override
            public String getName() {
                return "adrian";
            }
        };
        return p;
    }
}
