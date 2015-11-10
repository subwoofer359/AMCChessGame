package org.amc.game.chessserver;

import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.apache.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Deprecated
@Component
public class MoveConverter extends SimpleMessageConverter {

	private static final Logger logger = Logger.getLogger(MoveConverter.class);

	public MoveConverter() {
		super();
		logger.info("=====================MoveConverter====================================");
	}

	@Override
	public Object fromMessage(Message<?> message, Class<?> targetClass) {
		if (Move.class.equals(targetClass)) {
			String moveString = new String((byte[]) message.getPayload(),
					Charset.defaultCharset());
			String[] locations = moveString.split("-");
			return new Move(new Location(Coordinate.valueOf(locations[0]
					.substring(0, 1)), Integer.parseInt(locations[0].substring(
					1, 2))), new Location(Coordinate.valueOf(locations[1]
					.substring(0, 1)), Integer.parseInt(locations[1].substring(
					1, 2))));
		} else {
			return super.fromMessage(message, targetClass);
		}
	}
}
