package org.amc.game.chess

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

class VirtualChessGamePlayerTest {

	def colour = Colour.BLACK;
	def name = 'Ted';
	
	def userName = 'TedsUserName';
	def virtualPlayer;
	def player;
	
	def proxyId = 0;
	def proxyUserName = userName;
	
	@Before
	void setUp() throws Exception {
		
		player = [getName: {name;}, 
					getUserName: {delegate.proxyUserName},
					setUserName: {delegate.proxyUserName = it},
					getId:{delegate.proxyId}, 
					setId:{delegate.proxyId = it}] as Player;
		virtualPlayer = new VirtualChessGamePlayer(player, colour);
	}

	@Test
	void testGetUserName() {
		assertEquals(userName, virtualPlayer.userName);
	}
	
	@Test
	void testSetUserName() {
		def newUserName = 'new UserName';
		virtualPlayer.userName = newUserName;
		assertEquals(newUserName, virtualPlayer.userName);
	}
	
	@Test
	void testGetName() {
		assertEquals(name, virtualPlayer.name);
	}
	
	@Test
	void testSetGetId() {
		def id = 4;
		
		virtualPlayer.setId(id);
		
		assertEquals(id, virtualPlayer.id);
	}
	
	@Test
	void toStringTest() {
		String output = virtualPlayer.toString();
		assertTrue(output.contains(virtualPlayer.name));
		assertTrue(output.contains(virtualPlayer.colour.toString()));
	}
}
