/*global $*/
/*global chesspiecesModule*/
/*global document*/

/**
 *
 * @file related chessboard functions
 * @author Adrian McLaughlin
 */

var chessboardModule = (function () {
    "use strict";
    var letterCoordinates = [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' ],
        boardWidth = 8,
        whiteSquareColour = '#e6e6e6',
        blackSquareColour = '#000000',
        Colour = ChessPiecesModule.Colour,
        constPieces = {
            blackQueen : new ChessPiecesModule.QueenPiece(Colour.black),
            blackKing : new ChessPiecesModule.KingPiece(Colour.black),
            blackKnight : new ChessPiecesModule.KnightPiece(Colour.black),
            blackBishop : new ChessPiecesModule.BishopPiece(Colour.black),
            blackRook : new ChessPiecesModule.RookPiece(Colour.black),
            blackPawn : new ChessPiecesModule.PawnPiece(Colour.black),

            whiteQueen : new ChessPiecesModule.QueenPiece(Colour.white),
            whiteKing : new ChessPiecesModule.KingPiece(Colour.white),
            whiteKnight : new ChessPiecesModule.KnightPiece(Colour.white),
            whiteBishop : new ChessPiecesModule.BishopPiece(Colour.white),
            whiteRook : new ChessPiecesModule.RookPiece(Colour.white),
            whitePawn : new ChessPiecesModule.PawnPiece(Colour.white)
        };
        


    /**
     * Creates a SVG of an empty chessboard
     *
     * @public
     * @returns SVG element
     */
    function createBlankChessBoardSVG() {
        var svgDocument = document.createElementNS("http://www.w3.org/2000/svg", "svg"),
            svgNS = svgDocument.namespaceURI,
            layer = document.createElementNS(svgNS, 'g'),
            x = 0,
            y = 0,
            whiteToggle = true,
            i,
            letter,
            rect;

        svgDocument.setAttribute('viewBox', '0 0 500 500');
        //svgDocument.setAttribute('height', '500px');
        //svgDocument.setAttribute('width', '500px');
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
        var json = JSON.parse(chessboardJSON),
            location,
            piecesOnBoard = "";

        for (location in json.squares) {
            if (json.squares.hasOwnProperty(location)) {
                switch (json.squares[location]) {
                case "p":
                    piecesOnBoard += constPieces.whitePawn.toString("pawn-" + location, location, playerColour);
                    break;

                case "P":
                    piecesOnBoard += constPieces.blackPawn.toString("pawn-" + location, location, playerColour);
                    break;

                case 'r':
                    piecesOnBoard += constPieces.whiteRook.toString("rook-" + location, location, playerColour);
                    break;

                case 'R':
                    piecesOnBoard += constPieces.blackRook.toString("rook-" + location, location, playerColour);
                    break;

                case 'B':
                    piecesOnBoard += constPieces.blackBishop.toString("bishop-" + location, location, playerColour);
                    break;

                case 'b':
                    piecesOnBoard += constPieces.whiteBishop.toString("bishop-" + location, location, playerColour);
                   break;

                case 'N':
                    piecesOnBoard += constPieces.blackKnight.toString("knight-" + location, location, playerColour);
                    break;

                case 'n':
                    piecesOnBoard += constPieces.whiteKnight.toString("knight-" + location, location, playerColour);
                    break;

                case 'Q':
                    piecesOnBoard += constPieces.blackQueen.toString("queen-" + location, location, playerColour);
                    break;

                case 'q':
                    piecesOnBoard += constPieces.whiteQueen.toString("queen-" + location, location, playerColour);
                    break;

                case 'K':
                    piecesOnBoard += constPieces.blackKing.toString("king-" + location, location, playerColour);
                    break;

                case 'k':
                    piecesOnBoard += constPieces.whiteKing.toString("king-" + location, location, playerColour);
                    break;
                }
            }
        }
        return piecesOnBoard;
    }

    function createChessBoard(playerColour, chessboardJSON) {
        var chessBoardSVG = $("#chessboard-surround"),
            board;
        chessBoardSVG.html(createBlankChessBoardSVG());
        board = $("#layer1");
        board.append(createChessPiecesElements(playerColour, chessboardJSON));
        chessBoardSVG.html(chessBoardSVG.html());
    }

    return {
        boardWidth : boardWidth,
        createBlankChessBoardSVG : createBlankChessBoardSVG,
        createChessBoard : createChessBoard
    };
}());