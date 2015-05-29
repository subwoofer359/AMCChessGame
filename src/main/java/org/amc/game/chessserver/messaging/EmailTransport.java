package org.amc.game.chessserver.messaging;

import javax.mail.Message;

import javax.mail.MessagingException;

public interface EmailTransport {
    public void send(Message message) throws MessagingException;
}
