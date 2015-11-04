package org.amc.dao;

import org.amc.DAOException;
import org.amc.game.chess.ChessGame;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

public class ChessGameDAO extends DAO<ChessGame> {

    public ChessGameDAO() {
        super(ChessGame.class);
    }
    
    
    /**
     * @see DAO#getEntity(int)
     * manually marks the field board of ChessBoard dirty so it's updated
     * when the EntityManager is closed. An implementation solution
     * @throws DAOException when Database query fails or not using an OpenJPAEntityManager
     * TODO Replace with an agnostic solution
     */
    @Override
    public ChessGame getEntity(int id) throws DAOException {
        ChessGame game = super.getEntity(id);
        if(getEntityManager() instanceof OpenJPAEntityManager) {
            if(game != null) {
                ((OpenJPAEntityManager)getEntityManager()).dirty(game, "board");
            }
        } else {
            throw new DAOException("Not an OpenJPA entity manager: changes to chessboard won't be saved");
        }
        
        return game;
    }

    
}
