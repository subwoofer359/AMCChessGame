package org.amc.game.chessserver.messaging;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;

public abstract class EmailTemplate {
	
	static final String TEMPLATE_BACKGROUND_TAG = "background";
    
    static final String BACKGROUND_IMAGE_RESOURCE = "background";
    
    final String backgroundImagePath = "src/main/webapp/img/1700128.jpg";//"webapps/AMCChessGame/img/1700128.jpg";

	private static final String IMAGE_TYPE = "image/jpg";
	
	private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
	
	private SpringTemplateEngine templateEngine;

    private ServerChessGame serverChessGame;

    private Player otherPlayer;
    
    private Map<String, EmbeddedMailImage> images = new HashMap<String,EmbeddedMailImage>();
    
    private String emailSubject;
    
    public EmailTemplate() {
        emailSubject = DEFAULT_EMAIL_SUBJECT;
    }
    
    public EmailTemplate(Player player, ServerChessGame serverChessGame){
        this();
        this.serverChessGame = serverChessGame;
        this.otherPlayer = player;
    }
    
    public abstract String getEmailHtml();

    protected void addTempEmbeddedImage(String contentId, File filePath) {
        images.put(contentId, new EmbeddedMailImage(contentId, filePath, IMAGE_TYPE));
    }
    
    protected void addEmbeddedImage(String contentId, File filePath) {
        images.put(contentId, new EmbeddedMailImage(contentId, filePath, IMAGE_TYPE, false));
    }
        
    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public TemplateEngine getTemplateEngine() {
    	return this.templateEngine;
    }
    
    public ServerChessGame getServerChessGame() {
        return this.serverChessGame;
    }
    
    public Player getPlayer() {
        return otherPlayer;
    }

    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
    }

    public void setPlayer(Player player) {
        this.otherPlayer = player;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
    
    public Map<String, EmbeddedMailImage> getEmbeddedImages() {
        return images;
    }   
}