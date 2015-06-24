package org.amc.game.chessserver.messaging;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

public abstract class EmailTemplate {
	
	static final String TEMPLATE_BACKGROUND_TAG = "background";
    
    static final String BACKGROUND_IMAGE_RESOURCE = "background";
    
    final String backgroundImagePath = "webapps/AMCChessGame/img/1700128.jpg";

    /*
     * location of image for local testing
     */
    //final String backgroundImagePath = "src/main/webapp/img/1700128.jpg";
    
	private static final String IMAGE_TYPE = "image/jpg";
	
	private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
	
	private SpringTemplateEngine templateEngine;

    private ServerChessGame serverChessGame;

    private Player otherPlayer;
    
    private Map<String, EmbeddedMailImage> images = new HashMap<String,EmbeddedMailImage>();
    
    private Map<String, Object> contextVariables = new HashMap<String,Object>();
    
    private String emailSubject;
    
    private String emailTemplateName = "";
    
    public EmailTemplate() {
        emailSubject = DEFAULT_EMAIL_SUBJECT;
    }
    
    public EmailTemplate(Player player, ServerChessGame serverChessGame){
        this();
        this.serverChessGame = serverChessGame;
        this.otherPlayer = player;
    }
    
    public abstract void addContextVariables();
    
    public abstract void addImages();
    
    
    /**
     * Return a string containing the html markup for the email
     * Template method design pattern
     * Should be final but can't be due to testing
     * @return String
     */
    public String getEmailHtml() {
        addContextVariables();
        addImages();
        
        final Context ctx = new Context(Locale.getDefault());
        
        for(String key:contextVariables.keySet()) {
            ctx.setVariable(key, contextVariables.get(key));
        }

        return this.templateEngine.process(emailTemplateName, ctx);
    }

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
    
    SpringTemplateEngine getTemplateEngine() {
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
    
    public void addContextVariable(String name, Object value) {
        this.contextVariables.put(name, value);
    }
    
    public void setEmailTemplateName(String emailTemplateName) {
        this.emailTemplateName = emailTemplateName;
    }
}