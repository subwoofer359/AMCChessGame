package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailMessageService implements GameMessageService<EmailTemplate> {

    private static final Logger log = Logger.getLogger(EmailMessageService.class);
    static final String EMAIL_SENDER_ADDR = "chessgame@adrianmclaughlin.ie";
    
    private JavaMailSender mailSender;
    
    private ThreadPoolTaskExecutor executor;
    
    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
  
    @Autowired
    public void setTaskExecutor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }
    
    @Override
    public Future<String> send(User user, EmailTemplate message) throws MailException, MessagingException {
        Callable<String> sendTask = new SendMessage(mailSender, user, message);
        return executor.submit(sendTask);
    }
    
    
    
    
    static class SendMessage implements Callable<String> {

        private JavaMailSender mailSender;
        private User user;
        private EmailTemplate message;
        public static final String SENT_SUCCESS="Message sent";
        public static final String SENT_FAILED="Message failed";
        
        public SendMessage(JavaMailSender mailSender, User user, EmailTemplate message) {
            this.mailSender = mailSender;
            this.user = user;
            this.message = message;
        }
        
        @Override
        public String call() throws Exception {
            try {
                log.debug("MessageService: Creating message");
                MimeMessage emailMessage = createMessage(user.getEmailAddress(), message);
                log.debug("MessageService: Sending message");
                synchronized (mailSender) {
                    this.mailSender.send(emailMessage);
                }
                deleteImages(message);
                log.debug("MessageService: Sent message:"+ emailMessage.toString());
            } catch (MailException e) {
                log.error(e);
                e.printStackTrace();
                return SENT_FAILED;
            } catch (MessagingException e) {
                log.error(e);
                e.printStackTrace();
                return SENT_FAILED;
            }
            return SENT_SUCCESS;
        }
       
        private final MimeMessage createMessage(String toAddress,EmailTemplate messageTextBody)throws MessagingException{
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(EMAIL_SENDER_ADDR);
            message.setTo(toAddress);
            message.setSubject(messageTextBody.getEmailSubject());
            message.setText(messageTextBody.getEmailHtml(), true);
            FileSystemResource imageResource = new FileSystemResource(messageTextBody.getImageFile());
            message.addInline(EmailTemplate.CHESSBOARD_IMAGE_RESOURCE, imageResource, "image/jpg");
            FileSystemResource backgoundImageResource = new FileSystemResource(messageTextBody.getBackgroundImagePath());
            message.addInline(EmailTemplate.BACKGROUND_IMAGE_RESOURCE, backgoundImageResource, "image/jpg");
            
            return mimeMessage;
        }
        
        private void deleteImages(EmailTemplate message){
        	message.getImageFile().delete();
        }
    }
}
