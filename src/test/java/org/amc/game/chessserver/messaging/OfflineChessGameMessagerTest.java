package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

public class OfflineChessGameMessagerTest {

    private OfflineChessGameMessager offlineGMessager;
    private EmailMessageService emailService;
    private EmailTemplateFactory templateFactory;
    private SessionRegistry registry;
    private DAO<User> userDAO;
    private JavaMailSender mailSender;

    private ArgumentCaptor<User> userCaptor;
    private ArgumentCaptor<EmailTemplate> emailTemplateCaptor;

    private Player player;
    private Player opponentPlayer;
    private User opponentUser;
    private final long GAME_UID = 3332L;
    private List<Object> userSessionList;
    private List<User> userList;
    
    private final List<Object> emptyUserSessionList = new ArrayList<Object>();

    private ServerChessGame serverChessGame;
    private Move move;

    @Before
    public void setUp() throws Exception {
        
        player = new HumanPlayer("Ted");
        
        opponentPlayer = new HumanPlayer("Chris Murton");
        opponentPlayer.setUserName("chris");
        opponentUser = new User();
        opponentUser.setName(opponentPlayer.getName());
        opponentUser.setEmailAddress("Chris@adrianmclaughlin.ie");
        opponentUser.setUserName(opponentPlayer.getUserName());

        offlineGMessager = new OfflineChessGameMessager();
        
        emailService = mock(EmailMessageService.class);
        registry = mock(SessionRegistry.class);
        userDAO = mock(DAO.class);
        mailSender = mock(JavaMailSender.class);

        //To capture values passed to mock objects
        userCaptor = ArgumentCaptor.forClass(User.class);
        emailTemplateCaptor = ArgumentCaptor.forClass(EmailTemplate.class);

        emailService.setMailSender(mailSender);
        
        templateFactory = mock(EmailTemplateFactory.class);
        when(templateFactory.getEmailTemplate(ChessGame.class)).thenReturn(new MoveUpdateEmail());
        when(templateFactory.getEmailTemplate(Player.class)).thenReturn(new PlayerJoinedChessGameEmail());

        offlineGMessager.setMessageService(emailService);
        offlineGMessager.setSessionRegistry(registry);
        offlineGMessager.setUserDAO(userDAO);
        offlineGMessager.setEmailTemplateFactory(templateFactory);

        userList = Arrays.asList(opponentUser);

        //Emulate an authenticated User
        userSessionList = Arrays
                        .asList((Object) new org.springframework.security.core.userdetails.User(
                                        opponentUser.getUserName(), "password",
                                        new ArrayList<GrantedAuthority>()));

        when(userDAO.findEntities("userName", opponentPlayer.getUserName())).thenReturn(userList);

        serverChessGame = new ServerChessGame(GAME_UID, player);
        serverChessGame.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
            
            @Override
            public ChessGame getChessGame() {
                return null;
            }
        });
        
        serverChessGame.attachObserver(offlineGMessager);

        move = new Move(new Location(ChessBoard.Coordinate.A, 2), new Location(
                        ChessBoard.Coordinate.A, 3));

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEmailSentWhenPlayerOffline() throws Exception {
        when(registry.getAllPrincipals()).thenReturn(emptyUserSessionList);

        serverChessGame.addOpponent(opponentPlayer);
        serverChessGame.move(serverChessGame.getPlayer(player), move);

        verify(emailService).send(userCaptor.capture(), emailTemplateCaptor.capture());
        assertEquals(userCaptor.getValue().getUserName(), opponentUser.getUserName());
        assertEquals(emailTemplateCaptor.getValue().getServerChessGame(), serverChessGame);
        assertTrue(ComparePlayers.comparePlayers(opponentPlayer, emailTemplateCaptor.getValue()
                        .getPlayer()));
    }

    @Test
    public void testNoEmailSentWhenPlayerOnline() throws Exception {
        when(registry.getAllPrincipals()).thenReturn(userSessionList);
        
        serverChessGame.addOpponent(opponentPlayer);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        verify(emailService, never()).send(userCaptor.capture(), emailTemplateCaptor.capture());

    }

    @Test
    public void testNoUserExists() throws DAOException, IllegalMoveException, MessagingException,
                    MailException {
        final List<User> emptyUserList = new ArrayList<User>();
        when(userDAO.findEntities("userName", opponentPlayer.getUserName())).thenReturn(emptyUserList);
        
        serverChessGame.addOpponent(opponentPlayer);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        verify(emailService, never()).send(userCaptor.capture(), emailTemplateCaptor.capture());
    }
    
    @Test
    public void testNoEmailAddress() throws Exception {
        opponentUser.setEmailAddress(null);
        when(registry.getAllPrincipals()).thenReturn(emptyUserSessionList);
        
        serverChessGame.addOpponent(opponentPlayer);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        verify(emailService, never()).send(userCaptor.capture(), emailTemplateCaptor.capture());
    }
    
    @Test
    public void testHandlePlayerUpdate() throws Exception {
        when(userDAO.findEntities("userName", player.getUserName())).thenReturn(userList);
        when(registry.getAllPrincipals()).thenReturn(emptyUserSessionList);
        serverChessGame.addOpponent(opponentPlayer);
        verify(emailService).send(userCaptor.capture(), emailTemplateCaptor.capture());
    }
    
    @Test
    public void testUpdateThrowsMessagingException() throws Exception {
        doThrow(MessagingException.class).when(emailService).send(any(User.class),any(EmailTemplate.class));
        serverChessGame.addOpponent(opponentPlayer);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
    }
}
