package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailMessageService implements GameMessageService<EmailTemplate> {

    private static final Logger log = Logger.getLogger(EmailMessageService.class);
    static final String EMAIL_SENDER_ADDR = "chessgame@adrianmclaughlin.ie";
    static final String EMAIL_SUBJECT = "This is the message subject";
    
    private JavaMailSender mailSender;
    
    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
  
    @Override
    public void send(User user, EmailTemplate message) throws MailException, MessagingException {
        MimeMessage emailMessage = createMessage(user.getEmailAddress(), message);
        this.mailSender.send(emailMessage);
        log.debug("Sent message:"+ emailMessage.toString());
    }
    
    private final MimeMessage createMessage(String toAddress,EmailTemplate messageTextBody)throws MessagingException{
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom(EMAIL_SENDER_ADDR);
        message.setTo(toAddress);
        message.setSubject(EMAIL_SUBJECT);
        message.setText(messageTextBody.getEmailHtml(), true);
        return mimeMessage;
    }
}
