package org.amc.game.chessserver.messaging;

import static org.amc.game.chess.ChessBoard.Coordinate.B;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chessserver.DatabaseFixture;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerConstants;
import org.amc.game.chessserver.messaging.EmailMessageService.SendMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Tests Email integration with Google's gmail service
 * @author Adrian Mclaughlin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/EntityManagerFactory.groovy", "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/EmailServiceContext.xml", "/GameServerSecurity.xml" })
public class GmailEmailMessagingIT {

	private static final String GAME_TYPE = "gameType";

    private static final String CHESSBOARD_CONFIG = "Ka6:kc6:qb1";

    private static final String USER_FULLNAME = "Adrian McLaughlin";

    private static final String USER_USERNAME = "adrian";

    private static final String USER_EMAIL_ADDR = "subwoofer359@gmail.com";

    private static final String MESSAGE_SERVICE = "messageService";

    private static final String GAME_UUID = "gameUUID";

    private static final String JOIN_GAME = "/joinGame";

    private static final String USER_NAME = "userName";

    private static final String NOBBY = "nobby";

    private static final String STEPHEN = "stephen";

    private static final String MY_PLAYER_DAO = "myPlayerDAO";

    private static final String EMAIL_TEMPLATE_FACTORY = "emailTemplateFactory";

    private static final String CREATE_GAME = "/createGame/";
	
    private static final String SESSION_ATTRIBUTE = "PLAYER";
    
    private DatabaseFixture signUpfixture = new DatabaseFixture();

    private ChessGamePlayer whitePlayer;

    private ChessGamePlayer blackPlayer;

    private long gameUUID;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    private DAO<Player> playerDAO;
    
    private EmailTemplateFactory factory;
    
    
    @Before
    public void setUp() throws Exception {
    	this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        
        setUpPlayers();
        
        factory = (EmailTemplateFactory)wac.getBean(EMAIL_TEMPLATE_FACTORY);
    }
    
    @SuppressWarnings("unchecked")
    private void setUpPlayers() throws Exception {
        playerDAO = (DAO<Player>)wac.getBean(MY_PLAYER_DAO);
        
        Player stephen = getPlayerFromDatabase(STEPHEN);
        Player nobby = getPlayerFromDatabase(NOBBY);
 
        whitePlayer = new RealChessGamePlayer(stephen, Colour.WHITE);
        blackPlayer = new RealChessGamePlayer(nobby, Colour.BLACK);
    }
    
    private Player getPlayerFromDatabase(String name) throws Exception {
        Player player = playerDAO.findEntities(USER_NAME, name).get(0);
        playerDAO.getEntityManager().detach(player);
        return player;
    }
    
    @After
    public void tearDown() {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testMove() throws Exception {
         
        MvcResult result = createAndJoinChessGame();   
        AbstractServerChessGame serverChessGame = doMove(result);
        send(serverChessGame);
        
    }
    
    private MvcResult createAndJoinChessGame() throws Exception {
        MvcResult result = this.mockMvc
                        .perform(post(CREATE_GAME).sessionAttr(SESSION_ATTRIBUTE, whitePlayer)
                        .param(GAME_TYPE, GameType.NETWORK_GAME.name()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model()
                        .attributeExists(ServerConstants.GAME_UUID))
                        .andReturn();
        
        this.gameUUID = (long) result.getModelAndView().getModelMap()
                        .get(ServerConstants.GAME_UUID);
        
        result = this.mockMvc.perform(
                        post(JOIN_GAME).param(GAME_UUID, String.valueOf(this.gameUUID))
                        .sessionAttr(ServerConstants.PLAYER, blackPlayer))
                        .andExpect(status()
                        .isOk())
                        .andDo(print()).andReturn();
        return result;
    }
    
    private AbstractServerChessGame doMove(MvcResult result) throws Exception {
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard(CHESSBOARD_CONFIG);
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        
        AbstractServerChessGame scg = (AbstractServerChessGame)result.getModelAndView().getModel().get("GAME");
        
        scg.getChessGame().setChessBoard(board);
        
        scg.move(whitePlayer, queenMove);
        
        return scg;
    }
    
    private void  send(AbstractServerChessGame serverChessGame) throws Exception {
        EmailMessageService service = (EmailMessageService)wac.getBean(MESSAGE_SERVICE);
        User user = getUser();
        Future<String> mailresult = service.send(user, getEmailTemplate(serverChessGame));
        Future<String> mailresult2 = service.send(user, getPlayerJoinedGameEmailTemplate(serverChessGame));
        Future<String> mailresult3 = service.send(user, getPlayerQuitGameEmailTemplate(serverChessGame));
        
        assertEquals(SendMessage.SENT_SUCCESS, mailresult.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult2.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult3.get());
    }
    
    private User getUser() {
        User user = new User();
        user.setEmailAddress(USER_EMAIL_ADDR);
        user.setUserName(USER_USERNAME);
        user.setName(USER_FULLNAME);
        return user;
    }
    
    private EmailTemplate getEmailTemplate(AbstractServerChessGame serverChessGame) throws Exception {
        EmailTemplate template = factory.getEmailTemplate(ChessGame.class);
        template.setPlayer(whitePlayer);
        template.setServerChessGame(serverChessGame);
        return template;
    }
    
    private EmailTemplate getPlayerQuitGameEmailTemplate(AbstractServerChessGame serverChessGame) {
        EmailTemplate playerQuitGameEmail = factory.getEmailTemplate(Player.class, ServerGameStatus.FINISHED);
        playerQuitGameEmail.setPlayer(blackPlayer);
        playerQuitGameEmail.setServerChessGame(serverChessGame);
        return playerQuitGameEmail;  
    }
    
    private EmailTemplate getPlayerJoinedGameEmailTemplate(AbstractServerChessGame serverChessGame) {
        EmailTemplate playerJoinedGameEmail = factory.getEmailTemplate(Player.class);
        playerJoinedGameEmail.setPlayer(blackPlayer);
        playerJoinedGameEmail.setServerChessGame(serverChessGame);
        return playerJoinedGameEmail;
    }
}
