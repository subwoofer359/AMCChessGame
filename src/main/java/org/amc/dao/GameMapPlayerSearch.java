package org.amc.dao;

import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameMapPlayerSearch {

    public Map<Long, ServerChessGame> getGames(Map<Long, ServerChessGame> gameMap, Player player) {
        Map<Long, ServerChessGame> games = new HashMap<Long, ServerChessGame>();
        for(Map.Entry<Long,ServerChessGame> entry: gameMap.entrySet()) {
            ServerChessGame scg = entry.getValue();
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
