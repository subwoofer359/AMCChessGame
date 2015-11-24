package org.amc.game.chess;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RealChessGamePlayer extends ChessGamePlayer {
	
	private static final long serialVersionUID = -2024403466742066351L;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
    		targetEntity=org.amc.game.chess.HumanPlayer.class)
    private Player player = null;
	
	protected RealChessGamePlayer() {
	}
	
	public RealChessGamePlayer(Player player, Colour colour) {
        this.player = player;
        setColour(colour);
    }
	
	@Override
    public String getName() {
        return player.getName();
    }
	
	public int getId() {
        return player.getId();
    }
	
	@Override
    public void setId(int uid) {
       this.player.setId(uid); 
    }

    @Override
    public String getUserName() {
        return this.player.getUserName();
    }
    
    @Override
    public void setUserName(String userName) {
        this.player.setUserName(userName);
    }
    
    @Override
    public String toString(){
        return String.format("%s(%s)",player.getName(),getColour().toString());
    }
}
