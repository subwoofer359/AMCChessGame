package org.amc.game.chess;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

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

    /**
     * Regex used for matching the components of the string input
     */
    private static final Pattern inputPattern = Pattern.compile("([KQBNRPkqbnrp][a-h][1-8]#?):?");

    /**
     * Regex used for matching the whole string input
     */
    private static final Pattern wholePattern = Pattern.compile("(" + inputPattern + ")+");
    
    public static final String MOVE_TOKEN = "#";

    /**
     * Checks the input string to see if can be parsed by this class
     * 
     * @param setupNotation {@link String}
     * @return boolean True if the string can be parsed
     */
    public boolean isInputValid(String setupNotation) {
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
        if(group.endsWith(MOVE_TOKEN) && piece != NO_CHESSPIECE) {
                piece = piece.moved(); 
        }
        
        return new ChessPieceLocation(piece, location);
    }

    private ChessPiece getChessPiece(String chessPieceStr) {

        switch (ChessPieceNotation.valueOf(chessPieceStr)) {
        case K:
            return KingPiece.getPiece(Colour.BLACK);
        case k:
            return KingPiece.getPiece(Colour.WHITE);
        case Q:
            return QueenPiece.getPiece(Colour.BLACK);
        case q:
            return QueenPiece.getPiece(Colour.WHITE);
        case B:
            return BishopPiece.getPiece(Colour.BLACK);
        case b:
            return BishopPiece.getPiece(Colour.WHITE);
        case R:
            return RookPiece.getPiece(Colour.BLACK);
        case r:
            return RookPiece.getPiece(Colour.WHITE);
        case N:
            return KnightPiece.getPiece(Colour.BLACK);
        case n:
            return KnightPiece.getPiece(Colour.WHITE);
        case P:
            return PawnPiece.getPiece(Colour.BLACK);
        case p:
            return PawnPiece.getPiece(Colour.WHITE);
        default:
            return null;
        }
    }
}
