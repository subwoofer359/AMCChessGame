package org.amc.game.chessserver;

/**
 * STOMP message related constants 
 * @author Adrian Mclaughlin
 *
 */
public enum StompConstants {
    MESSAGE_HEADER_TYPE("TYPE");
    
    private String value;
    private StompConstants(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
