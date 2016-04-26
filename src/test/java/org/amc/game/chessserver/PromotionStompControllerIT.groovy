package org.amc.game.chessserver

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.amc.dao.DAO;
import org.amc.dao.EntityManagerCache;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration([ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
    "/GameServerWebSockets.xml", "/EmailServiceContext.xml" ])
class PromotionStompControllerIT {

    static final String SESSION_ID = "20";

    static final String SUBSCRIPTION_ID = "20";

    static final String SESSION_ATTRIBUTE = "PLAYER";

    static final String MESSAGE_DESTINATION = "/app/promote/";

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

    DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();

    DAO<Player> playerDAO;

    Player stephen;

    Player nobby;

    JsonChessGameView view;

    SimpMessagingTemplate template;

    ServerChessGameDAO serverChessGameDAO;

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

        serverChessGameDAO = (ServerChessGameDAO) wac.getBean("myServerChessGameDAO");

        ServerChessGameFactory scgfactory = (ServerChessGameFactory) wac
                .getBean("serverChessGameFactory");

        def entityCache = (EntityManagerCache) wac.getBean('myEntityManagerCache');
        entityCache.getEntityManager(gameUUID).close();
        
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7");
        def sGameFactory = new StandardChessGameFactory();
        def scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME, gameUUID, stephen);
        scg.addOpponent(nobby);
        scg.setChessGame(sGameFactory.getChessGame(board, scg.getPlayer(stephen), scg.getPlayer(nobby)));
        Move move = new Move("a7:a8");
        
        scg.move(scg.getPlayer(stephen), move);
        
        serverChessGameDAO.saveServerChessGame(scg);

        template = mock(SimpMessagingTemplate.class);
        view = new JsonChessGameView(template);
        view.setGameToObserver(scg);
    }

    @After
    public void tearDown() throws Exception {
        this.fixture.tearDown();
    }

    @Test
    public void testPromote() {
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;
        
        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, "promote qa8");
        println(testInfoMessageSent());
        
        game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.RUNNING;
        
        ChessPiece chessPiece =  game.getChessGame().getChessBoard().getPieceFromBoardAt(new Location("a8"));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.WHITE;
        assert chessPiece.getClass() == QueenPiece.class;
    }
    
    @Test
    public void testInvalidPromote() {
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;
        
        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, "promote qe1");
        println(testErrorMessageSent());
        
        game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;
        
        ChessPiece chessPiece =  game.getChessGame().getChessBoard().getPieceFromBoardAt(new Location("e1"));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.WHITE;
        assert chessPiece.getClass() == KingPiece.class;
    }

    private void promote(Player player, long gameUid, String messageDestination, String promoteString)
    throws Exception {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId(SUBSCRIPTION_ID);
        headers.setDestination(messageDestination + gameUid);
        headers.setSessionId(SESSION_ID);
        headers.setUser(getTestPrincipal());
        Map<String, Object> sessionAttributes = new HashMap<String, Object>();
        sessionAttributes.put(SESSION_ATTRIBUTE, player);
        headers.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = MessageBuilder.createMessage(promoteString.getBytes(),
                headers.getMessageHeaders());
        System.out.println(message);
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
                        return "stephen";
                    }
                };
        return p;
    }
    
    private String testInfoMessageSent() throws Exception {
        testMessageSent(MessageType.INFO);
    }

    private String testErrorMessageSent() throws Exception {
        testMessageSent(MessageType.ERROR);
    }

    private String testMessageSent(MessageType type) throws Exception {
        Message<?> message = this.brokerChannelInterceptor.awaitMessage(5);
        assertNotNull("Message sent is null", message);
        
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = message.getHeaders()
            .get("nativeHeaders", Map.class);
        
        assertEquals(type, MessageType.valueOf(nativeHeaders.get(MESSAGE_HEADER_TYPE).get(0)));
        return new String(message.getPayload());
    }
}
