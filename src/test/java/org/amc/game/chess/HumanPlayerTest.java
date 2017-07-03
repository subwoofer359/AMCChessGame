package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.Test;

public class HumanPlayerTest {

	@Test
	public void test() {
		Player player = new HumanPlayer();
		assertNotNull(player.getName());
		assertNotNull(player.getUserName());
	}
	
	@Test
	public void testSetUserName() {
		final String userName = "ted";
		Player player = new HumanPlayer();
		player.setUserName(userName);
		assertEquals(userName, player.getUserName());
	}

	@Test
	public void testConstructorName() {
		final String name = "Ted Bing";
		Player player = new HumanPlayer(name);
		assertEquals(name, player.getName());
	}
	
	@Test
	public void testsetName() {
		final String name = "Ted Bing";
		HumanPlayer player = new HumanPlayer();
		player.setName(name);
		assertEquals(name, player.getName());
	}
}
