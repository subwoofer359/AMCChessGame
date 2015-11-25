package org.amc.game.chessserver.messaging;

import javax.servlet.ServletContext;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.UrlViewChessGameController;
import org.apache.log4j.Logger;
import org.thymeleaf.spring4.SpringTemplateEngine;

public abstract class EmailTemplateFactory {

    private static final Logger logger = Logger.getLogger(EmailTemplateFactory.class);
    
	private SpringTemplateEngine templateEngine;
	
	/**
	 * The URL part to view the chess game using {@link UrlViewChessGameController}
	 */
	static final String URL_APP_PATH = "/app/chessgame/urlview/";
	
	/**
	 * Full URL path to be used by {@link UrlViewChessGameController}
	 * To be embedded in Emails
	 */
	private String urlBase;

	public EmailTemplate getEmailTemplate(Class<?> clss) {
		if (clss.equals(Player.class)) {
			PlayerJoinedChessGameEmail email = new PlayerJoinedChessGameEmail();
			email.setTemplateEngine(templateEngine);
			email.setURLBase(urlBase);
			return email;
		} else if (clss.equals(ChessGame.class)) {
			MoveUpdateEmail email = new MoveUpdateEmail();
			email.setChessBoardSVGFactory(getChessBoardSVGFactory());
			email.setTemplateEngine(templateEngine);
			email.setURLBase(urlBase);
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
		this.templateEngine = templateEngine;
	}
	
	public void setServletContext(ServletContext servletContext) {
		if(servletContext == null) {
			logger.error(this.getClass() + ":Servlet property not set");
		} else {
			this.urlBase = servletContext.getContextPath() + URL_APP_PATH;
		}
	}

	public abstract ChessBoardSVGFactory getChessBoardSVGFactory();

	public static class FactoryInstantinationException extends RuntimeException {

        private static final long serialVersionUID = 5174849128024896037L;

	}
}
