package org.amc;

import org.amc.dao.ChessGameDAOTest;
import org.amc.dao.DatabaseGameMapIntegrationTest;
import org.amc.dao.ServerChessGameDAOTest;
import org.amc.dao.UserSearchDAOTest;
import org.amc.game.chessserver.DBGameMapFactoryTest;
import org.amc.game.chessserver.OnlinePlayerListControllerIntegrationTest;
import org.amc.game.chessserver.SignUpControllerIntegrationTest;
import org.amc.game.chessserver.StartPageControllerIntegrationTest;
import org.amc.game.chessserver.StompControllerIntegrationTest;
import org.amc.game.chessserver.UserPlayerIntegrationTest;
import org.amc.game.chessserver.UserSearchIntegrationTest;
import org.amc.game.chessserver.messaging.EmailMessagingIntegrationTest;
import org.amc.game.chessserver.messaging.MoveUpdateEmailTest;
import org.amc.game.chessserver.observers.FinishedChessGameRemovalThreadTest;
import org.amc.game.chessserver.observers.ObserverFactoryChainIntegrationTest;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
@Ignore
@RunWith(Suite.class)
@SuiteClasses({ 
    ChessGameDAOTest.class,
    DatabaseGameMapIntegrationTest.class,
    ServerChessGameDAOTest.class,
    UserSearchDAOTest.class,
    OnlinePlayerListControllerIntegrationTest.class,
    SignUpControllerIntegrationTest.class,
    StartPageControllerIntegrationTest.class,
    StompControllerIntegrationTest.class,
    UserPlayerIntegrationTest.class,
    UserSearchIntegrationTest.class,
    EmailMessagingIntegrationTest.class,
    MoveUpdateEmailTest.class,
    FinishedChessGameRemovalThreadTest.class,
    ObserverFactoryChainIntegrationTest.class,
    DBGameMapFactoryTest.class
})

public class IntegrationTestSuite {

}
