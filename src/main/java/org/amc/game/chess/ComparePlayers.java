package org.amc.game.chess;

/**
 * Compares two Players to see if they are the same even if they are of different type 
 * 
 * @author Adrian Mclaughlin
 *
 */

public class ComparePlayers {

   
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
            return playerOne.getUid() == playerTwo.getUid();
        }
        
        private static boolean isSameName(Player playerOne, Player playerTwo) {
            return playerOne.getName().equals(playerTwo.getName());
        }

}
