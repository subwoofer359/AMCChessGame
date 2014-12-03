package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Given an input string a chess board in a particular configuration is created
 * Format of the input string is (ChessPieceNotation)(file)(rank) e.g. Kh3 -
 * Black king at square h3 series of input is can be separated by ':'
 * 
 * @author Adrian Mclaughlin
 *
 */
public class SimpleChessBoardSetupNotation {

    enum ChessPieceNotation {
        /** black king */
        K,
        /** black queen */
        Q,
        /** black bishop */
        B,
        /** black knight */
        N,
        /** black rook */
        R,
        /** black pawn */
        P,
        /** white king */
        k,
        /** white queen */
        q,
        /** white bishop */
        b,
        /** white rook */
        r,
        /** white knight */
        n,
        /** white pawn */
        p;
    }

    /**
     * Regex used for matching the components of the string input
     */
    private static final Pattern inputPattern = Pattern.compile("([KQBNRPkqbnrp][a-h][1-8]):?");

    /**
     * Regex used for matching the whole string input
     */
    private static final Pattern wholePattern = Pattern.compile("(" + inputPattern + ")+");

    /**
     * Checks the input string to see if can be parsed by this class
     * 
     * @param setupNotation
     * @return boolean True if the string can be parsed
     */
    boolean isInputValid(String setupNotation) {
        return wholePattern.matcher(setupNotation).matches();

    }

    /**
     * Creates a new Chessboard which is populated with the ChessPieces at
     * Locations specified in the input String
     * 
     * @param setupNotation
     *            input string which is parsed
     * @return ChessBoard
     * @throws ParseException
     *             if the input string can't be parsed
     */
    public ChessBoard getChessBoard(String setupNotation) throws ParseException {
        if (isInputValid(setupNotation)) {
            ChessBoard board = new ChessBoard();
            Matcher matcher = inputPattern.matcher(setupNotation);
            while (matcher.find()) {
                ChessPieceLocation cpl = parseGroup(matcher.group(1));
                placeChessPieceOnTheBoard(board, cpl);
            }
            return board;
        } else {
            throw new ParseException("Not valid setup notation", 0);
        }
    }

    private ChessPieceLocation parseGroup(String group) throws ParseException {
        ChessPiece piece = getChessPiece(group.substring(0, 1));
        Location location = getChessPieceLocation(group.substring(1, 3));
        return new ChessPieceLocation(piece, location);
    }

    private ChessPiece getChessPiece(String chessPieceStr) {

        switch (ChessPieceNotation.valueOf(chessPieceStr)) {
        case K:
            return new KingPiece(Colour.BLACK);
        case k:
            return new KingPiece(Colour.WHITE);
        case Q:
            return new QueenPiece(Colour.BLACK);
        case q:
            return new QueenPiece(Colour.WHITE);
        case B:
            return new BishopPiece(Colour.BLACK);
        case b:
            return new BishopPiece(Colour.WHITE);
        case R:
            return new RookPiece(Colour.BLACK);
        case r:
            return new RookPiece(Colour.WHITE);
        case N:
            return new KnightPiece(Colour.BLACK);
        case n:
            return new KnightPiece(Colour.WHITE);
        case P:
            return new PawnPiece(Colour.BLACK);
        case p:
            return new PawnPiece(Colour.WHITE);
        default:
            return null;
        }
    }

    private Location getChessPieceLocation(String locationStr) throws ParseException {
        String coordinateStr = locationStr.substring(0, 1).toUpperCase();
        String numberStr = locationStr.substring(1, 2);

        return new Location(Coordinate.valueOf(coordinateStr), Integer.parseInt(numberStr));
    }

    private void placeChessPieceOnTheBoard(ChessBoard board, ChessPieceLocation pieceLocation) {
        board.putPieceOnBoardAt(pieceLocation.getPiece(), pieceLocation.getLocation());
    }
}
