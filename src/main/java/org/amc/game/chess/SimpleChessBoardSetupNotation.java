package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chess.ChessBoard.Coordinate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
public class SimpleChessBoardSetupNotation implements ChessBoardSetupNotation {

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

    /* (non-Javadoc)
     * @see org.amc.game.chess.ChessBoardSetupNotation#getChessPieceLocations(java.lang.String)
     */
    @Override
    public List<ChessPieceLocation> getChessPieceLocations(String setupNotation)
                    throws ParseException {
        if (isInputValid(setupNotation)) {
            List<ChessPieceLocation> pieceLocations = new ArrayList<>();

            Matcher matcher = inputPattern.matcher(setupNotation);
            while (matcher.find()) {
                ChessPieceLocation cpl = parseGroup(matcher.group(1));
                pieceLocations.add(cpl);
            }
            return pieceLocations;
        } else {
            throw new ParseException("Not valid setup notation", 0);
        }
    }

    private ChessPieceLocation parseGroup(String group) throws ParseException {
        ChessPiece piece = getChessPiece(group.substring(0, 1));
        Location location = new Location(group.substring(1, 3));
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
}
