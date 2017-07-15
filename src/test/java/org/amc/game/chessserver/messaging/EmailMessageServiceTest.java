package org.amc.game.chessserver.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.User;
import org.amc.game.chessserver.messaging.EmailMessageService.SendMessage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.ServletContext;


public class EmailMessageServiceTest {
	
	private static final String EMAIL_TEXT = "<html><body><h1>Test Email</h1></body></html>";
    
	private static final int POOL_SIZE = 3;
	
	private static final String CONTEXT_PATH = "/context/path/";
	
    private static ThreadPoolTaskExecutor executor;
    
    @Mock
    private JavaMailSender mailSender;
    
    @Mock
    private EmailTemplate emailTemplate;
    
    private EmailMessageService service;
    
    private User user;
    
    @Mock
    private MimeMessage mailMessage;
    
    @Mock
    private ServletContext servletContext;
    
    private String imageFile = "/false.jpg";
    
    private Map<String, EmbeddedMailImage> images;
    
   
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
    	MockitoAnnotations.initMocks(this);
    	
        service = new EmailMessageService();
        
        service.setMailSender(mailSender);
        
        service.setTaskExecutor(executor);

        when(mailSender.createMimeMessage()).thenReturn(mailMessage);

        when(emailTemplate.getEmailHtml()).thenReturn(EMAIL_TEXT);
        
        when(emailTemplate.getEmailSubject()).thenReturn("Test Message");
        
        images = new HashMap<>();
        EmbeddedServletImage aImage = new EmbeddedServletImage("TEST", imageFile, "image/jpg");
        aImage.setServletContext(servletContext);
        
        images.put("TEST", aImage);
        
        when(emailTemplate.getEmbeddedImages()).thenReturn(images);
        
        when(servletContext.getContextPath()).thenReturn(CONTEXT_PATH);
        when(servletContext.getRealPath(anyString())).thenReturn(CONTEXT_PATH + imageFile);

        user = new User();
        user.setEmailAddress("adrian@adrianmclaughlin.ie");
        user.setUserName("adrian");
        user.setName("Adrian Mc");
    }


    @Test
    public void testFromAddress() throws Exception{
        Future<String>  status = service.send(user, emailTemplate);
        
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
    public void testSendFail() throws Exception {
    	MailException me = mock(MailException.class);
    	doThrow(me).when(mailSender).send(any(MimeMessage.class));
    	Future<String>  status = service.send(user, emailTemplate);
        status.get();
        
        assertEquals(status.get(), SendMessage.SENT_FAILED);
    }
    
    @Test
    public void testImagePath() throws Exception {
    	SendMessage sendMessage = new SendMessage(mailSender, user, emailTemplate);
    	sendMessage.call();
    }
    
    @Test
    public void testTempFileDeleted() throws Exception {
    	EmbeddedServletImage emb = mock(EmbeddedServletImage.class);
    	File f = spy(new File("/tmp/l"));
    	
    	when(emb.getImageSource()).thenReturn(f);
    	when(emb.getContentId()).thenReturn("contentId");
    	when(emb.isToBeDeleted()).thenReturn(true);
    	
    	images.clear();
    	images.put("TEST", emb);  	
  
    	SendMessage sendMessage = new SendMessage(mailSender, user, emailTemplate);
    	sendMessage.call();
    	
        verify(f, times(1)).delete();
    }
}
