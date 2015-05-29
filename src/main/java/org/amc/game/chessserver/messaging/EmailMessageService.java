package org.amc.game.chessserver.messaging;

import org.amc.game.chess.Player;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailMessageService implements GameMessageService<String> {

    static final String TO_ADDRESS = "to.address";
    static final String SMTP_USERNAME = "smtp.username";
    static final String SMTP_PASSWORD = "smtp.password";
    static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    static final String MAIL_SMTP_HOST = "mail.smtp.host";
    static final String MAIL_SMTP_PORT = "mail.smtp.port";
    static final String SUBJECT_LINE = "AMCChessGame Update";
    
    private final static Logger log = Logger.getLogger(EmailMessageService.class);
    
    private static EmailTransport transport=new JavamailTransport();
    
    private Properties javamailProperties;
    
    public EmailMessageService() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Initialise the EmailDispatcher instance by load properties from a file
     * @param propertiesFilePath Path to Properties file
     * @throws IOException if file not found
     */
    public final void init(String propertiesFilePath) throws IOException{
        this.javamailProperties=new Properties();
        javamailProperties.load(getPropertyFileReader(propertiesFilePath));
        log.debug("Email Dispatcher initialised");
    }
    
    /**
     * Checks the file path and then creates a {@link java.io.Reader Reader} for that file.
     * 
     * @param propertiesFilePath String path of file
     * @return java.io.BufferedReader for the file
     * @throws IOException
     */
    private final Reader getPropertyFileReader(String propertiesFilePath) throws IOException
    {
        InputStream inputStream = this.getClass().getResourceAsStream(propertiesFilePath);
        if(inputStream==null){
            StringBuilder sb=new StringBuilder();
            sb.append(propertiesFilePath);
            sb.append(": Couldn't be found or opened");
            throw new IOException(sb.toString());
        }
        Reader reader=new BufferedReader(new InputStreamReader(inputStream));
        return reader;
    }
    
    static final void setEmailTransport(EmailTransport transport){
        EmailMessageService.transport=transport;
    }
    

    @Override
    public void send(Player player, String message) throws Exception {
        player.getUserName();
        Message emailMessage = createMessage(player.getUserName(), message);
    }
    
    private final Message createMessage(String fromAddress,String messageTextBody)throws MessagingException{
        final String toAddress=javamailProperties.getProperty(TO_ADDRESS);
        final String userName=javamailProperties.getProperty(SMTP_USERNAME);
        final String password=javamailProperties.getProperty(SMTP_PASSWORD);
        
        Session session=Session.getInstance(javamailProperties,new javax.mail.Authenticator(){
          protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(userName, password);
          }
        });
        
        Message message=new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(SUBJECT_LINE);
        message.setText(messageTextBody);
        
        return message;
    }

}
