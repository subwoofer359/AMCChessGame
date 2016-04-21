package org.amc.game.chessserver.messaging;

import static org.amc.game.chess.ChessBoard.Coordinate.B;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

import org.amc.EntityManagerThreadLocal;
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
import org.amc.game.chessserver.DatabaseSignUpFixture;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "/SpringTestConfig.xml" ,"/GameServerWebSockets.xml", "/EmailServiceContext.xml", "/GameServerSecurity.xml" })
public class EmailMessagingIT {

	private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
	
    private static final String SESSION_ATTRIBUTE = "PLAYER";

    private ChessGamePlayer whitePlayer;

    private ChessGamePlayer blackPlayer;

    private long gameUUID;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SimpMessagingTemplate template;

    private MockMvc mockMvc;

    private DAO<Player> playerDAO;
    
    
    @Before
    public void setUp() throws Exception {
    	this.signUpfixture.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        playerDAO = (DAO<Player>)wac.getBean("myPlayerDAO");
        Player stephen = playerDAO.findEntities("userName", "stephen").get(0);
        Player nobby = playerDAO.findEntities("userName", "nobby").get(0);
        
        //detaching player from EntityManager to prevent exception on new Em.flush()
        EntityManagerThreadLocal.getEntityManager().detach(stephen);
        EntityManagerThreadLocal.getEntityManager().detach(nobby);
        
        whitePlayer = new RealChessGamePlayer(stephen, Colour.WHITE);
        
        blackPlayer = new RealChessGamePlayer(nobby, Colour.BLACK);
        
        
    }
    
    @After
    public void tearDown() {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testMove() throws Exception {
        MvcResult result = this.mockMvc
                        .perform(post("/createGame/").sessionAttr(SESSION_ATTRIBUTE, whitePlayer)
                        .param("gameType", GameType.NETWORK_GAME.name()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(model()
                        .attributeExists(ServerConstants.GAME_UUID))
                        .andReturn();
        
        this.gameUUID = (long) result.getModelAndView().getModelMap()
                        .get(ServerConstants.GAME_UUID);
        
        result = this.mockMvc.perform(
                        post("/joinGame").param("gameUUID", String.valueOf(this.gameUUID))
                        .sessionAttr(ServerConstants.PLAYER, blackPlayer))
                        .andExpect(status()
                        .isOk())
                        .andDo(print()).andReturn();  
        
        EmailTemplateFactory factory = (EmailTemplateFactory)wac.getBean("emailTemplateFactory");
        EmailTemplate template = factory.getEmailTemplate(ChessGame.class);
        template.setPlayer(whitePlayer);
        
        EmailTemplate playerJoinedGameEmail = factory.getEmailTemplate(Player.class);
        EmailTemplate playerQuitGameEmail = factory.getEmailTemplate(Player.class, ServerGameStatus.FINISHED);
        
        playerJoinedGameEmail.setPlayer(blackPlayer);
        playerQuitGameEmail.setPlayer(blackPlayer);
        
        ChessBoard board = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation()).getChessBoard("Ka6:kc6:qb1");
        Move queenMove = new Move(new Location(B, 1), new Location(B, 6));
        
        AbstractServerChessGame scg = (AbstractServerChessGame)result.getModelAndView().getModel().get("GAME");
        
        scg.getChessGame().setChessBoard(board);
        
        scg.move(whitePlayer, queenMove);
        
        template.setServerChessGame(scg);
        playerJoinedGameEmail.setServerChessGame(scg);
        playerQuitGameEmail.setServerChessGame(scg);
        
        EmailMessageService service = (EmailMessageService)wac.getBean("messageService");
        User user =new User();
        user.setEmailAddress("subwoofer359@gmail.com");
        user.setUserName("adrian");
        user.setName("Adrian McLaughlin");
        
        Future<String> mailresult = service.send(user, template);
        Future<String> mailresult2 = service.send(user, playerJoinedGameEmail);
        Future<String> mailresult3 = service.send(user, playerQuitGameEmail);
        
        assertEquals(SendMessage.SENT_SUCCESS, mailresult.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult2.get());
        assertEquals(SendMessage.SENT_SUCCESS, mailresult3.get());
    }

    
}
