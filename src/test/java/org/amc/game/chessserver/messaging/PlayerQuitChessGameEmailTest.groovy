package org.amc.game.chessserver.messaging

import static org.junit.Assert.*;

import java.io.File;

import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.junit.Before;
import org.junit.Test;

class PlayerQuitChessGameEmailTest {
	final static Long UID = 23330L; 
	Player player = [name: "adrian", userName: "adrian"] as HumanPlayer;
	Player chessPlayer = new RealChessGamePlayer(player, Colour.WHITE);
	AbstractServerChessGame scg = [
		getPlayer: {
			return chessPlayer;
		},
		getUid: {
			return UID;
		}
		
		
	] as AbstractServerChessGame;

	@Test
	public void testConstructor() {
		PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail();
		
		assert email.emailTemplateName == PlayerQuitChessGameEmail.EMAIL_TEMPLATE;
		assert email.emailSubject == PlayerQuitChessGameEmail.DEFAULT_EMAIL_SUBJECT;
	}
	
	@Test
	public void testConstructorWithParameters() {
		PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail(player, scg);
		
		assert email.emailTemplateName == PlayerQuitChessGameEmail.EMAIL_TEMPLATE;
		assert email.emailSubject == PlayerQuitChessGameEmail.DEFAULT_EMAIL_SUBJECT;
		assert email.player == player;
		assert email.serverChessGame == scg;
	}
	
	@Test
	public void testAddContextVariables() {
		PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail(player, scg);
		email.addContextVariables();
		assert email.getContextVariable('name') == scg.player.name;
		assert email.getContextVariable('player') == player.name;
		assert email.getContextVariable('GAME_UUID') == String.valueOf(scg.uid);
		assertNotNull(email.getContextVariable(EmailTemplate.TEMPLATE_BACKGROUND_TAG)); 
		
	}
	
	@Test
	public void testAddImages() {
		PlayerQuitChessGameEmail email = new PlayerQuitChessGameEmail(player, scg);
		email.addImages();
		assertTrue(email.getEmbeddedImages().containsKey(EmailTemplate.BACKGROUND_IMAGE_RESOURCE));
		
	}

}
