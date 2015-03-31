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

        private int letterIndex;

        private Coordinate(int letterIndex) {
            this.letterIndex = letterIndex;
        }

        /**
         * 
         * @return int value of the Letter Coordinate
         */
        public int getIndex() {
            return this.letterIndex;
        }

    }

    /**
     * 2 Dimensional array representing the squares on a chess board
     */
    private final ChessPiece[][] board;

    /**
     * The width of the Chess Board
     */
    public static final int BOARD_WIDTH = 8;

    List<Move> allGameMoves;

    /**
     * Represents an non move
     * 
     * @see EmptyMove
     */
    private static final Move EMPTY_MOVE = new EmptyMove();

    /**
     * Creates a new chess board array and initialises the moves list.
     */
    public ChessBoard() {
        board = new ChessPiece[Coordinate.values().length][BOARD_WIDTH];
        allGameMoves = new ArrayList<>();
    }
    
    /**
     * Create a deep copy of a ChessBoard
     *  
     * @param board ChessBoard
     */
    public ChessBoard(ChessBoard board) {
        this();
        for(Coordinate coord:Coordinate.values()){
            copyFile(board, coord);
        }
        copyMoveList(board);
    }
    
    private void copyFile(ChessBoard board, Coordinate file){
        for(int i=1;i<=BOARD_WIDTH;i++){
            ChessPiece piece=board.getPieceFromBoardAt(file.letterIndex, i);
            storeCopyOfChessPiece(piece, file, i);
        }
        
    }
    
    private void storeCopyOfChessPiece(ChessPiece piece,Coordinate file,int rank){
        if(piece != null){
            this.putPieceOnBoardAt(piece.copy(), new Location(file,rank));
        }
    }
    
    private void copyMoveList(ChessBoard board){
        this.allGameMoves=new ArrayList<>(board.allGameMoves);
    }

    /**
     * Sets up the board in it's initial state
     */
    public void initialise() {
        SetupChessBoard.setUpChessBoardToDefault(this);
    }

    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * 
     * @param move
     *            Move
     */
    public void move(Move move) {
        quietMove(move);
        this.notifyObservers(new ChessBoard(this));
    }

    /**
     * Move a ChessPiece from one square to another as long as the move is valid
     * Doesn't update Observers
     * 
     * @param move
     *            Move
     */

    void quietMove(Move move) {
        ChessPiece piece = getPieceFromBoardAt(move.getStart());
        removePieceOnBoardAt(move.getStart());
        putPieceOnBoardAt(piece, move.getEnd());
        piece.moved();
        this.allGameMoves.add(move);
    }

    /**
     * Maps the Chess file coordinate number to the index of
     * <code>ChessBoard[][] board</code>
     * 
     * @param squareRank
     * @return
     */
    private int mapNumberCoordinate(int squareRank) {
        return BOARD_WIDTH - squareRank;
    }

    /**
     * Removes the ChessPiece from the Board The square it occupied is set back
     * to null
     * 
     * @param location
     */
    void removePieceOnBoardAt(Location location) {
        this.board[location.getLetter().getIndex()][mapNumberCoordinate(location.getNumber())] = null;
    }

    /**
     * Places ChessPiece on the board at the give Location If the square is
     * already occupied the ChessPiece already on the square is replaced.
     * 
     * @param piece
     * @param location
     */
    public void putPieceOnBoardAt(ChessPiece piece, Location location) {
        this.board[location.getLetter().getIndex()][mapNumberCoordinate(location.getNumber())] = piece;
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
        return board[letterCoordinate][mapNumberCoordinate(numberCoordinate)];
    }

    /**
     * Returns the Chess Piece on the ChessBoard at the give Location
     * 
     * @param location
     *            Location
     * @return ChessPiece
     */
    public ChessPiece getPieceFromBoardAt(Location location) {
        return getPieceFromBoardAt(location.getLetter().getIndex(), location.getNumber());
    }

    /**
     * Return the last Move make or null if no move has yet to be made
     * 
     * @return Move
     */
    Move getTheLastMove() {
        if (allGameMoves.isEmpty()) {
            return EMPTY_MOVE;
        } else {
            return allGameMoves.get(allGameMoves.size() - 1);
        }
    }

    /**
     * Checks a square to see if it's empty
     * 
     * @param location
     * @return Boolean true if it's empty
     */
    boolean isEndSquareEmpty(Location location) {
        return getPieceFromBoardAt(location.getLetter().getIndex(), location.getNumber()) == null;
    }

    /**
     * Returns a List of the Player's chess pieces on the chessboard
     * 
     * @param player
     *            Player
     * @return List
     */
    List<ChessPieceLocation> getListOfPlayersPiecesOnTheBoard(Player player) {
        List<ChessPieceLocation> listOfPieces = new ArrayList<>();
        for (Coordinate letterIndex : Coordinate.values()) {
            searchFileForChessPieces(player, letterIndex, listOfPieces);
        }
        return listOfPieces;
    }

    private void searchFileForChessPieces(Player player, Coordinate file,
                    List<ChessPieceLocation> listOfPieces) {
        for (int i = 1; i <= BOARD_WIDTH; i++) {
            ChessPiece piece = getPieceFromBoardAt(file.getIndex(), i);
            if (isPlayersChessPiece(player, piece)) {
                listOfPieces.add(new ChessPieceLocation(piece, new Location(file, i)));
            }
        }
    }

    private boolean isPlayersChessPiece(Player player, ChessPiece piece) {
        return piece != null && piece.getColour().equals(player.getColour());
    }

    /**
     * Returns the Location of the square where the Player's King is located
     * 
     * @param player
     *            Player
     * @return Location
     * @throws RuntimeException
     *             if no King is found
     */
    Location getPlayersKingLocation(Player player) {
        for (Coordinate letterIndex : Coordinate.values()) {
            for (int i = 1; i <= BOARD_WIDTH; i++) {
                ChessPiece piece = getPieceFromBoardAt(letterIndex.getIndex(), i);
                if (piece != null && piece instanceof KingPiece
                                && piece.getColour().equals(player.getColour())) {
                    return new Location(letterIndex, i);
                }
            }
        }
        throw new RuntimeException("Player's king not found this should not happened");
    }

    public static class ChessPieceLocation {
        private ChessPiece piece;
        private Location location;

        public ChessPieceLocation(ChessPiece piece, Location location) {
            this.piece = piece;
            this.location = location;
        }

        public final ChessPiece getPiece() {
            return piece;
        }

        public final Location getLocation() {
            return location;
        }
        
        @Override
        public String toString(){
            StringBuilder sb=new StringBuilder(piece.toString());
            sb.append(location.toString());
            return sb.toString();
        }
    }
}
