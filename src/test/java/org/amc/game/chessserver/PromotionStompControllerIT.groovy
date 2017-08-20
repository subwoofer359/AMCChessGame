package org.amc.game.chessserver

import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.amc.dao.DAO;
import org.amc.dao.EntityManagerCache;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
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
import org.apache.openjpa.kernel.exps.This;
import org.junit.*;
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

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(['/EntityManagerFactory.groovy', '/SpringTestConfig.xml', '/GameServerSecurity.xml',
    '/GameServerWebSockets.xml', '/EmailServiceContext.xml' ])
class PromotionStompControllerIT extends StompControllerFixtureIT {
	
	def scgfactory;
	
	static final def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
    
	def sGameFactory = new StandardChessGameFactory();
	
    static final String MESSAGE_DESTINATION = '/app/promote/';
    
    private static final Long ONEVIEW_GAMEUUID  = 9409409L;
	
	private static final String TWOVIEW_BOARD_CONFIG = 'Ke7:ke1:pa7';
	
	private static final String ONEVIEW_BOARD_CONFIG = 'Ke8:ke2:pa7:Pa2';
    
	@Before
    void setup() {
        super.setup();
        clearDAO();
		scgfactory = (ServerChessGameFactory) wac.getBean('serverChessGameFactory');
    }

    @After
    void tearDown() {
        super.tearDown();
    }

    private void clearDAO() {
        def entityCache = (EntityManagerCache) wac.getBean('myEntityManagerCache');
        entityCache.getEntityManager(gameUUID).close();
    }

    private void createChessGame() {
        def scg = setupGame(GameType.NETWORK_GAME);
     
        scg.move(scg.getPlayer(stephen), new Move('a7:a8'));
        serverChessGameDAO.saveServerChessGame(scg);
    }
    
    private void createOneViewChessGame() {
		def scg = setupGame(GameType.LOCAL_GAME);
        
        scg.chessGame.changePlayer();
        
        scg.move(scg.getPlayer(nobby), new Move('a2:a1'));
        serverChessGameDAO.saveServerChessGame(scg);
    }
	
	private AbstractServerChessGame setupGame(GameType gameType) {
		def gUUID;
		def view;
		
		if(GameType.LOCAL_GAME == gameType) {
			gUUID = ONEVIEW_GAMEUUID;
			view = ONEVIEW_BOARD_CONFIG;
		} else {
			gUUID = gameUUID;
			view = TWOVIEW_BOARD_CONFIG;
		}
	
		def board  = chessBoardFactory.getChessBoard(view);
		def scg = scgfactory.getServerChessGame(gameType, gUUID, stephen);
		
		scg.addOpponent(nobby);
		scg.setChessGame(sGameFactory.getChessGame(board, scg.getPlayer(stephen), scg.getPlayer(nobby)));
		
		return scg;
	}

    @Test
    void testWhitePromote() {
        createChessGame()
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, 'promote qa8');
        consumePromotionStatusUpdate();
        testInfoMessageSent();

        game = serverChessGameDAO.getServerChessGame(gameUUID);
        

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().get(new Location('a8'));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.WHITE;
        assert chessPiece.getClass() == QueenPiece.class;
        assert game.getChessGame().gameState == GameState.RUNNING;
    }

    @Test
    void testBlackPromote() {
        createOneViewChessGame();
        def game = serverChessGameDAO.getServerChessGame(ONEVIEW_GAMEUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(nobby, ONEVIEW_GAMEUUID, MESSAGE_DESTINATION, 'promote Qa1');
        consumePromotionStatusUpdate();
        testInfoMessageSent();

        game = serverChessGameDAO.getServerChessGame(ONEVIEW_GAMEUUID);
        

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().get(new Location('a1'));
        assert chessPiece != null;
        assert chessPiece.colour == Colour.BLACK;
        assert chessPiece.getClass() == QueenPiece.class;
        assert game.getChessGame().gameState == GameState.RUNNING;
    }
    
    
    private void consumePromotionStatusUpdate() {
        testStatusMessageSent();
    }
    
    @Test
    void testInvalidPromote() {
        createChessGame()
        def game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        subscribe();
        promote(stephen, gameUUID, MESSAGE_DESTINATION, 'promote qe1');
        consumePromotionStatusUpdate();
        testErrorMessageSent();

        game = serverChessGameDAO.getServerChessGame(gameUUID);
        assert game.getChessGame().gameState == GameState.PAWN_PROMOTION;

        ChessPiece chessPiece =  game.getChessGame().getChessBoard().get(new Location('e1'));
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
    
    @Controller
	static class PromotionStompControllerHelper extends PromotionStompController {

		static final String TEST_MESSAGE = 'Testing message';
	
		@Override
		@MessageMapping('/promote/{gameUUID}')
    	@SendToUser(value = '/queue/updates', broadcast = false)
		void promotePawnTo(Principal user,
        	    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
            	@DestinationVariable long gameUUID, @Payload String promotionMessage) {
			super.promotePawnTo(user, wsSession, gameUUID, promotionMessage);
			sendMessageToUser(user, TEST_MESSAGE, MessageType.INFO);
		}

	
}
    
}
