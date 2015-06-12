package org.amc.game.chessserver.messaging;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

public class EmailTemplate {

    private static final Logger logger = Logger.getLogger(EmailTemplate.class);

    private SpringTemplateEngine templateEngine;

    private ServerChessGame serverChessGame;

    private Player otherPlayer;
    
    private ChessBoardSVGImage chessBoardImage;

    private static final String EMAIL_TEMPLATE = "gameStatus.html";
    
    private final String backgroundImagePath="src/main/webapp/img/1700128.jpg";
    
    private File chessBoardImageURL;
    
    static final String TEMPLATE_BACKGROUND_TAG = "background";
    
    static final String BACKGROUND_IMAGE_RESOURCE = "background";
    
    static final String TEMPLATE_CHESSBOARD_TAG = "svg";
    
    static final String CHESSBOARD_IMAGE_RESOURCE = "svg";
    
    private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
    
    private String emailSubject;
    
    public EmailTemplate() {
        emailSubject = DEFAULT_EMAIL_SUBJECT;
    }
    
    public EmailTemplate(Player player, ServerChessGame serverChessGame){
        this();
        this.serverChessGame = serverChessGame;
        this.otherPlayer = player;
    }

    
    
    public String getEmailHtml() {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", otherPlayer.getName());
        ctx.setVariable("status", serverChessGame.getCurrentStatus().toString());
        ctx.setVariable("opponent", serverChessGame.getChessGame()
                        .getOpposingPlayer(serverChessGame.getPlayer(otherPlayer)));
        try {
        	this.chessBoardImageURL = createChessBoardImage();
            ctx.setVariable(TEMPLATE_CHESSBOARD_TAG, CHESSBOARD_IMAGE_RESOURCE);
            ctx.setVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return this.templateEngine.process(EMAIL_TEMPLATE, ctx);
    }

    private File createChessBoardImage() throws IOException {
    	chessBoardImage.setServerChessGame(this.serverChessGame);
    	return chessBoardImage.getChessBoardImage();
    }
    
    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    ServerChessGame getServerChessGame() {
        return this.serverChessGame;
    }
    
    Player getPlayer() {
        return otherPlayer;
    }

    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
    }

    public void setPlayer(Player player) {
        this.otherPlayer = player;
    }
    
    public File getImageFile() {
    	return this.chessBoardImageURL;
    }
    
    @Autowired
    public void setChessBoardSVGImage(ChessBoardSVGImage chessboardImage) {
    	this.chessBoardImage = chessboardImage;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }    
}
