export var Colour = {
        white :{
            fill : '#ffd5d5',
            stroke : '#000000',
            toString : function () { return 'WHITE'; }
        },
        black : {
            fill : '#191406',
            stroke : '#ffffff',
            toString : function () { return 'BLACK'; }
        }
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
    };

export class ChessPiece {
    /**
     * x-axis origin in the SVG chessboard
     * @member
     *
     */
    x : 0;
    
    /**
     * y-axis origin in the SVG chessboard
     * @member
     */
    y : 0;
    
    /**
     * The size of the SVG squares in pixels
     * @member
     */
    offsetXY : 62.5;

    pieceColour : {fill, stroke};

    constructor(pieceColour) {
        this.pieceColour = this.checkColour(pieceColour);
    }

    getClasses(playerColour) : string {
        var colour = this.checkColour(playerColour);
        if ((this.pieceColour === Colour.white && colour == Colour.white) ||
            (this.pieceColour === Colour.black && colour == Colour.black)) {
            return 'chesspiece draggable';
        }
        return 'chesspiece';
    }

    getCoordX(location) {
        return this.x + (this.offsetXY * (coordinates[location.file] - 1));
    }

    getCoordY(location) {
        return this.y + (this.offsetXY * (8 - location.rank));
    }

            /**
    * Parses a Square coordinate in the form <letter><number>
    * into an object with properties rank and file
    *
    * @access public
    * @param {string} squareCoordinates
    * @return {object} containing rank and file coordinates
    * @throws {string} if coordinate can't be parsed
    */
    static parseSquareCoordinates(squareCoordinates: string) {
        var coordinateRegex = /([A-H])([1-8])/,
            coordinate;
        if (coordinateRegex.test(squareCoordinates)) {
            coordinate = coordinateRegex.exec(squareCoordinates);
            return {file : coordinate[1],
                    rank : coordinate[2]
                };
        }
        throw 'Not valid ChessBoard coordinate';
    }

    toString(id: string, squareCoordinates: string, playerColour:{fill, stroke}): string {
        return "";
    }

    getColour() {   
        return this.pieceColour; 
    }

    checkColour(colour) {
        if (typeof(colour) === 'string') {
            switch (colour) {
                case "BLACK":
                    return Colour.black;
                case "WHITE":
                    return Colour.white;
                default:
                    throw new Error("Can't create ChessPiece Object");
            }
        } else if (typeof(colour) === 'object' && (colour === Colour.black || 
            colour === Colour.white)) {
            return colour;
        } else {
            throw new Error("Can't create ChessPiece Object");
        }
    }
}
