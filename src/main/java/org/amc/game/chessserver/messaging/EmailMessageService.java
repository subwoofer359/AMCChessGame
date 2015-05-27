package org.amc.game.chessserver.messaging;

import org.amc.game.chess.Player;

import javax.mail.Message;

public class EmailMessageService implements GameMessageService<Message> {

    public EmailMessageService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void send(Player player, Message message) throws Exception {
        // TODO Auto-generated method stub
    }

}
