package org.amc.game.chessserver.messaging;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;

public class PlayerJoinedChessGameEmail extends PlayerChessGameEmail {
	static final String EMAIL_TEMPLATE = "joinGameEmail.html";
	
	public PlayerJoinedChessGameEmail() {
		super();
		setEmailTemplateName(EMAIL_TEMPLATE);
		setEmailSubject(DEFAULT_EMAIL_SUBJECT);
		
	}
	
	public PlayerJoinedChessGameEmail(Player player, AbstractServerChessGame serverChessGame){
		this();
	    setPlayer(player);
	    setServerChessGame(serverChessGame);
	    
    }

	@Override
    public void addContextVariables() {
	    super.addContextVariables();
        addContextVariable("gameUrl", getUrlRoot() + getServerChessGame().getUid());
	}
}
