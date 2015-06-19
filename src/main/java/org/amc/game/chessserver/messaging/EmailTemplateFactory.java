package org.amc.game.chessserver.messaging;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.thymeleaf.spring4.SpringTemplateEngine;

public class EmailTemplateFactory {

	private SpringTemplateEngine templateEngine;

	private ChessBoardSVGImage chessBoardImage;

	public EmailTemplate getEmailTemplate(Class<?> clss) {
		if (clss.equals(Player.class)) {
			PlayerJoinedChessGameEmail email = new PlayerJoinedChessGameEmail();
			email.setTemplateEngine(templateEngine);
			return email;
		} else if (clss.equals(ChessGame.class)) {
			MoveUpdateEmail email = new MoveUpdateEmail();
			email.setChessBoardSVGImage(chessBoardImage);
			email.setTemplateEngine(templateEngine);
			return email;
		} else {
			throw new FactoryInstantinationException();
		}
	}

	public void setTemplateEngine(SpringTemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void setChessBoardImage(ChessBoardSVGImage chessBoardImage) {
		this.chessBoardImage = chessBoardImage;
	}

	public static class FactoryInstantinationException extends RuntimeException {

		private static final long serialVersionUID = 1L;

	}
}
