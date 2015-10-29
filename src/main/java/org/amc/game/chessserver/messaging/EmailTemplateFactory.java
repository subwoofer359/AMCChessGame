package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.thymeleaf.spring4.SpringTemplateEngine;

public abstract class EmailTemplateFactory {

    private static final Logger logger = Logger.getLogger(EmailTemplateFactory.class);
    
	private SpringTemplateEngine templateEngine;

	public EmailTemplate getEmailTemplate(Class<?> clss) {
		if (clss.equals(Player.class)) {
			PlayerJoinedChessGameEmail email = new PlayerJoinedChessGameEmail();
			email.setTemplateEngine(templateEngine);
			return email;
		} else if (clss.equals(ChessGame.class)) {
			MoveUpdateEmail email = new MoveUpdateEmail();
			email.setChessBoardSVGFactory(getChessBoardSVGFactory());
			email.setTemplateEngine(templateEngine);
			return email;
		} else {
		    logger.error("Factory received object of invalid class:" + clss.getSimpleName());
			throw new FactoryInstantinationException();
		}
	}
	
	public EmailTemplate getEmailTemplate(Class<?> clss, ServerGameStatus status) {
	    if(clss.equals(Player.class)){
	        if(status.equals(ServerGameStatus.FINISHED)){
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

	public abstract ChessBoardSVGFactory getChessBoardSVGFactory();

	public static class FactoryInstantinationException extends RuntimeException {

        private static final long serialVersionUID = 5174849128024896037L;

	}
}
