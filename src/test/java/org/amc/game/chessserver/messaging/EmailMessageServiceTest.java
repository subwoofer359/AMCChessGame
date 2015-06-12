package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.amc.User;
import org.amc.game.chessserver.messaging.EmailMessageService.SendMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;


public class EmailMessageServiceTest {

    private JavaMailSender mailSender;
    private static ThreadPoolTaskExecutor executor;
    private EmailTemplate emailTemplate;
    private EmailMessageService service;
    private User user;
    private MimeMessage mailMessage;
    private File imageFile = mock(File.class);
    private static final String EMAIL_TEXT = "<html><body><h1>Test Email</h1></body></html>";
    private static final int POOL_SIZE = 3;
    
    @BeforeClass
    public static void setUpThreadPool() throws Exception {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.initialize();
    }
    
    @AfterClass
    public static void shutdownThreadPool() throws Exception {
        executor.shutdown();
    }
    
    @Before
    public void setUp() throws Exception {
        mailSender = mock(JavaMailSender.class);
        emailTemplate = mock(EmailTemplate.class);
        mailMessage = mock(MimeMessage.class);
        service = new EmailMessageService();
        service.setMailSender(mailSender);
        service.setTaskExecutor(executor);

        when(mailSender.createMimeMessage()).thenReturn(mailMessage);

        when(emailTemplate.getEmailHtml()).thenReturn(EMAIL_TEXT);
        
        when(emailTemplate.getEmailSubject()).thenReturn("Test Message");
        when(emailTemplate.getBackgroundImagePath()).thenReturn("/");
        when(emailTemplate.getImageFile()).thenReturn(imageFile);

        user = new User();
        user.setEmailAddress("adrian@adrianmclaughlin.ie");
        user.setUserName("adrian");
        user.setName("Adrian Mc");
    }

    @After
    public void tearDown() throws Exception {
       
    }

    @Test
    public void testFromAddress() throws Exception{
        Future<String>  status = service.send(user, emailTemplate);
        status.get();
        assertEquals(status.get(), SendMessage.SENT_SUCCESS);
        ArgumentCaptor<InternetAddress> fromAddressCaptor = ArgumentCaptor
                        .forClass(InternetAddress.class);
        verify(mailMessage).setFrom(fromAddressCaptor.capture());
        assertEquals(EmailMessageService.EMAIL_SENDER_ADDR, fromAddressCaptor.getValue()
                        .getAddress());
    }
    
    @Test
    public void testToAddress() throws Exception {
        Future<String>  status = service.send(user, emailTemplate);
        assertEquals(status.get(), SendMessage.SENT_SUCCESS);
        
        ArgumentCaptor<InternetAddress> toAddressCaptor = ArgumentCaptor
                        .forClass(InternetAddress.class);
        verify(mailMessage).setRecipient(eq(RecipientType.TO), toAddressCaptor.capture());
        assertEquals(user.getEmailAddress(), toAddressCaptor.getValue().getAddress());
    }
    
    @Test
    public void testSubjectLine() throws Exception {
        Future<String>  status = service.send(user, emailTemplate);
        status.get();
        assertEquals(status.get(), SendMessage.SENT_SUCCESS);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailMessage).setSubject(subjectCaptor.capture(),anyString());
        assertEquals(emailTemplate.getEmailSubject(), subjectCaptor.getValue());
    }
 
    @Test
    public void testTempFileDeleted() throws Exception {
    	Future<String>  status = service.send(user, emailTemplate);
    	assertEquals(status.get(), SendMessage.SENT_SUCCESS);
        verify(this.imageFile, times(1)).delete();
    }
}
