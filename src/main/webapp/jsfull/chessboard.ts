/*global $*/
/*global chesspiecesModule*/
/*global document*/

/**
 *
 * @file related chessboard functions
 * @author Adrian McLaughlin
 */
import "jquery";
import * as Snap from "snapsvg";

import { Colour, ChessPiece } from "./Pieces/ChessPiece";
import { QueenPiece } from "./Pieces/QueenPiece";
import { KingPiece } from "./Pieces/KingPiece";
import { KnightPiece } from "./Pieces/KnightPiece";
import { RookPiece } from "./Pieces/RookPiece";
import { PawnPiece } from "./Pieces/PawnPiece";
import { BishopPiece } from "./Pieces/BishopPiece";




export var chessboardModule = (function () {
    "use strict";
    var letterCoordinates = [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' ],
        boardWidth : number = 8,
        whiteSquareColour : string = '#e6e6e6',
        blackSquareColour :string = '#000000',
        serialiser = new XMLSerializer(),
        constPieces : any = {
            blackQueen : new QueenPiece(Colour.black),
            blackKing : new KingPiece(Colour.black),
            blackKnight : new KnightPiece(Colour.black),
            blackBishop : new BishopPiece(Colour.black),
            blackRook : new RookPiece(Colour.black),
            blackPawn : new PawnPiece(Colour.black),

            whiteQueen : new QueenPiece(Colour.white),
            whiteKing : new KingPiece(Colour.white),
            whiteKnight : new KnightPiece(Colour.white),
            whiteBishop : new BishopPiece(Colour.white),
            whiteRook : new RookPiece(Colour.white),
            whitePawn : new PawnPiece(Colour.white)
        };
        


    /**
     * Creates a SVG of an empty chessboard
     *
     * @public
     * @returns SVG element
     */
    function createBlankChessBoardSVG() : string {
        var svgDocument = document.createElementNS("http://www.w3.org/2000/svg", "svg"),
            svgNS = svgDocument.namespaceURI,
            layer = document.createElementNS(svgNS, 'g'),
            x : number = 0,
            y : number = 0,
            whiteToggle : boolean = true,
            i : number,
            letter : string,
            rect;

        svgDocument.setAttribute('viewBox', '0 0 500 500');
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
        return serialiser.serializeToString(svgDocument);
    }

    function createChessPiecesElements(playerColour, chessboardJSON) : string {
        var json = JSON.parse(chessboardJSON),
            location : string,
            piecesOnBoard : string = "";

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

    function createChessBoard(playerColour, chessboardJSON) : void {
        var chessBoardSVG = $("#chessboard-surround"),
            board;
        chessBoardSVG.html(createBlankChessBoardSVG());
        board = $("#layer1");
        board.append(createChessPiecesElements(playerColour, chessboardJSON));
        chessBoardSVG.html(chessBoardSVG.html());
    }

    function move(moveStr : string, callback ?: () => void ) : void {
        let s = Snap("svg"),
            moveRegex = /^([A-H][1-8])-([A-H][1-8])$/;
        
        if (moveStr !== "" && s) {
            if (moveRegex.test(moveStr)) {
                let square = moveRegex.exec(moveStr),
                    coord = getMoveCoordinates(square[1], square[2]),
                    pieceToMove = s.select(`g[id *= "${square[1]}"]`);
                
                if (pieceToMove) {
                    pieceToMove.transform(`T${coord.start.x},${coord.start.y}`);
                    pieceToMove.animate({transform: `t${coord.end.x},${coord.end.y}`}, 1000, mina.bounce, callback);
                    return;
                }
            } else {
                throw "Not valid Move coordinates";
            }
        }

        if (callback) {
            callback();
        }
    }

    function getMoveCoordinates(start: string, end: string) {
        let piece = new ChessPiece(Colour.white),
            s = ChessPiece.parseSquareCoordinates(start),
            e = ChessPiece.parseSquareCoordinates(end),
            startLoc = {
                    x: piece.getCoordX(s), 
                    y: piece.getCoordY(s)
                },
            endLoc = {
                    x: piece.getCoordX(e), 
                    y: piece.getCoordY(e)
            };
        return {
            start : startLoc, 
            end : endLoc
        };
    }

    return {
        boardWidth : boardWidth,
        createBlankChessBoardSVG : createBlankChessBoardSVG,
        createChessBoard : createChessBoard,
        move : move
    };
}());