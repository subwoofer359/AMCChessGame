package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;

public interface GameMessageService<T> {
    void send(User user, T message) throws MailException, MessagingException;
}
