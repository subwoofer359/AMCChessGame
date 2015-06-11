package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.ChessBoard;
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
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

public class OfflineChessGameMessagerTest {

    private OfflineChessGameMessager offlineGMessager;
    private EmailMessageService emailService;
    private SessionRegistry registry;
    private DAO<User> userDAO;
    private JavaMailSender mailSender;
    
    private ArgumentCaptor<User> userCaptor;
    private ArgumentCaptor<EmailTemplate> emailTemplateCaptor;
    
    private Player player;
    private Player opponent;
    private User opponentUser;
    private final long GAME_UID = 3332L;
    private List<Object> userSessionList;
    private List<User> userList;
    
    private ServerChessGame serverChessGame;
    private Move move;
    
    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Ted");
        opponent = new HumanPlayer("Chris");
        opponentUser = new User();
        opponentUser.setEmailAddress("user@adrianmclaughlin.ie");
        opponentUser.setName("Chris Murton");
        opponentUser.setUserName(opponent.getUserName());
        
        offlineGMessager = new OfflineChessGameMessager();
        emailService = mock(EmailMessageService.class);
        registry = mock(SessionRegistry.class);
        userDAO = mock(DAO.class);
        mailSender = mock(JavaMailSender.class);
        
        userCaptor = ArgumentCaptor.forClass(User.class);
        emailTemplateCaptor = ArgumentCaptor.forClass(EmailTemplate.class);
        
        emailService.setMailSender(mailSender);
        
        offlineGMessager.setMessageService(emailService);
        offlineGMessager.setSessionRegistry(registry);
        offlineGMessager.setUserDAO(userDAO);
        
        userList = Arrays.asList(opponentUser);
        userSessionList = Arrays.asList((Object)opponentUser);
        
        when(userDAO.findEntities("userName", opponent.getUserName())).thenReturn(userList);
        
        serverChessGame = new ServerChessGame(GAME_UID, player);
        serverChessGame.addOpponent(opponent);
        serverChessGame.attachObserver(offlineGMessager);
        
        move =new Move(
                        new Location(ChessBoard.Coordinate.A,2),
                        new Location(ChessBoard.Coordinate.A,3)
                        );
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEmailSentWhenPlayerOffline() throws Exception {
        final List<Object> emptyUserSessionList = new ArrayList<Object>();
        when(registry.getAllPrincipals()).thenReturn(emptyUserSessionList);
        
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        
        verify(emailService).send(userCaptor.capture(), emailTemplateCaptor.capture());
        assertEquals(userCaptor.getValue().getUserName(), opponentUser.getUserName());
        assertEquals(emailTemplateCaptor.getValue().getServerChessGame(), serverChessGame);
        assertTrue(ComparePlayers.comparePlayers(opponent, emailTemplateCaptor.getValue().getPlayer()));
    }

    @Test
    public void testNoEmailSentWhenPlayerOnline() throws Exception {
        when(registry.getAllPrincipals()).thenReturn(userSessionList);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        verify(emailService, never()).send(userCaptor.capture(), emailTemplateCaptor.capture());
        
    }
    
    @Test
    public void testNoUserExists() throws DAOException, IllegalMoveException, MessagingException, MailException {
        final List<User> emptyUserList = new ArrayList<User>();
        when(userDAO.findEntities("userName", opponent.getUserName())).thenReturn(emptyUserList);
        serverChessGame.move(serverChessGame.getPlayer(player), move);
        verify(emailService, never()).send(userCaptor.capture(), emailTemplateCaptor.capture());
    }
}

