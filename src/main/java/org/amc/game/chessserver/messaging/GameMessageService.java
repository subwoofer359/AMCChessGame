package org.amc.game.chessserver.messaging;

import org.amc.User;

public interface GameMessageService<T> {
    void send(User user, T message) throws Exception;
}
