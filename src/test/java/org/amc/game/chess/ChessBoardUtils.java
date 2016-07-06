/**
 * Contains simple chessboard manipulating methods
 * @author Adrian McLaughlin
 */
 
 public class ChessBoardUtils {
    private ChessBoard board;
    
    public ChessBoardUtils(ChessBoard board) {
        this.board = board;
    }
    
    public Move createMove(String start, String end) {
        return new Move(start + Move.MOVE_SEPARATOR + end);
    }
    
    public void addChessPieceToBoard(ChessPiece piece, String location) {
        board.putPieceOnBoardAt(piece, new Location(location));
    }
    
    public void addPawnPieceToBoard(Colour colour, String location) {
        addChessPieceToBoard(PawnPiece.getPawnPiece(colour), location);
    }
 }
