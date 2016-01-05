package org.amc.dao;

import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameMapPlayerSearch {

    public Map<Long, AbstractServerChessGame> getGames(Map<Long, AbstractServerChessGame> gameMap, Player player) {
        Map<Long, AbstractServerChessGame> games = new HashMap<Long, AbstractServerChessGame>();
        for(Map.Entry<Long, AbstractServerChessGame> entry: gameMap.entrySet()) {
            AbstractServerChessGame scg = entry.getValue();
            if(scg != null) {
                if(ComparePlayers.comparePlayers(player, scg.getPlayer()) || 
                                scg.getChessGame() != null && 
                                ComparePlayers.comparePlayers(player, scg.getOpponent())) {
                    games.put(entry.getKey(), scg);
                }
            }
        }
        return Collections.unmodifiableMap(games);
    }
}
