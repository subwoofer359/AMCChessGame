package org.amc.game.chessserver.messaging;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;

public abstract class EmailTemplate {
	
	static final String TEMPLATE_BACKGROUND_TAG = "background";
    
    static final String BACKGROUND_IMAGE_RESOURCE = "background";
    
    static final String backgroundImagePath = "/img/1700128.jpg";;

    static final String IMAGE_TYPE = "image/jpg";
    
    private static String URL_ROOT = "";
	
	private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
	
	private TemplateEngineAdapter templateEngine;
	
	private MailImageFactory mailImageFactory;

    private AbstractServerChessGame serverChessGame;

    private Player otherPlayer = NO_PLAYER;
    
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
    
    protected void addEmbeddedImage(EmbeddedMailImage embeddedMailImage) {
    	images.put(embeddedMailImage.getContentId(), embeddedMailImage);
    }
        
    @Autowired
    public void setTemplateEngine(TemplateEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    TemplateEngineAdapter getTemplateEngine() {
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
    
    public MailImageFactory getMailImageFactory() {
    	if(mailImageFactory == null) {
    		throw new NullPointerException("MailImageFactory not set");
    	}
		return mailImageFactory;
	}

	public void setMailImageFactory(MailImageFactory mailImageFactory) {
		this.mailImageFactory = mailImageFactory;
	}

	public static String getUrlRoot() {
        synchronized (EmailTemplate.class) {
            return EmailTemplate.URL_ROOT;
        }
    }
    
    public static void setUrlRoot(String urlRoot) {
        synchronized (EmailTemplate.class) {
            if(URL_ROOT == "") {
                URL_ROOT = urlRoot;
            }
        }
    }
}
