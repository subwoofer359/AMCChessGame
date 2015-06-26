package org.amc.game.chessserver.messaging;

import java.io.File;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;

public class PlayerQuitChessGameEmail extends EmailTemplate {
	
	private static final Logger logger = Logger.getLogger(PlayerQuitChessGameEmail.class);
	static final String EMAIL_TEMPLATE = "quitGameEmail.html";
	
	private static final int GAMEUID_LENGTH = 5;
	
	static final String DEFAULT_EMAIL_SUBJECT = "Game update from AMCChessGame";
	
	static final String NO_VALID_UID_MESSAGE = "No Game Uid";
	
	public PlayerQuitChessGameEmail() {
		super();
		setEmailTemplateName(EMAIL_TEMPLATE);
		setEmailSubject(DEFAULT_EMAIL_SUBJECT);
		
	}
	
	public PlayerQuitChessGameEmail(Player player, ServerChessGame serverChessGame){
		this();
	    setPlayer(player);
	    setServerChessGame(serverChessGame);
	    
    }

	@Override
    public void addContextVariables() {
	    addContextVariable("name", getServerChessGame().getPlayer().getName());
	    addContextVariable("player", getPlayer().getName());
	    String gameUid = String.valueOf(getServerChessGame().getUid());
        String gameUidFragment = gameUid.length() > GAMEUID_LENGTH ? gameUid.substring(gameUid.length() - GAMEUID_LENGTH, gameUid.length()) : NO_VALID_UID_MESSAGE;
        addContextVariable("GAME_UUID", gameUidFragment);
        addContextVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
	}

    @Override
    public void addImages() {
        addEmbeddedImage(BACKGROUND_IMAGE_RESOURCE, new File(backgroundImagePath));
    }
	
}