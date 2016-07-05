package org.amc.game.chessserver.messaging;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;

public class PlayerQuitChessGameEmail extends PlayerChessGameEmail {

	static final String EMAIL_TEMPLATE = "quitGameEmail.html";

	public PlayerQuitChessGameEmail() {
		super();
		setEmailTemplateName(EMAIL_TEMPLATE);
		setEmailSubject(DEFAULT_EMAIL_SUBJECT);

	}

	public PlayerQuitChessGameEmail(Player player, AbstractServerChessGame serverChessGame) {
		this();
		setPlayer(player);
		setServerChessGame(serverChessGame);

	}
}
