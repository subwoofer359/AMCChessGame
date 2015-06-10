package org.amc.game.chessserver.messaging;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class EmailMessageServiceTest {

    private JavaMailSender mailSender;
    private EmailTemplate emailTemplate;
    private EmailMessageService service;
    private User user;
    private MimeMessage mailMessage;
    private static final String EMAIL_TEXT = "<html><body><h1>Test Email</h1></body></html>";

    @Before
    public void setUp() throws Exception {
        mailSender = mock(JavaMailSender.class);
        emailTemplate = mock(EmailTemplate.class);
        mailMessage = mock(MimeMessage.class);
        service = new EmailMessageService();
        service.setMailSender(mailSender);

        when(mailSender.createMimeMessage()).thenReturn(mailMessage);

        when(emailTemplate.getEmailHtml()).thenReturn(EMAIL_TEXT);

        user = new User();
        user.setEmailAddress("adrian@adrianmclaughlin.ie");
        user.setUserName("adrian");
        user.setName("Adrian Mc");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws MessagingException, MailException {
        service.send(user, emailTemplate);

        ArgumentCaptor<InternetAddress> fromAddressCaptor = ArgumentCaptor
                        .forClass(InternetAddress.class);
        verify(mailMessage).setFrom(fromAddressCaptor.capture());
        assertEquals(EmailMessageService.EMAIL_SENDER_ADDR, fromAddressCaptor.getValue()
                        .getAddress());

        ArgumentCaptor<InternetAddress> toAddressCaptor = ArgumentCaptor
                        .forClass(InternetAddress.class);
        verify(mailMessage).setRecipient(eq(RecipientType.TO), toAddressCaptor.capture());
        assertEquals(user.getEmailAddress(), toAddressCaptor.getValue().getAddress());

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailMessage).setSubject(subjectCaptor.capture());
        assertEquals(EmailMessageService.EMAIL_SUBJECT, subjectCaptor.getValue());

        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailMessage).setContent(textCaptor.capture(), anyString());
        assertEquals(EMAIL_TEXT, textCaptor.getValue());

    }

}
