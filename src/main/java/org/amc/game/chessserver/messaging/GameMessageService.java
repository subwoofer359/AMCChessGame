package org.amc.game.chessserver.messaging;

import org.amc.game.chess.Player;

public interface GameMessageService<T> {
    void send(Player player, T message) throws Exception;
}
