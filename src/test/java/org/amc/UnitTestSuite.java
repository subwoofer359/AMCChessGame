package org.amc;

import org.amc.dao.ChessBoardExternalizerTest;
import org.amc.dao.DAOUnitTest;
import org.amc.dao.DatabaseGameMapTest;
import org.amc.game.chess.BishopPieceTest;
import org.amc.game.chess.BugCG23TestCase;
import org.amc.game.chess.BugCG24PawnCantMoveTwoSquares;
import org.amc.game.chess.BugCG42ChessGameDidntEndWhenCheckmateWasAchieved;
import org.amc.game.chess.BugCG44MoveToSameSquareAllowed;
import org.amc.game.chess.CastlingTest;
import org.amc.game.chess.ChessBoardTest;
import org.amc.game.chess.ChessGameCastlingKingInCheck;
import org.amc.game.chess.ChessGameGetOpposingPlayerTest;
import org.amc.game.chess.ChessGameMoveKingInCheck;
import org.amc.game.chess.ChessGameMoveKingOutOfCheck;
import org.amc.game.chess.ChessGameMoveTest;
import org.amc.game.chess.ChessPieceCanCopy;
import org.amc.game.chess.EmptyMoveTest;
import org.amc.game.chess.EnPassantTest;
import org.amc.game.chess.GameStateTest;
import org.amc.game.chess.IsKingCheckmated;
import org.amc.game.chess.IsKingInCheckTest;
import org.amc.game.chess.IsStalemateTest;
import org.amc.game.chess.KingPieceTest;
import org.amc.game.chess.KnightPieceTest;
import org.amc.game.chess.LocationTest;
import org.amc.game.chess.MoveTest;
import org.amc.game.chess.PawnIsNotPromotedTest;
import org.amc.game.chess.PawnIsPromotedTest;
import org.amc.game.chess.PawnPieceTest;
import org.amc.game.chess.QueenPieceTest;
import org.amc.game.chess.RookPieceTest;
import org.amc.game.chess.SimpleChessBoardSetupNotationTest;
import org.amc.game.chess.StandardChessGameTest;
import org.amc.game.chess.UnmodifibleChessBoardTest;
import org.amc.game.chess.controller.ConsoleControllerTest;
import org.amc.game.chess.view.ChessBoardViewTest;
import org.amc.game.chessserver.BugCG42ServerChessGameDidntEndWhenCheckmateWasAchieved;
import org.amc.game.chessserver.BugCG52ServerChessGameDidntEndInCheckmate;
import org.amc.game.chessserver.GameTableControllerTest;
import org.amc.game.chessserver.NewGameRequestControllerTest;
import org.amc.game.chessserver.LogOutControllerTest;
import org.amc.game.chessserver.MemoryGameMapFactoryBeanTest;
import org.amc.game.chessserver.OneViewServerChessGameTest;
import org.amc.game.chessserver.OnlinePlayerListControllerTest;
import org.amc.game.chessserver.OnlinePlayerListMessagerTest;
import org.amc.game.chessserver.ServerChessGameFactoryTest;
import org.amc.game.chessserver.ServerChessGameSerilaiserTest;
import org.amc.game.chessserver.ServerChessGameTest;
import org.amc.game.chessserver.ServerChessGameTest2;
import org.amc.game.chessserver.SignUpControllerUnitTest;
import org.amc.game.chessserver.StartPageControllerChessAppPageTest;
import org.amc.game.chessserver.StartPageControllerCreateGameTest;
import org.amc.game.chessserver.StartPageControllerJoinGameTest;
import org.amc.game.chessserver.StompControllerOneViewUnitTest;
import org.amc.game.chessserver.StompControllerSaveUnitTest;
import org.amc.game.chessserver.StompControllerUnitTest;
import org.amc.game.chessserver.TwoViewServerChessGameTest;
import org.amc.game.chessserver.UserNameAvailableTest;
import org.amc.game.chessserver.UserSearchTest;
import org.amc.game.chessserver.messaging.EmailMessageServiceTest;
import org.amc.game.chessserver.messaging.EmailTemplateFactoryTest;
import org.amc.game.chessserver.messaging.OfflineChessGameMessagerTest;
import org.amc.game.chessserver.messaging.PlayerJoinedChessGameEmailTest;
import org.amc.game.chessserver.observers.FinishedChessGameRemovalUnitTest;
import org.amc.game.chessserver.observers.GameStateListenerTest;
import org.amc.game.chessserver.observers.JsonChessBoardViewTest;
import org.amc.game.chessserver.observers.JsonChessGameViewFactoryTest;
import org.amc.game.chessserver.observers.ObserverFactoryChainImplTest;
import org.amc.game.chessserver.spring.CsrfMatcherTest;
import org.amc.game.chessserver.spring.InValidEmailAddrTest;
import org.amc.game.chessserver.spring.InValidFullNameTest;
import org.amc.game.chessserver.spring.InValidPasswordTest;
import org.amc.game.chessserver.spring.InValidUserNameTest;
import org.amc.game.chessserver.spring.ValdatorIsUserNameFreeTest;
import org.amc.game.chessserver.spring.ValidEmailAddrTest;
import org.amc.game.chessserver.spring.ValidFullNameTest;
import org.amc.game.chessserver.spring.ValidPasswordTest;
import org.amc.game.chessserver.spring.ValidUserNameTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
    DAOUnitTest.class,
    ChessBoardExternalizerTest.class,
    DatabaseGameMapTest.class,
    BishopPieceTest.class,
    BugCG23TestCase.class,
    BugCG24PawnCantMoveTwoSquares.class,
    BugCG42ChessGameDidntEndWhenCheckmateWasAchieved.class,
    BugCG42ServerChessGameDidntEndWhenCheckmateWasAchieved.class,
    BugCG44MoveToSameSquareAllowed.class,
    CastlingTest.class,
    ChessBoardTest.class,
    ChessGameCastlingKingInCheck.class,
    ChessGameGetOpposingPlayerTest.class,
    ChessGameMoveKingInCheck.class,
    ChessGameMoveKingOutOfCheck.class,
    ChessGameMoveTest.class,
    ChessPieceCanCopy.class,
    EmptyMoveTest.class,
    EnPassantTest.class,
    GameStateTest.class,
    IsKingCheckmated.class,
    IsKingInCheckTest.class,
    IsStalemateTest.class,
    KingPieceTest.class,
    KnightPieceTest.class,
    LocationTest.class,
    LogOutControllerTest.class,
    MemoryGameMapFactoryBeanTest.class,
    MoveTest.class,
    NewGameRequestControllerTest.class,
    PawnIsNotPromotedTest.class,
    PawnIsPromotedTest.class,
    PawnPieceTest.class,
    QueenPieceTest.class,
    RookPieceTest.class,
    SimpleChessBoardSetupNotationTest.class,
    ConsoleControllerTest.class,
    ChessBoardViewTest.class,
    BugCG42ServerChessGameDidntEndWhenCheckmateWasAchieved.class,
    BugCG52ServerChessGameDidntEndInCheckmate.class,
    OnlinePlayerListControllerTest.class,
    OnlinePlayerListMessagerTest.class,
    ServerChessGameFactoryTest.class,
    ServerChessGameTest.class,
    ServerChessGameTest2.class,
    OneViewServerChessGameTest.class,
    TwoViewServerChessGameTest.class,
    SignUpControllerUnitTest.class,
    StartPageControllerChessAppPageTest.class,
    StartPageControllerJoinGameTest.class,
    StompControllerUnitTest.class,
    StartPageControllerCreateGameTest.class,
    StompControllerOneViewUnitTest.class,
    StompControllerSaveUnitTest.class,
    UserSearchTest.class,
    EmailMessageServiceTest.class,
    EmailTemplateFactoryTest.class,
    OfflineChessGameMessagerTest.class,
    PlayerJoinedChessGameEmailTest.class,
    FinishedChessGameRemovalUnitTest.class,
    GameStateListenerTest.class,
    GameTableControllerTest.class,
    JsonChessBoardViewTest.class,
    JsonChessGameViewFactoryTest.class,
    ObserverFactoryChainImplTest.class,
    CsrfMatcherTest.class,
    InValidEmailAddrTest.class,
    InValidFullNameTest.class,
    InValidPasswordTest.class,
    InValidUserNameTest.class,
    ValdatorIsUserNameFreeTest.class,
    ValidEmailAddrTest.class,
    ValidFullNameTest.class,
    ValidPasswordTest.class,
    ValidUserNameTest.class,
    StandardChessGameTest.class,
    UnmodifibleChessBoardTest.class,
    ServerChessGameSerilaiserTest.class,
    UserNameAvailableTest.class
})

public class UnitTestSuite {

}
