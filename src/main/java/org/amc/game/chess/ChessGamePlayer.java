package org.amc.game.chess;

/**
 * Decorator for Player to be used by the Chess Game class
 * @author Adrian Mclaughlin
 *
 */
public class ChessGamePlayer implements Player {

    private Colour colour;
    private final Player player;
    
    public ChessGamePlayer(Player player, Colour colour) {
        this.player = player;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    public final Colour getColour() {
        return colour;
    }

    public final void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)",player.getName(),getColour().toString());
    }

    @Override
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
    
    
}
