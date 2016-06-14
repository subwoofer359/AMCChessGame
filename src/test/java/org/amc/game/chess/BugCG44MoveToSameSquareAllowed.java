package org.amc.game.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Bug test case for JIRA CG-44
 * 
 * @author Adrian Mclaughlin
 *
 */
public class BugCG44MoveToSameSquareAllowed {

    private ChessGame chessGame;
    private ChessBoard board;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private static Map<String,Move> moves;
    private static ChessBoardFactory boardFactory;
    
    
    @BeforeClass
    public static void setUpFactory(){
        boardFactory=new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        moves=new HashMap<String, Move>();
        moves.put("blackKing",new Move("A8-A8"));
        moves.put("whiteKing",new Move("A1-A1"));
        moves.put("blackQueen",new Move("E8-E8"));
        moves.put("whiteQueen",new Move("E1-E1"));
        moves.put("blackBishop",new Move("C8-C8"));
        moves.put("whiteBishop",new Move("C1-C1"));
        moves.put("blackKnight",new Move("B8-B8"));
        moves.put("whiteKnight",new Move("B1-B1"));
        moves.put("blackPawn",new Move("F8-F8"));
        moves.put("whitePawn",new Move("F1-F1"));
        moves.put("blackRook",new Move("D8-D8"));
        moves.put("whiteRook",new Move("D1-D1"));
    }
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new RealChessGamePlayer(new HumanPlayer("White Player"),Colour.WHITE);
        blackPlayer=new RealChessGamePlayer(new HumanPlayer("Black Player"), Colour.BLACK);
        board=boardFactory.getChessBoard("Ka8:Nb8:Bc8:Rd8:Qe8:Pf8:ka1:nb1:bc1:rd1:qe1:pf1");
        chessGame=new ChessGame(board, whitePlayer, blackPlayer);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBlackQueenNoneMove() {
        QueenPiece queen = new QueenPiece(Colour.BLACK);
        assertFalse(queen.isValidMove(board, moves.get("blackQueen")));
    }
    
    @Test
    public void testWhiteQueenNoneMove() {
        QueenPiece queen = new QueenPiece(Colour.WHITE);
        assertFalse(queen.isValidMove(board, moves.get("whiteQueen")));
    }
    
    @Test
    public void testBlackPawnNoneMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.BLACK);
        assertFalse(pawn.isValidMove(board, moves.get("blackPawn")));
    }
    
    @Test
    public void testWhitePawnNoneMove() {
        PawnPiece pawn = PawnPiece.getPawnPiece(Colour.WHITE);
        assertFalse(pawn.isValidMove(board, moves.get("whitePawn")));
    }
    
    @Test
    public void testBlackBishopNoneMove() {
        BishopPiece bishop = new BishopPiece(Colour.BLACK);
        assertFalse(bishop.isValidMove(board, moves.get("blackBishop")));
    }
    
    @Test
    public void testWhiteBishopNoneMove() {
        BishopPiece bishop = new BishopPiece(Colour.WHITE);
        assertFalse(bishop.isValidMove(board, moves.get("whiteBishop")));
    }
    
    @Test
    public void testBlackKingNoneMove() {
        KingPiece king = KingPiece.getKingPiece(Colour.BLACK);
        assertFalse(king.isValidMove(board, moves.get("blackKing")));
    }
    
    @Test
    public void testWhiteKingNoneMove() {
        KingPiece king = KingPiece.getKingPiece(Colour.WHITE);
        assertFalse(king.isValidMove(board, moves.get("whiteKing")));
    }
    
    @Test
    public void testBlackKnightNoneMove() {
        KnightPiece knight = new KnightPiece(Colour.BLACK);
        assertFalse(knight.isValidMove(board, moves.get("blackKnight")));
    }
    
    @Test
    public void testWhiteKnightNoneMove() {
        KnightPiece knight = new KnightPiece(Colour.WHITE);
        assertFalse(knight.isValidMove(board, moves.get("whiteKnight")));
    }
    
    @Test
    public void testBlackRookNoneMove() {
        RookPiece rook = new RookPiece(Colour.BLACK);
        assertFalse(rook.isValidMove(board, moves.get("blackRook")));
    }
    
    @Test
    public void testWhiteRookNoneMove() {
        RookPiece rook = new RookPiece(Colour.WHITE);
        assertFalse(rook.isValidMove(board, moves.get("whiteRook")));
    }

    @Test(expected=IllegalMoveException.class)
    public void testKingNoneMoveInChessGame() throws IllegalMoveException {
        chessGame.move(whitePlayer, moves.get("whiteKing"));
        chessGame.changePlayer();
        chessGame.move(blackPlayer, moves.get("blackKing"));
    }
    
    @Test(expected=IllegalMoveException.class)
    public void testQueenNoneMoveInChessGame() throws IllegalMoveException {
        chessGame.move(whitePlayer, moves.get("whiteQueen"));
        chessGame.changePlayer();
        chessGame.move(blackPlayer, moves.get("blackQueen"));
    }
    
    @Test(expected=IllegalMoveException.class)
    public void testBishopNoneMoveInChessGame() throws IllegalMoveException {
        chessGame.move(whitePlayer, moves.get("whiteBishop"));
        chessGame.changePlayer();
        chessGame.move(blackPlayer, moves.get("blackBishop"));
    }
    
    @Test(expected=IllegalMoveException.class)
    public void testKnightNoneMoveInChessGame() throws IllegalMoveException {
        chessGame.move(whitePlayer, moves.get("whiteKnight"));
        chessGame.changePlayer();
        chessGame.move(blackPlayer, moves.get("blackKnight"));
    }
    
    @Test(expected=IllegalMoveException.class)
    public void testPawnNoneMoveInChessGame() throws IllegalMoveException {
        chessGame.move(whitePlayer, moves.get("whitePawn"));
        chessGame.changePlayer();
        chessGame.move(blackPlayer, moves.get("blackPawn"));
    }
}
