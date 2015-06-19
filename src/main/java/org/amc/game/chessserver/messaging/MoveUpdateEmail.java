package org.amc.game.chessserver.messaging;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;

public class MoveUpdateEmail extends EmailTemplate {

	private static final Logger logger = Logger.getLogger(MoveUpdateEmail.class);
    
    private ChessBoardSVGImage chessBoardImage;

    private static final String EMAIL_TEMPLATE = "gameStatus.html";
    
    private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
    
    static final String TEMPLATE_CHESSBOARD_TAG = "svg";
    
    static final String CHESSBOARD_IMAGE_RESOURCE = "svg";
        
    public MoveUpdateEmail() {
    	setEmailSubject(DEFAULT_EMAIL_SUBJECT);
    	addEmbeddedImage(BACKGROUND_IMAGE_RESOURCE, new File(backgroundImagePath));
    }
    
    public MoveUpdateEmail(Player player, ServerChessGame serverChessGame) {
    	this();
    	setPlayer(player);
    	setServerChessGame(serverChessGame);
    }
    
    public String getEmailHtml() {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", getPlayer().getName());
        ctx.setVariable("status", getServerChessGame().getCurrentStatus().toString());
        ctx.setVariable("opponent", getServerChessGame().getChessGame()
                        .getOpposingPlayer(getServerChessGame().getPlayer(getPlayer())));
        try {
        	addTempEmbeddedImage(CHESSBOARD_IMAGE_RESOURCE, createChessBoardImage());
            
        	ctx.setVariable(TEMPLATE_CHESSBOARD_TAG, CHESSBOARD_IMAGE_RESOURCE);
            ctx.setVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return getTemplateEngine().process(EMAIL_TEMPLATE, ctx);
    }

    private File createChessBoardImage() throws IOException {
    	chessBoardImage.setServerChessGame(getServerChessGame());
    	return chessBoardImage.getChessBoardImage();
    }
    
    @Autowired
    public void setChessBoardSVGImage(ChessBoardSVGImage chessboardImage) {
    	this.chessBoardImage = chessboardImage;
    }
}
