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

import { BishopPiece } from "./Pieces/BishopPiece";
import { ChessPiece, Colour } from "./Pieces/ChessPiece";
import { KingPiece } from "./Pieces/KingPiece";
import { KnightPiece } from "./Pieces/KnightPiece";
import { PawnPiece } from "./Pieces/PawnPiece";
import { QueenPiece } from "./Pieces/QueenPiece";
import { RookPiece } from "./Pieces/RookPiece";

export const letterCoordinates = [ "A", "B", "C", "D", "E", "F", "G", "H" ];

export const boardWidth: number = 8;

const whiteSquareColour: string = "#e6e6e6";

const blackSquareColour: string = "#000000";

const serialiser = new XMLSerializer();

const constPieces: any = {
        blackBishop : new BishopPiece(Colour.black),
        blackKing: new KingPiece(Colour.black),
        blackKnight : new KnightPiece(Colour.black),
        blackPawn : new PawnPiece(Colour.black),
        blackQueen : new QueenPiece(Colour.black),
        blackRook : new RookPiece(Colour.black),

        whiteBishop : new BishopPiece(Colour.white),
        whiteKing : new KingPiece(Colour.white),
        whiteKnight : new KnightPiece(Colour.white),
        whitePawn : new PawnPiece(Colour.white),
        whiteQueen : new QueenPiece(Colour.white),
        whiteRook : new RookPiece(Colour.white),
    };

/**
 * Creates a SVG of an empty chessboard
 *
 * @public
 * @returns SVG element
 */
export function createBlankChessBoardSVG(): string {
    const svgDocument = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    const svgNS = svgDocument.namespaceURI;
    const layer = document.createElementNS(svgNS, "g");
    let x: number = 0;
    let y: number = 0;
    let whiteToggle: boolean = true;
    let i: number;
    let letter: string;
    let rect;

    svgDocument.setAttribute("viewBox", "0 0 500 500");
    layer.setAttribute("id", "layer1");

    for (i = boardWidth; i > 0; i -= 1) {
        x = 0;
        for (letter in letterCoordinates) {
            if (letterCoordinates.hasOwnProperty(letter)) {
                rect = document.createElementNS(svgNS, "rect");
                rect.setAttribute("id", (letterCoordinates[letter] + i));
                rect.setAttribute("x", x);
                rect.setAttribute("y", y);
                rect.setAttribute("width", 62.5);
                rect.setAttribute("height", 62.5);
                if (whiteToggle) {
                    rect.setAttribute("fill", whiteSquareColour);
                } else {
                    rect.setAttribute("fill", blackSquareColour);
                }
                whiteToggle = !whiteToggle;
                rect.setAttribute("stroke", "none");
                rect.setAttribute("fill-opacity", "1");
                rect.setAttribute("class", "dropzone");
                layer.appendChild(rect);
                x = x + 62.5;
            }
        }
        y = y + 62.5;
        whiteToggle = !whiteToggle; // alternate black white pattern

    }
    svgDocument.appendChild(layer);
    return serialiser.serializeToString(svgDocument);
}

function createChessPiecesElements(playerColour, chessboardJSON): string {
    const json = JSON.parse(chessboardJSON);
    let location: string;
    let piecesOnBoard: string = "";

    for (location in json.squares) {
        if (json.squares.hasOwnProperty(location)) {
            switch (json.squares[location]) {
            case "p":
                piecesOnBoard += constPieces.whitePawn.toString("pawn-" + location, location, playerColour);
                break;

            case "P":
                piecesOnBoard += constPieces.blackPawn.toString("pawn-" + location, location, playerColour);
                break;

            case "r":
                piecesOnBoard += constPieces.whiteRook.toString("rook-" + location, location, playerColour);
                break;

            case "R":
                piecesOnBoard += constPieces.blackRook.toString("rook-" + location, location, playerColour);
                break;

            case "B":
                piecesOnBoard += constPieces.blackBishop.toString("bishop-" + location, location, playerColour);
                break;

            case "b":
                piecesOnBoard += constPieces.whiteBishop.toString("bishop-" + location, location, playerColour);
                break;

            case "N":
                piecesOnBoard += constPieces.blackKnight.toString("knight-" + location, location, playerColour);
                break;

            case "n":
                piecesOnBoard += constPieces.whiteKnight.toString("knight-" + location, location, playerColour);
                break;

            case "Q":
                piecesOnBoard += constPieces.blackQueen.toString("queen-" + location, location, playerColour);
                break;

            case "q":
                piecesOnBoard += constPieces.whiteQueen.toString("queen-" + location, location, playerColour);
                break;

            case "K":
                piecesOnBoard += constPieces.blackKing.toString("king-" + location, location, playerColour);
                break;

            case "k":
                piecesOnBoard += constPieces.whiteKing.toString("king-" + location, location, playerColour);
                break;
            }
        }
    }
    return piecesOnBoard;
}

export function createChessBoard(playerColour, chessboardJSON): void {
    const chessBoardSVG = $("#chessboard-surround");
    let board;
    chessBoardSVG.html(createBlankChessBoardSVG());
    board = $("#layer1");
    board.append(createChessPiecesElements(playerColour, chessboardJSON));
    chessBoardSVG.html(chessBoardSVG.html());
}

export function move(moveStr: string, callback ?: () => void ): void {
    const s = Snap("svg");
    const moveRegex = /^([A-H][1-8])-([A-H][1-8])$/;

    if (moveStr !== "" && s) {
        if (moveRegex.test(moveStr)) {
            const square = moveRegex.exec(moveStr);
            const coord = getMoveCoordinates(square[1], square[2]);
            const pieceToMove = s.select(`g[id *= "${square[1]}"]`);

            if (pieceToMove) {
                pieceToMove.transform(`T${coord.start.x},${coord.start.y}`);
                pieceToMove.animate({transform: `t${coord.end.x},${coord.end.y}`}, 1000, mina.bounce, callback);
                return;
            }
        } else {
            throw new Error("Not valid Move coordinates");
        }
    }

    if (callback) {
        callback();
    }
}

function getMoveCoordinates(start: string, end: string) {
    const piece = new ChessPiece(Colour.white);
    const s = ChessPiece.parseSquareCoordinates(start);
    const e = ChessPiece.parseSquareCoordinates(end);
    const startLoc = {
                x: piece.getCoordX(s),
                y: piece.getCoordY(s),
            };
    const endLoc = {
                x: piece.getCoordX(e),
                y: piece.getCoordY(e),
        };
    return {
        end : endLoc,
        start : startLoc,
    };
}
