package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompConstants.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.DAO;
import org.amc.dao.DatabaseGameMap;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.After;
import org.junit.Before;
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
import java.util.Map;

import javax.persistence.OptimisticLockException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
        "/GameServerWebSockets.xml", "/EmailServiceContext.xml" })
public class StompControllerIntegrationTest {

    private static final String SESSION_ID = "20";

    private static final String SUBSCRIPTION_ID = "20";

    private static final String SESSION_ATTRIBUTE = "PLAYER";

    private static final String MESSAGE_DESTINATION = "/app/move/";
    private static final String SAVE_MESSAGE_DESTINATION = "/app/save/";

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

    private final long gameUUID = 1234L;

    private final long oneViewChessGameUUID = 4321L;

    private String[] moves = { "A2-A3", "A7-A6", "B2-B3", "B7-B6", "C2-C3", "C7-C6", "D2-D3", 
                    "D7-D6", "E2-E3", "E7-E6", "F2-F3", "F7-F6", "G2-G3", "G7-G6" ,"H2-H3", "H7-H6" };

    private DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();

    private DAO<Player> playerDAO;

    private Player stephen;

    private Player nobby;

    private DatabaseGameMap gameMap;

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

        gameMap = (DatabaseGameMap) wac.getBean("gameMap");
        gameMap.clearCache();

        ServerChessGameFactory scgfactory = (ServerChessGameFactory) wac
                        .getBean("serverChessGameFactory");

        ServerChessGame scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME, gameUUID, stephen);
        scg.addOpponent(nobby);
        scg = gameMap.put(gameUUID, scg);

        ServerChessGame oneViewChessGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, oneViewChessGameUUID,
                        stephen);
        oneViewChessGame.addOpponent(nobby);

        gameMap.put(oneViewChessGameUUID, oneViewChessGame);
        
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

        for (int i = 0; i < moves.length; i++) {
            ServerChessGame scg = gameMap.get(gameUUID);
            twoViewMove(scg.getChessGame().getCurrentPlayer(), gameUUID, moves[i]);
            testInfoMessageSent();
            Thread.sleep(200);
            verifyMove(scg, moves[i]);
        }
    }

    private void verifyMove(ServerChessGame scg, String moveString) {
        scg = gameMap.get(scg.getUid());
        Move actualMove = scg.getChessGame().getTheLastMove();
        Move expectedMove = new Move(moveString);
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
        for (int i = 0; i < moves.length; i++) {
            ServerChessGame oneViewChessGame = gameMap.get(oneViewChessGameUUID);
            oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID,
                            moves[i]);
            testInfoMessageSent();

            verifyMove(oneViewChessGame, moves[i]);
        }

    }

    @Test
    public void OneViewInvalidMove() throws Exception {
        subscribe();
        ServerChessGame oneViewChessGame = gameMap.get(gameUUID);
        oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID,
                        "A2-A5");
        testErrorMessageSent();
        assertEmptyMove(oneViewChessGame);
    }

    private void assertEmptyMove(ServerChessGame scg) {
        assertEquals(Move.EMPTY_MOVE, scg.getChessGame().getTheLastMove());
    }

    @Test
    public void TwoViewInvalidMove() throws Exception {
        subscribe();
        ServerChessGame scg = gameMap.get(gameUUID);
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

    private void move(Player player, long gameUid, String messageDestination, String moveString)
                    throws Exception {
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
    
    @Test
    public void saveTest() throws Exception {
        subscribe();
        ServerChessGameDAO dao = new ServerChessGameDAO();
        ObserverFactoryChain chain = (ObserverFactoryChain)wac.getBean("defaultObserverFactoryChain");
        dao.setObserverFactoryChain(chain);
        
        for(String move : moves) {         
            ServerChessGame scg = gameMap.get(gameUUID);
            dao.detachEntity(scg);
            move(scg.getChessGame().getCurrentPlayer(), 
                            gameUUID, MESSAGE_DESTINATION, move);
            
            Thread.sleep(200);
            saveGame(scg);
            testInfoMessageSent();
            Thread.sleep(200);
            ServerChessGame savedGame = dao.getServerChessGame(gameUUID);
            dao.detachEntity(savedGame);
            if (savedGame == null) {
                Thread.sleep(200);
                savedGame = dao.getServerChessGame(gameUUID);
                
            }
            assertNotNull(savedGame);
            ChessBoardUtilities.compareBoards(scg.getChessGame().getChessBoard(), savedGame.getChessGame().getChessBoard());
        }
      
    }
    
    @Test
    public void saveTestOpt() throws Exception {
        subscribe();
        ServerChessGameDAO dao = new ServerChessGameDAO();
        ObserverFactoryChain chain = (ObserverFactoryChain)wac.getBean("defaultObserverFactoryChain");
        dao.setObserverFactoryChain(chain);
        
        for(String move : moves) {         
            saveGameTest(dao, move, 10);
        }
        
        Thread.sleep(200);
        ServerChessGame savedGame = dao.getServerChessGame(gameUUID);
        Move move = savedGame.getChessGame().getTheLastMove();
        System.out.println("MOVE:" + move);
        assertTrue(move.equals(new Move(moves[moves.length-1])));
        
        
    }
    
    private void saveGameTest(ServerChessGameDAO dao, String move, int delayInMillis) throws Exception {
        ServerChessGame scg = gameMap.get(gameUUID);
        dao.detachEntity(scg);
        move(scg.getChessGame().getCurrentPlayer(), gameUUID, MESSAGE_DESTINATION, move);

        Thread.sleep(delayInMillis);
        saveGame(scg);
        Thread.sleep(delayInMillis);
        testInfoMessageSent();
        ServerChessGame savedGame = null;
        
        
        int check = 0;
        do{
            if (savedGame == null) {
                Thread.sleep(100);
                savedGame = dao.getServerChessGame(gameUUID);
                check++;
            }
         } while(check < 5 && savedGame == null);
        dao.detachEntity(savedGame);
        assertNotNull(savedGame);
    }
    
    @DirtiesContext
    @Test
    public void saveOptimisticLockingExceptionThrownTest() throws Exception {
        subscribe();
        ServerChessGameDAO dao = spy(new ServerChessGameDAO());
        
        
        ObserverFactoryChain chain = (ObserverFactoryChain)wac.getBean("defaultObserverFactoryChain");
        
        
        dao.setObserverFactoryChain(chain);
        
        ServerChessGame scg = gameMap.get(gameUUID);
        saveGame(scg);
        Thread.sleep(200);
        
        StompController controller = (StompController)wac.getBean(StompController.class);
        controller.setServerChessDAO(dao);
        doThrow(new OptimisticLockException()).doReturn(scg).when(dao).saveServerChessGame(any(ServerChessGame.class));
        
        saveGameTest(dao, moves[0], 10);
       
        
        ServerChessGame savedGame = dao.getServerChessGame(gameUUID);
        assertTrue(savedGame.getChessGame().getTheLastMove().equals(Move.EMPTY_MOVE));
        
        
    }
    
    
    private void saveGame(ServerChessGame scg) {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId(SUBSCRIPTION_ID);
        headers.setDestination(SAVE_MESSAGE_DESTINATION + scg.getUid());
        headers.setSessionId(SESSION_ID);
        headers.setUser(getTestPrincipal());
        Map<String, Object> sessionAttributes = new HashMap<String, Object>();
        sessionAttributes.put(SESSION_ATTRIBUTE, scg.getChessGame().getCurrentPlayer());
        headers.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = MessageBuilder.createMessage("save".getBytes(),
                        headers.getMessageHeaders());
        this.clientInboundChannel.send(message);
    }
}
