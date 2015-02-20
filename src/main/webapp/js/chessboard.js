/**
 *
 * @file related chessboard functions
 * @author Adrian McLaughlin
 */

"use strict";

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
    var svgDocument = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    var svgNS = svgDocument.namespaceURI;
    svgDocument.setAttribute('height', '500px');
    svgDocument.setAttribute('width', '500px');

    var layer = document.createElementNS(svgNS, 'g');
    layer.setAttribute('id', 'layer1');

    var x = 0, y = 0, whiteToggle = true;
    for (var i = boardWidth;i > 0; i--) {
        x=0;
        for (var letter in letterCoordinates) {
            var rect=document.createElementNS(svgNS, 'rect');
            rect.setAttribute('id', (letterCoordinates[letter]+i));
            rect.setAttribute('x', x);
            rect.setAttribute('y', y);
            rect.setAttribute('width', 62.5);
            rect.setAttribute('height', 62.5);
            if(whiteToggle){
                rect.setAttribute('fill', whiteSquareColour);
            }else{
                rect.setAttribute('fill', blackSquareColour);
            }
            whiteToggle=!whiteToggle;
            rect.setAttribute('stroke', 'none');
            rect.setAttribute('fill-opacity', '1');
            rect.setAttribute('class', 'dropzone');
            layer.appendChild(rect);
            x = x + 62.5;    
        }
        y = y + 62.5;
        whiteToggle=!whiteToggle; //alternate black white pattern
        
    }
    svgDocument.appendChild(layer);
    return svgDocument;
}

function createChessBoard(chessboardJSON) {
    var json = $.parseJSON(chessboardJSON);
    console.log("parsed JSON");
    var chessBoardSVG=$("#chessboard-surround");
    chessBoardSVG.html(createBlankChessBoardSVG());
    for (var location in json){
        switch(json[location]){
                case "p":
                console.log("Create pawn");
                $("#layer1").append(chesspieces.pawn("test-"+location,location));
                break;
                case 'r':
                console.log("Create rook");
                $("#layer1").append(chesspieces.rook("test-"+location,location));
                break;
        }
    }
                
    chessBoardSVG.html(chessBoardSVG.html());
    
}