package org.amc.game.chessserver.messaging;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

public abstract class EmailTemplate {
	
	static final String TEMPLATE_BACKGROUND_TAG = "background";
    
    static final String BACKGROUND_IMAGE_RESOURCE = "background";
    
    static final String backgroundImagePath;
    
    static {
        if(System.getProperty("user.dir").contains("workspace")) {
            
            backgroundImagePath = "src/main/webapp/img/1700128.jpg";
        } else {
            backgroundImagePath = "webapps/AMCChessGame/img/1700128.jpg";
        }
    }

    private static String URL_ROOT;
    
	private static final String IMAGE_TYPE = "image/jpg";
	
	private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
	
	private SpringTemplateEngine templateEngine;

    private AbstractServerChessGame serverChessGame;

    private Player otherPlayer;
    
    private Map<String, EmbeddedMailImage> images = new HashMap<>();
    
    private Map<String, Object> contextVariables = new HashMap<>();
    
    private String emailSubject;
    
    private String emailTemplateName = "";
    
    public EmailTemplate() {
        emailSubject = DEFAULT_EMAIL_SUBJECT;
    }
    
    public EmailTemplate(Player player, AbstractServerChessGame serverChessGame){
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
    
    public AbstractServerChessGame getServerChessGame() {
        return this.serverChessGame;
    }
    
    public Player getPlayer() {
        return otherPlayer;
    }

    public void setServerChessGame(AbstractServerChessGame serverChessGame) {
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
    
    public Object getContextVariable(String name) {
    	return this.contextVariables.get(name);
    }
    
    public String getEmailTemplateName() {
    	return emailTemplateName;
    }
    
    public void setEmailTemplateName(String emailTemplateName) {
        this.emailTemplateName = emailTemplateName;
    }
    
    public static String getUrlRoot() {
        synchronized (EmailTemplate.class) {
            return EmailTemplate.URL_ROOT;
        }
        
    }
    
    public static void setUrlRoot(String urlRoot) {
        synchronized (EmailTemplate.class) {
            if(URL_ROOT == null) {
                URL_ROOT = urlRoot;
            }
        }
    }
}