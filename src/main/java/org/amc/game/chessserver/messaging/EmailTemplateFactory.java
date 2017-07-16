package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.UrlViewChessGameController;
import org.apache.log4j.Logger;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.servlet.ServletContext;

public abstract class EmailTemplateFactory {

    private static final Logger logger = Logger.getLogger(EmailTemplateFactory.class);
    
	private TemplateEngineAdapter templateEngine;
	
	private MailImageFactory mailImageFactory;
	
	private String urlRoot = "";
	
	/**
	 * The URL part to view the chess game using {@link UrlViewChessGameController}
	 */


	public EmailTemplate getEmailTemplate(Class<?> clss) {
		if (clss.equals(Player.class)) {
			PlayerJoinedChessGameEmail email = new PlayerJoinedChessGameEmail();
			
			email.setTemplateEngine(templateEngine);
			email.setMailImageFactory(mailImageFactory);
			email.setUrlRoot(urlRoot);
			return email;
		} else if (clss.equals(ChessGame.class)) {
			MoveUpdateEmail email = new MoveUpdateEmail();
			
			email.setChessBoardSVGFactory(getChessBoardSVGFactory());
			email.setTemplateEngine(templateEngine);
			email.setMailImageFactory(mailImageFactory);
			email.setUrlRoot(urlRoot);
			return email;
		} else {
		    logger.error("Factory received object of invalid class:" + clss.getSimpleName());
			throw new FactoryInstantinationException();
		}
	}
	
	public EmailTemplate getEmailTemplate(Class<?> clss, ServerGameStatus status) {
	    if(Player.class.equals(clss)){
	        if(ServerGameStatus.FINISHED.equals(status)){
	            PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail();
	            
	            email.setTemplateEngine(templateEngine);
	            email.setMailImageFactory(mailImageFactory);
	            email.setUrlRoot(urlRoot);
	            return email;
	        } else {
	            logger.error("Factory received ServerGameStatus of invalid status:" + status.toString());
	            throw new FactoryInstantinationException();
	        }
	    }
	    else {
            logger.error("Factory received object of invalid class:" + clss.getSimpleName());
            throw new FactoryInstantinationException();
        }
	}

	public void setTemplateEngine(SpringTemplateEngine templateEngine) {
		this.templateEngine = new TemplateEngineAdapter(templateEngine);
	}
	
	public void setMailImageFactory(MailImageFactory mailImageFactory) {
		this.mailImageFactory = mailImageFactory;
	}
	
	public void setServletContext(ServletContext servletContext) {
		urlRoot = new StringBuilder("http://")
			.append((String)servletContext.getAttribute("HOSTIP"))
			.append(':')
			.append((String)servletContext.getAttribute("PORT"))
			.append(servletContext.getContextPath())
			.append(servletContext.getInitParameter("URL_APP_PATH"))
			.toString();
		
		
		logger.debug("URL_ROOT ------------>" + urlRoot);
	}

	public abstract ChessBoardSVGFactory getChessBoardSVGFactory();

	public static class FactoryInstantinationException extends RuntimeException {

        private static final long serialVersionUID = 5174849128024896037L;
	}	
}
