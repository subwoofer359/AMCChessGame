package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailMessageService implements GameMessageService<EmailTemplate> {

    private static final Logger log = Logger.getLogger(EmailMessageService.class);
    static final String EMAIL_SENDER_ADDR = "chessgame@adrianmclaughlin.ie";
    
    private JavaMailSender mailSender;
    
    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
  
    
    @Override
    public void send(User user, EmailTemplate message) throws MailException, MessagingException {
        log.debug("MessageService: Creating message");
        MimeMessage emailMessage = createMessage(user.getEmailAddress(), message);
        log.debug("MessageService: Sending message");
        this.mailSender.send(emailMessage);
        log.debug("MessageService: Sent message:"+ emailMessage.toString());
    }
    
    private final MimeMessage createMessage(String toAddress,EmailTemplate messageTextBody)throws MessagingException{
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setFrom(EMAIL_SENDER_ADDR);
        message.setTo(toAddress);
        message.setSubject(messageTextBody.getEmailSubject());
        message.setText(messageTextBody.getEmailHtml(), true);
        FileSystemResource imageResource = new FileSystemResource(messageTextBody.getImageFileName());
        message.addInline(EmailTemplate.CHESSBOARD_IMAGE_RESOURCE, imageResource, "image/jpg");
        FileSystemResource backgoundImageResource = new FileSystemResource(messageTextBody.getBackgroundImagePath());
        message.addInline(EmailTemplate.BACKGROUND_IMAGE_RESOURCE, backgoundImageResource, "image/jpg");
        
        return mimeMessage;
    }
}
