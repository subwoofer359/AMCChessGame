package org.amc.game.chess;


public class NoPlayer extends ChessGamePlayer
{
	private static final long serialVersionUID = 2640083742191053488L;
	
	private static final String PLAYER_NAME = "NO PLAYER";
	
	public static final NoPlayer NO_PLAYER = new NoPlayer();
	
	private NoPlayer() {
		super();
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public void setId(int id) {
		// Do nothing

	}

	@Override
	public String getName() {
		return PLAYER_NAME;
	}

	@Override
	public String getUserName() {
		return PLAYER_NAME;
	}

	@Override
	public void setUserName(String userName) {
		// do nothing

	}

	@Override
	public Colour getColour() {
		return Colour.NONE;
	}
	
	@Override
	public Class<?> getType() {
		return getClass();
	}
	

}
