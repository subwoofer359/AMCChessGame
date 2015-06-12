package org.amc.game.chessserver.messaging;

import org.amc.User;
import org.springframework.mail.MailException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.mail.MessagingException;

public interface GameMessageService<T> {
    Future<String> send(User user, T message) throws MailException, MessagingException;
}
