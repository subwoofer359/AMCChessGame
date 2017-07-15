package org.amc.game.chessserver.messaging;

public abstract class PlayerChessGameEmail extends EmailTemplate {
	
	static final int GAMEUID_LENGTH = 5;
	static final String DEFAULT_EMAIL_SUBJECT = "Game update from AMCChessGame";
	static final String NO_VALID_UID_MESSAGE = "No Game Uid";
	
	@Override
	public void addContextVariables() {
		addContextVariable("name", getServerChessGame().getPlayer().getName());
	    addContextVariable("player", getPlayer().getName());
	    
	    String gameUid = String.valueOf(getServerChessGame().getUid());
        String gameUidFragment = gameUid.length() >= GAMEUID_LENGTH ? 
        		gameUid.substring(gameUid.length() - GAMEUID_LENGTH, gameUid.length()) : 
        			NO_VALID_UID_MESSAGE;
        addContextVariable("GAME_UUID", gameUidFragment);
        addContextVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
		
	}
	@Override
	public void addImages() {
		 addEmbeddedImage(getMailImageFactory().getServletPathImage(BACKGROUND_IMAGE_RESOURCE,
				 backgroundImagePath, EmailTemplate.IMAGE_TYPE));
	}
	
	
}
