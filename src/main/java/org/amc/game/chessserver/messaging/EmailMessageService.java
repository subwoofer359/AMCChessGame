package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collection;
import java.util.Iterator;
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
		SendMessage sendTask = new SendMessage(mailSender, user, message);
		return executor.submit(sendTask);
	}
	

	
	static class SendMessage implements Callable<String> {
		public static final String SENT_SUCCESS = "Message sent";
		public static final String SENT_FAILED = "Message failed";
		
		private JavaMailSender mailSender;
		private User user;
		private EmailTemplate message;

		public SendMessage(JavaMailSender mailSender, User user, EmailTemplate message) {
			this.mailSender = mailSender;
			this.user = user;
			this.message = message;
		}
		
		@Override
		public String call() {
			try {
				log.info("MessageService: Creating message for " + message.getPlayer());
				MimeMessage emailMessage = createMessage(user.getEmailAddress(), message);

				log.info("MessageService: Sending message");

				this.mailSender.send(emailMessage);
				
				log.info("MessageService: Sent message:" + emailMessage.toString());
				deleteImages(message);
			} catch (MailException | MessagingException e) {
				log.error(e);
				return SENT_FAILED;
			} catch (RuntimeException re) {
				throw re;
			} catch (Exception e) {
				log.error(e);
				return SENT_FAILED;
			}
			return SENT_SUCCESS;
		}

		private final MimeMessage createMessage(String toAddress, EmailTemplate messageTextBody)
				throws MessagingException {
			final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			message.setFrom(EMAIL_SENDER_ADDR);
			message.setTo(toAddress);
			message.setSubject(messageTextBody.getEmailSubject());
			message.setText(messageTextBody.getEmailHtml(), true);
			
			log.info("Adding images to Email");
			addImagesToMessage(message, messageTextBody);
			return mimeMessage;
		}

		private final void addImagesToMessage(MimeMessageHelper message, EmailTemplate messageTextBody)
				throws MessagingException {
			for (String key : messageTextBody.getEmbeddedImages().keySet()) {
				EmbeddedMailImage embeddedImage = messageTextBody.getEmbeddedImages().get(key);
				
				
				FileSystemResource imageResource = new FileSystemResource(embeddedImage.getImageSource());
				
				message.addInline(embeddedImage.getContentId(), imageResource, embeddedImage.getContentType());
			}
		}

		/*
		 * Needs safety checks for delete
		 */
		private void deleteImages(EmailTemplate message) {
			Collection<EmbeddedMailImage> embeddedImages = message.getEmbeddedImages().values();
			Iterator<EmbeddedMailImage> iterator = embeddedImages.iterator();
			
			while (iterator.hasNext()) {
				EmbeddedMailImage image = iterator.next();
				if (image.isToBeDeleted() && image.getImageSource() != null) {
					if (!image.getImageSource().delete()) {
						log.error(String.format("File:%s couldn't be deleted", image.getImageSource().getName()));
					}
				}
			}
		}
	}
}
