var ChessPieceModule = (function () {

    var Colour = {
            white :{},
            black : {}
        },
        coordinates = {
            A : 1,
            B : 2,
            C : 3,
            D : 4,
            E : 5,
            F : 6,
            G : 7,
            H : 8
        }

    function ChessPiece(colour) {
        this.colour = colour;
    }

    ChessPiece.prototype = {
        /**
         * x-axis origin in the SVG chessboard
         * @member
         *
         */
        x : 0,
        
        /**
         * y-axis origin in the SVG chessboard
         * @member
         */
        y : 0,
        
        /**
         * The size of the SVG squares in pixels
         * @member
         */
        offsetXY : 62.5,
        
        getClasses : function (playerColour) {
            if ((this.colour === "WHITE" && playerColour === Colour.white) ||
                (this.colour === "BLACK" && playerColour === Colour.black)) {
                return 'chesspiece draggable';
            }
            return 'chesspiece';
        }, 
        getCoordX : function (location) {
            return this.x + (this.offsetXY * (coordinates[location.file] - 1));
        },
            
        getCoordY : function (location) {
            return this.y + (this.offsetXY * (8 - location.rank));
        },
        /**
        * Parses a Square coordinate in the form <letter><number>
        * into an object with properties rank and file
        *
        * @access public
        * @param {string} squareCoordinates
        * @return {object} containing rank and file coordinates
        * @throws {string} if coordinate can't be parsed
        */
        parseSquareCoordinates : function (squareCoordinates) {
            var coordinateRegex = /([A-H])([1-8])/,
                coordinate;
            if (coordinateRegex.test(squareCoordinates)) {
                coordinate = coordinateRegex.exec(squareCoordinates);
                return {file : coordinate[1],
                        rank : coordinate[2]
                    };
            }
            throw 'Not valid ChessBoard coordinate';
        },
        toString : function () {
            return "";
        }
    }

    return {
        Colour : Colour,
        coordinates : coordinates,
        ChessPiece : ChessPiece
    }
})();