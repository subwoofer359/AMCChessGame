package org.amc.game.chess;

import javax.persistence.Entity;


@Entity
public class ComputerPlayer extends HumanPlayer {

	private static final long serialVersionUID = 77937995958579445L;

	public static final String NAME = "COMPUTER";
	
	public static final String USERNAME = "COMPUTER";
	
	public ComputerPlayer() {
		setName(NAME);
		super.setUserName(USERNAME);
	}

	@Override
	public void setUserName(String userName) {
		//do nothing
	}

}
