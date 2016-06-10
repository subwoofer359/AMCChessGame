package org.amc.game.chess;

/**
 * Compares two Players to see if they are the same even if they are of different type 
 * 
 * @author Adrian Mclaughlin
 *
 */

public final class ComparePlayers {

        /**
         * Compares Two players
         *  
         * @param playerOne Player one
         * @param playerTwo Player two
         * @return true if fields uid and name are the same.
         */
        public static boolean comparePlayers(Player playerOne, Player playerTwo) {
            return isSameUid(playerOne, playerTwo) && isSameName(playerOne, playerTwo);
        }
        
        private static boolean isSameUid(Player playerOne, Player playerTwo) {
        	return isPlayersNotNull(playerOne, playerTwo) && playerOne.getId() == playerTwo.getId();
        }
        
        private static boolean isSameName(Player playerOne, Player playerTwo) {
        	return isPlayersNotNull(playerOne, playerTwo) && playerOne.getName().equals(playerTwo.getName());
        }

        private static boolean isPlayersNotNull(Player playerOne, Player playerTwo) {
            return !(playerOne == null || playerTwo == null);
        }

        private ComparePlayers() {
            throw new RuntimeException("Can't be instantiated");
        }

}
