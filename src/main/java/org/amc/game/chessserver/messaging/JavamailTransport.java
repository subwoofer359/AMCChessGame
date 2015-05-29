package org.amc.game.chessserver.messaging;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class JavamailTransport implements EmailTransport {

    public void send(Message message) throws MessagingException{
        Transport.send(message);
    }
}
