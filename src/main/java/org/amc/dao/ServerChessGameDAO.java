package org.amc.dao;

import org.amc.game.chessserver.ServerChessGame;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ServerChessGameDAO extends DAO<ServerChessGame> {

    private static final long serialVersionUID = 1853960541217545162L;

    public ServerChessGameDAO() {
        super(ServerChessGame.class);
    }
    
    public Set<Long> getGameUids() {
        EntityManager entityManager= getEntityManager();
        Query query = entityManager.createQuery("Select x from " + getEntityClass().getSimpleName() + " x");
        Set<Long> gameUidsSet = new HashSet<Long>(query.getResultList());
    
        return gameUidsSet;
    }

}
