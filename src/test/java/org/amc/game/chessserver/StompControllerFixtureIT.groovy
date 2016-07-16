package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.amc.dao.DAOInterface;
import org.amc.dao.DAO;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

public class StompControllerFixtureIT {

    
    public static final String SESSION_ID = "20";
    
    public static final String SUBSCRIPTION_ID = "20";
    
    public static final String SESSION_ATTRIBUTE = "PLAYER";

    

    @Autowired
    WebApplicationContext wac;

    @Autowired
    AbstractSubscribableChannel clientInboundChannel;

    @Autowired
    AbstractSubscribableChannel clientOutboundChannel;

    @Autowired
    AbstractSubscribableChannel brokerChannel;

    TestChannelInterceptor clientOutboundChannelInterceptor;

    TestChannelInterceptor brokerChannelInterceptor;

    final long gameUUID = 1234L;

    DatabaseFixture fixture = new DatabaseFixture();

    DAOInterface<Player> playerDAO;

    Player stephen;

    Player nobby;

    SimpMessagingTemplate template;

    SCGDAOInterface serverChessGameDAO;

    @Before
    public void setup() throws Exception {
        fixture.setUp();
        playerDAO = new DAO<Player>(HumanPlayer.class);
        playerDAO.setEntityManager(fixture.getEntityManager());
        stephen = playerDAO.findEntities("userName", "stephen").get(0);
        nobby = playerDAO.findEntities("userName", "nobby").get(0);
        setUpStompChannels();
        serverChessGameDAO = (SCGDAOInterface) wac.getBean("myServerChessGameDAO");
        template = mock(SimpMessagingTemplate.class);
    }

    private void setUpStompChannels() {
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannelInterceptor.setIncludedDestinations("/user/**");
        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);

        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor.setIncludedDestinations(MESSAGE_DESTINATION);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
    }



    @After
    public void tearDown() throws Exception {
        this.fixture.tearDown();
    }

    Principal getTestPrincipal() {
        Principal p = new Principal() {

                    @Override
                    public String getName() {
                        return "stephen";
                    }
                };
        return p;
    }

    String testInfoMessageSent() throws Exception {
        testMessageSent(MessageType.INFO);
    }
    
    String testStatusMessageSent() throws Exception {
        testMessageSent(MessageType.STATUS);
    }

    String testErrorMessageSent() throws Exception {
        testMessageSent(MessageType.ERROR);
    }

    String testMessageSent(MessageType type) throws Exception {
        Message<?> message = this.brokerChannelInterceptor.awaitMessage(5);
        assertNotNull("Message sent is null", message);

        @SuppressWarnings("unchecked")
                Map<String, List<String>> nativeHeaders = message.getHeaders()
                .get("nativeHeaders", Map.class);

        String body = new String(message.getPayload());
        assertEquals(type, MessageType.valueOf(nativeHeaders.get(MESSAGE_HEADER_TYPE).get(0)));
        return new String(message.getPayload());
    }

    void subscribe() {
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
}
