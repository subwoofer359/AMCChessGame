package org.amc.game.chessserver;

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.dao.EntityManagerCache;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoardUtilities;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.JsonChessGameView;
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
@ContextConfiguration({ "/SpringTestConfig.xml", "/GameServerSecurity.xml",
        "/GameServerWebSockets.xml", "/EmailServiceContext.xml" })
public class StompControllerIT {

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
            "D7-D6", "E2-E3", "E7-E6", "F2-F3", "F7-F6", "G2-G3", "G7-G6", "H2-H3", "H7-H6" };

    private DatabaseSignUpFixture fixture = new DatabaseSignUpFixture();

    private DAO<Player> playerDAO;

    private Player stephen;

    private Player nobby;

    private JsonChessGameView view;

    private SimpMessagingTemplate template;

    private ServerChessGameDAO serverChessGameDAO;

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
        clearDAO();

        ServerChessGameFactory scgfactory = (ServerChessGameFactory) wac
                        .getBean("serverChessGameFactory");

        AbstractServerChessGame scg = scgfactory.getServerChessGame(GameType.NETWORK_GAME,
                        gameUUID, stephen);
        scg.addOpponent(nobby);

        serverChessGameDAO.saveServerChessGame(scg);

        AbstractServerChessGame oneViewChessGame = scgfactory.getServerChessGame(
                        GameType.LOCAL_GAME, oneViewChessGameUUID, stephen);
        oneViewChessGame.addOpponent(nobby);

        serverChessGameDAO.saveServerChessGame(oneViewChessGame);

        template = mock(SimpMessagingTemplate.class);
        view = new JsonChessGameView(template);
        view.setGameToObserver(oneViewChessGame);
    }

    private void clearDAO() {
        EntityManagerCache entityCache = (EntityManagerCache) wac.getBean("myEntityManagerCache");
        entityCache.getEntityManager(gameUUID).close();
        entityCache.getEntityManager(oneViewChessGameUUID).close();
    }

    @After
    public void tearDown() throws Exception {
        this.fixture.tearDown();
    }

    @Test
    public void TwoViewChessGameMove() throws Exception {
        subscribe();

        for (int i = 0; i < moves.length; i++) {
            AbstractServerChessGame scg = serverChessGameDAO.getServerChessGame(gameUUID);
            twoViewMove(scg.getChessGame().getCurrentPlayer(), gameUUID, moves[i]);
            testInfoMessageSent();
            verifyMove(scg, moves[i]);
        }
    }

    private void verifyMove(AbstractServerChessGame scg, String moveString) throws DAOException {
        scg = serverChessGameDAO.getServerChessGame(scg.getUid());
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

        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = message.getHeaders().get("nativeHeaders",
                        Map.class);

        assertEquals(type, MessageType.valueOf(nativeHeaders.get(MESSAGE_HEADER_TYPE).get(0)));
    }

    @Test
    public void OneViewChessGameMove() throws Exception {
        subscribe();
        for (int i = 0; i < moves.length; i++) {
            AbstractServerChessGame oneViewChessGame = serverChessGameDAO
                            .getServerChessGame(oneViewChessGameUUID);
            oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID,
                            moves[i]);
            testInfoMessageSent();

            verifyMove(oneViewChessGame, moves[i]);
        }

    }

    @Test
    public void OneViewInvalidMove() throws Exception {
        subscribe();
        AbstractServerChessGame oneViewChessGame = serverChessGameDAO.getServerChessGame(gameUUID);
        oneViewMove(oneViewChessGame.getChessGame().getCurrentPlayer(), oneViewChessGameUUID,
                        "A2-A5");
        testErrorMessageSent();
        assertEmptyMove(oneViewChessGame);
    }

    private void assertEmptyMove(AbstractServerChessGame scg) {
        assertEquals(Move.EMPTY_MOVE, scg.getChessGame().getTheLastMove());
    }

    @Test
    public void TwoViewInvalidMove() throws Exception {
        subscribe();
        AbstractServerChessGame scg = serverChessGameDAO.getServerChessGame(gameUUID);
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
        AbstractServerChessGame scg = null;
        for (String move : moves) {
            scg = serverChessGameDAO.getServerChessGame(gameUUID);
            move(scg.getChessGame().getCurrentPlayer(), gameUUID, MESSAGE_DESTINATION, move);

            testInfoMessageSent();
            saveGame(scg);
            testInfoMessageSent();
            serverChessGameDAO.getEntityManager(gameUUID).close();
        }
        AbstractServerChessGame savedGame = serverChessGameDAO.getServerChessGame(gameUUID);

        ChessBoardUtilities.compareBoards(scg.getChessGame().getChessBoard(), savedGame
                        .getChessGame().getChessBoard());
        assertNotNull(savedGame);

    }

    @Test
    public void saveTestOpt() throws Exception {
        subscribe();

        for (String move : moves) {
            saveGameTest(serverChessGameDAO, move);
        }

        AbstractServerChessGame savedGame = serverChessGameDAO.getServerChessGame(gameUUID);
        Move move = savedGame.getChessGame().getTheLastMove();
        System.out.println("MOVE:" + move);
        assertTrue(move.equals(new Move(moves[moves.length - 1])));
    }

    private void saveGameTest(ServerChessGameDAO dao, String move) throws Exception {
        AbstractServerChessGame scg = serverChessGameDAO.getServerChessGame(gameUUID);
        dao.detachEntity(scg);
        move(scg.getChessGame().getCurrentPlayer(), gameUUID, MESSAGE_DESTINATION, move);
        testInfoMessageSent();
        saveGame(scg);
        testInfoMessageSent();
        AbstractServerChessGame savedGame = null;

        int check = 0;
        do {
            if (savedGame == null) {
                savedGame = dao.getServerChessGame(gameUUID);
                check++;
            }
        } while (check < 5 && savedGame == null);
        dao.detachEntity(savedGame);
        assertNotNull(savedGame);
    }

    private void saveGame(AbstractServerChessGame scg) {
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
