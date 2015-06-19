package org.amc.game.chessserver.messaging;

import java.io.File;
import java.util.Locale;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.thymeleaf.context.Context;

public class PlayerJoinedChessGameEmail extends EmailTemplate {
	
	private static final Logger logger = Logger.getLogger(PlayerJoinedChessGameEmail.class);
	private static final String EMAIL_TEMPLATE = "joinGameEmail.html";
	
	private static final int GAMEUID_LENGTH = 5;
	
	private static final String DEFAULT_EMAIL_SUBJECT = "Game update from AMCChessGame";
	
	private static final String NO_VALID_UID_MESSAGE = "No Game Uid";
	
	public PlayerJoinedChessGameEmail() {
		super();
		setEmailSubject(DEFAULT_EMAIL_SUBJECT);
		addEmbeddedImage(BACKGROUND_IMAGE_RESOURCE, new File(backgroundImagePath));
	}
	
	public PlayerJoinedChessGameEmail(Player player, ServerChessGame serverChessGame){
		this();
	    setPlayer(player);
	    setServerChessGame(serverChessGame);
	    
    }

	@Override
	public String getEmailHtml() {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", getPlayer().getName());
 
        ctx.setVariable("player", getPlayer().getName());
        String gameUid = String.valueOf(getServerChessGame().getUid());
        String gameUidFragment = gameUid.length() > GAMEUID_LENGTH ? gameUid.substring(gameUid.length() - GAMEUID_LENGTH, gameUid.length()) : NO_VALID_UID_MESSAGE;
        ctx.setVariable("GAME_UUID", gameUidFragment);
        ctx.setVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
        return getTemplateEngine().process(EMAIL_TEMPLATE, ctx);
	}
	
	
}
