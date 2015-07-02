/*global $*/
/*global console*/
/*global ChessPieces*/
/*global document*/

/**
 *
 * @file related chessboard functions
 * @author Adrian McLaughlin
 */



var letterCoordinates = [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' ];

var boardWidth = 8;

var whiteSquareColour = '#e6e6e6';

var blackSquareColour = '#000000';


/**
 * Creates a SVG of an empty chessboard
 *
 * @public
 * @returns SVG element
 */
function createBlankChessBoardSVG() {
    "use strict";
    var svgDocument = document.createElementNS("http://www.w3.org/2000/svg", "svg"),
        svgNS = svgDocument.namespaceURI,
        layer = document.createElementNS(svgNS, 'g'),
        x = 0,
        y = 0,
        whiteToggle = true,
        i,
        letter,
        rect;

    svgDocument.setAttribute('height', '500px');
    svgDocument.setAttribute('width', '500px');
    layer.setAttribute('id', 'layer1');

    for (i = boardWidth; i > 0; i -= 1) {
        x = 0;
        for (letter in letterCoordinates) {
            if (letterCoordinates.hasOwnProperty(letter)) {
                rect = document.createElementNS(svgNS, 'rect');
                rect.setAttribute('id', (letterCoordinates[letter] + i));
                rect.setAttribute('x', x);
                rect.setAttribute('y', y);
                rect.setAttribute('width', 62.5);
                rect.setAttribute('height', 62.5);
                if (whiteToggle) {
                    rect.setAttribute('fill', whiteSquareColour);
                } else {
                    rect.setAttribute('fill', blackSquareColour);
                }
                whiteToggle = !whiteToggle;
                rect.setAttribute('stroke', 'none');
                rect.setAttribute('fill-opacity', '1');
                rect.setAttribute('class', 'dropzone');
                layer.appendChild(rect);
                x = x + 62.5;
            }
        }
        y = y + 62.5;
        whiteToggle = !whiteToggle; //alternate black white pattern

    }
    svgDocument.appendChild(layer);
    return svgDocument;
}

function createChessPiecesElements(playerColour, chessboardJSON) {
    "use strict";
    var json = JSON.parse(chessboardJSON),
        location,
        chesspieces,
        piecesOnBoard = "";

    chesspieces = new ChessPieces(playerColour);
    
    for (location in json.squares) {
        if (json.squares.hasOwnProperty(location)) {
            switch (json.squares[location]) {
            case "p":
                piecesOnBoard += chesspieces.pawn("pawn-" + location, location, chesspieces.colour.white);
                break;

            case "P":
                piecesOnBoard += chesspieces.pawn("pawn-" + location, location, chesspieces.colour.black);
                break;

            case 'r':
                piecesOnBoard += chesspieces.rook("rook-" + location, location, chesspieces.colour.white);
                break;

            case 'R':
                piecesOnBoard += chesspieces.rook("rook-" + location, location, chesspieces.colour.black);
                break;

            case 'B':
                piecesOnBoard += chesspieces.bishop("bishop-" + location, location, chesspieces.colour.black);
                break;

            case 'b':
                piecesOnBoard += chesspieces.bishop("bishop-" + location, location, chesspieces.colour.white);
                break;

            case 'N':
                piecesOnBoard += chesspieces.knight("knight-" + location, location, chesspieces.colour.black);
                break;

            case 'n':
                piecesOnBoard += chesspieces.knight("knight-" + location, location, chesspieces.colour.white);
                break;

            case 'Q':
                piecesOnBoard += chesspieces.queen("queen-" + location, location, chesspieces.colour.black);
                break;

            case 'q':
                piecesOnBoard += chesspieces.queen("queen-" + location, location, chesspieces.colour.white);
                break;

            case 'K':
                piecesOnBoard += chesspieces.king("king-" + location, location, chesspieces.colour.black);
                break;

            case 'k':
                piecesOnBoard += chesspieces.king("king-" + location, location, chesspieces.colour.white);
                break;
            }
        }
    }
    return piecesOnBoard;
}

function createChessBoard(playerColour, chessboardJSON) {
    "use strict";
    var chessBoardSVG = $("#chessboard-surround"),
        board;
    chessBoardSVG.html(createBlankChessBoardSVG());
    board = $("#layer1");
    board.append(createChessPiecesElements(playerColour, chessboardJSON));
    chessBoardSVG.html(chessBoardSVG.html());
}