package org.amc.game.chess;

import org.amc.util.DefaultSubject;

import static org.amc.game.chess.StartingSquare.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a Chess Board Responsibility is to know the position of all the
 * pieces
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoard extends DefaultSubject {
    /**
     * Represents the Letter Coordinates of squares on a chess board
     * 
     * @author Adrian Mclaughlin
     *
     */
    public enum Coordinate implements Comparable<Coordinate> {
        A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

        private int name;

        private Coordinate(int name) {
            this.name = name;
        }

        /**
         * 
         * @return int value of the Letter Coordinate
         */
        public int getName() {
            return this.name;
        }

    }

    private final ChessPiece[][] board;
    public final int BOARD_WIDTH=8;

    List<Move> allGameMoves;
    private static final Move EMPTY_MOVE=new EmptyMove();
    
    public ChessBoard() {
        super();
        board = new ChessPiece[Coordinate.values().length][BOARD_WIDTH];
        allGameMoves=new ArrayList<>();
    }

    /**
     * Sets up the board in it's initial state
     */
    public void initialise() {
        putPieceOnBoardAt(new BishopPiece(Colour.WHITE), WHITE_BISHOP_LEFT.getLocation());
        putPieceOnBoardAt(new BishopPiece(Colour.WHITE), WHITE_BISHOP_RIGHT.getLocation());
        putPieceOnBoardAt(new KingPiece(Colour.WHITE), WHITE_KING.getLocation());
        putPieceOnBoardAt(new QueenPiece(Colour.WHITE), WHITE_QUEEN.getLocation());
        putPieceOnBoardAt(new KnightPiece(Colour.WHITE), WHITE_KNIGHT_LEFT.getLocation());
        putPieceOnBoardAt(new KnightPiece(Colour.WHITE), WHITE_KNIGHT_RIGHT.getLocation());
        putPieceOnBoardAt(new RookPiece(Colour.WHITE), WHITE_ROOK_LEFT.getLocation());
        putPieceOnBoardAt(new RookPiece(Colour.WHITE), WHITE_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            putPieceOnBoardAt(new PawnPiece(Colour.WHITE), new Location(coord, 2));
        }

        putPieceOnBoardAt(new BishopPiece(Colour.BLACK), BLACK_BISHOP_LEFT.getLocation());
        putPieceOnBoardAt(new BishopPiece(Colour.BLACK), BLACK_BISHOP_RIGHT.getLocation());
        putPieceOnBoardAt(new KingPiece(Colour.BLACK), BLACK_KING.getLocation());
        putPieceOnBoardAt(new QueenPiece(Colour.BLACK), BLACK_QUEEN.getLocation());
        putPieceOnBoardAt(new KnightPiece(Colour.BLACK), BLACK_KNIGHT_LEFT.getLocation());
        putPieceOnBoardAt(new KnightPiece(Colour.BLACK), BLACK_KNIGHT_RIGHT.getLocation());
        putPieceOnBoardAt(new RookPiece(Colour.BLACK), BLACK_ROOK_LEFT.getLocation());
        putPieceOnBoardAt(new RookPiece(Colour.BLACK), BLACK_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            putPieceOnBoardAt(new PawnPiece(Colour.BLACK), new Location(coord, 7));
        }
    }

    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * @param move Move
     */
    public void move(Move move) {
        ChessPiece piece = getPieceFromBoardAt(move.getStart());
        removePieceOnBoardAt(move.getStart());
        putPieceOnBoardAt(piece, move.getEnd());
        piece.moved();
        this.allGameMoves.add(move);
        this.notifyObservers(null);
    }
    
    

    /**
     * Removes the ChessPiece from the Board The square it occupied is set back
     * to null
     * @param location
     */
    void removePieceOnBoardAt(Location location) {
        this.board[location.getLetter().getName()][location.getNumber() - 1] = null;
    }

    /**
     * Places ChessPiece on the board at the give Location If the square is
     * already occupied the ChessPiece already on the square is replaced.
     * 
     * @param piece
     * @param location
     */
    public void putPieceOnBoardAt(ChessPiece piece, Location location) {
        this.board[location.getLetter().getName()][location.getNumber() - 1] = piece;
    }

    /**
     * Returns the Chess Piece on the ChessBoard at the give Coordinates
     * 
     * @param letterCoordinate
     *            an Integer in the range 0 to 7
     * @param numberCoordinate
     *            an Integer in the range 1 to 8
     * @return ChessPiece from square on the board or null if the square is
     *         empty
     */
    ChessPiece getPieceFromBoardAt(int letterCoordinate, int numberCoordinate) {
        return board[letterCoordinate][numberCoordinate - 1];
    }

    /**
     * Returns the Chess Piece on the ChessBoard at the give Location
     * 
     * @param location
     *            Location
     * @return ChessPiece
     */
    public ChessPiece getPieceFromBoardAt(Location location) {
        return getPieceFromBoardAt(location.getLetter().getName(), location.getNumber());
    }
    
    /**
     * Return the last Move make or null if no move has yet to be made
     * @return Move
     */
    Move getTheLastMove(){
        if(allGameMoves.isEmpty()){
            return EMPTY_MOVE;
        }
        else
        {
            return allGameMoves.get(allGameMoves.size()-1);
        }
    }
    
    boolean isEndSquareEmpty(Location location){
        ChessPiece piece = getPieceFromBoardAt(location.getLetter().getName(),
                        location.getNumber());
        return piece==null;
    }
    
    List<ChessPieceLocation> getListOfPlayersPiecesOnTheBoard(Player player){
        List<ChessPieceLocation> listOfPieces=new ArrayList<>();
        for(Coordinate letterIndex:Coordinate.values()){
            for(int i=1;i<=BOARD_WIDTH;i++){
                ChessPiece piece=getPieceFromBoardAt(letterIndex.getName(), i);
                if(piece!=null && piece.getColour().equals(player.getColour())){
                    listOfPieces.add(new ChessPieceLocation(piece, new Location(letterIndex,i)));
                }
            }
        }
        return listOfPieces;
    }
    
    Location getPlayersKingLocation(Player player){
        for(Coordinate letterIndex:Coordinate.values()){
            for(int i=1;i<=BOARD_WIDTH;i++){
                ChessPiece piece=getPieceFromBoardAt(letterIndex.getName(), i);
                if(piece!=null && piece instanceof KingPiece && piece.getColour().equals(player.getColour())){
                    return new Location(letterIndex,i);
                }
            }
        }
        throw new RuntimeException("Player's king not found this should not happened");
    }
    
    /**
     * Removes a move saved from previous players turns
     * Can throw an exception if the Move doesn't exist
     * 
     * @param move Move to be removed
     */
    void removeMoveFromMoveList(Move move){
        this.allGameMoves.remove(move);
    }
    
    public class ChessPieceLocation{
        private ChessPiece piece;
        private Location location;
        public ChessPieceLocation(ChessPiece piece,Location location){
            this.piece=piece;
            this.location=location;
        }
        public final ChessPiece getPiece() {
            return piece;
        }
        public final Location getLocation() {
            return location;
        }
    }
}
