package org.amc.game.chess;

import org.amc.util.DefaultSubject;

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

    List<Move> allGameMoves;
    private static final Move EMPTY_MOVE=new EmptyMove();
    
    public ChessBoard() {
        super();
        board = new ChessPiece[8][8];
        allGameMoves=new ArrayList<>();
    }

    /**
     * Sets up the board in it's initial state
     */
    public void initialise() {
        putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.C, 1));
        putPieceOnBoardAt(new BishopPiece(Colour.WHITE), new Location(Coordinate.F, 1));
        putPieceOnBoardAt(new KingPiece(Colour.WHITE), new Location(Coordinate.E, 1));
        putPieceOnBoardAt(new QueenPiece(Colour.WHITE), new Location(Coordinate.D, 1));
        putPieceOnBoardAt(new KnightPiece(Colour.WHITE), new Location(Coordinate.B, 1));
        putPieceOnBoardAt(new KnightPiece(Colour.WHITE), new Location(Coordinate.G, 1));
        putPieceOnBoardAt(new RookPiece(Colour.WHITE), new Location(Coordinate.A, 1));
        putPieceOnBoardAt(new RookPiece(Colour.WHITE), new Location(Coordinate.H, 1));
        for (Coordinate coord : Coordinate.values()) {
            putPieceOnBoardAt(new PawnPiece(Colour.WHITE), new Location(coord, 2));
        }

        putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.C, 8));
        putPieceOnBoardAt(new BishopPiece(Colour.BLACK), new Location(Coordinate.F, 8));
        putPieceOnBoardAt(new KingPiece(Colour.BLACK), new Location(Coordinate.E, 8));
        putPieceOnBoardAt(new QueenPiece(Colour.BLACK), new Location(Coordinate.D, 8));
        putPieceOnBoardAt(new KnightPiece(Colour.BLACK), new Location(Coordinate.B, 8));
        putPieceOnBoardAt(new KnightPiece(Colour.BLACK), new Location(Coordinate.G, 8));
        putPieceOnBoardAt(new RookPiece(Colour.BLACK), new Location(Coordinate.A, 8));
        putPieceOnBoardAt(new RookPiece(Colour.BLACK), new Location(Coordinate.H, 8));
        for (Coordinate coord : Coordinate.values()) {
            putPieceOnBoardAt(new PawnPiece(Colour.BLACK), new Location(coord, 7));
        }
    }

    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * 
     * @param player
     *            Player making the move
     * @param move
     *            Move
     * @throws InvalidMoveException
     *             if not a valid movement
     */
    public void move(Player player, Move move) throws InvalidMoveException {
        ChessPiece piece = getPieceFromBoardAt(move.getStart());
        removePieceOnBoardAt(piece, move.getStart());
        putPieceOnBoardAt(piece, move.getEnd());
        piece.moved();
        this.allGameMoves.add(move);
        this.notifyObservers(null);
    }

    /**
     * Removes the ChessPiece from the Board The square it occupied is set back
     * to null
     * 
     * @param piece
     * @param location
     */
    void removePieceOnBoardAt(ChessPiece piece, Location location) {
        this.board[location.getLetter().getName()][location.getNumber() - 1] = null;
    }

    /**
     * Places ChessPiece on the board at the give Location If the square is
     * already occupied the ChessPiece already on the square is replaced.
     * 
     * @param piece
     * @param location
     */
    void putPieceOnBoardAt(ChessPiece piece, Location location) {
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
    ChessPiece getPieceFromBoardAt(Location location) {
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
}
