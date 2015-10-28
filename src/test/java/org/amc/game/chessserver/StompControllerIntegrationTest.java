package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompConstants.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.messaging.EmailTemplate;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
@ContextConfiguration({"/SpringTestConfig.xml", "/GameServerSecurity.xml",  "/GameServerWebSockets.xml", "/EmailServiceContext.xml"})

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

    private long gameUUID = 1234L;
    
    private long oneViewChessGameUUID = 4321L;

    private ServerChessGame scg;
    
    private ServerChessGame oneViewChessGame;
    
    private String[] moves = {"A2-A3", "A7-A6", "B1-C3", "E7-E6"};

    private DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();
    
    private DAO<Player> playerDAO;
    
    private Player stephen;
    
    private Player nobby;
    
    private Map<Long, ServerChessGame> gameMap;
    
    private JsonChessGameView view;
    
    private SimpMessagingTemplate template;
    
    @Before
    public void setup() throws Exception {
       fixture.setUp();
       
       playerDAO = new DAO<Player>(HumanPlayer.class);
       stephen = playerDAO.findEntities("userName", "stephen").get(0);
       nobby = playerDAO.findEntities("userName", "nobby").get(0);
       
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannelInterceptor.setIncludedDestinations("/user/**");
        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
        
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor.setIncludedDestinations(MESSAGE_DESTINATION);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
        
        
        gameMap = (Map<Long, ServerChessGame>) wac.getBean("gameMap");
          
        ServerChessGameFactory scgfactory = (ServerChessGameFactory)wac.getBean("serverChessGameFactory");        
        
        scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME, gameUUID, stephen);
        scg.addOpponent(nobby);
        scg = gameMap.put(gameUUID, scg);
        
        oneViewChessGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, oneViewChessGameUUID, stephen);
        oneViewChessGame.addOpponent(nobby);
        
        
        
        gameMap.put(oneViewChessGameUUID, oneViewChessGame);
        oneViewChessGame = gameMap.get(oneViewChessGameUUID);
        
        template = mock(SimpMessagingTemplate.class);
        view = new JsonChessGameView(template);
        view.setGameToObserver(oneViewChessGame);
    }
    
    @After
    public void tearDown() throws Exception {
        this.fixture.tearDown();
    }

    @Test
    public void TwoViewChessGameMove() throws Exception {
        subscribe();
        
        for(int i = 0; i < moves.length; i++) {
            scg = gameMap.get(gameUUID);
            twoViewMove(scg.getChessGame().getCurrentPlayer(), gameUUID, moves[i]);
            testInfoMessageSent();
            verifyMove(scg, moves[i]);
        }
    }
    
    private void verifyMove(ServerChessGame scg, String moveString) {
        Move actualMove = scg.getChessGame().getTheLastMove();
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
        ArgumentCaptor<Object> jsonBoard = ArgumentCaptor.forClass(Object.class);
        
        
        for(int i = 0; i < moves.length; i++) {
            oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID, moves[i]);
            testInfoMessageSent();
            verifyMove(oneViewChessGame, moves[i]);
            //verify(template).convertAndSend(anyString(), jsonBoard.capture(), anyMap());
            //System.out.println(jsonBoard.getValue());
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
        assertEquals(Move.EMPTY_MOVE, scg.getChessGame().getTheLastMove());
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
