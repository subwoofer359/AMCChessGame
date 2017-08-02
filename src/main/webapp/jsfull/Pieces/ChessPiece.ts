export let Colour = {
        black : {
            fill : '#191406',
            stroke : '#ffffff',
            toString : 'BLACK',
        },
        white : {
            fill : '#ffd5d5',
            stroke : '#000000',
            toString : 'WHITE',
        },
    };

export let coordinates = {
        A : 1,
        B : 2,
        C : 3,
        D : 4,
        E : 5,
        F : 6,
        G : 7,
        H : 8,
    };

export class ChessPiece {

    /**
     * Parses a Square coordinate in the form <letter><number>
     * into an object with properties rank and file
     *
     * @access public
     * @param {string} squareCoordinates
     * @return {object} containing rank and file coordinates
     * @throws {string} if coordinate can't be parsed
     */
    public static parseSquareCoordinates(squareCoordinates: string) {
        const coordinateRegex = /^([A-H])([1-8])$/;

        if (coordinateRegex.test(squareCoordinates)) {
            const coordinate = coordinateRegex.exec(squareCoordinates);
            return {file : coordinate[1],
                    rank : coordinate[2],
                };
        }
        throw new Error('Not valid ChessBoard coordinate');
    }

    /**
     * x-axis origin in the SVG chessboard
     * @member
     *
     */
    public readonly x = 0;

    /**
     * y-axis origin in the SVG chessboard
     * @member
     */
    public readonly y = 0;

    /**
     * The size of the SVG squares in pixels
     * @member
     */
    public  readonly offsetXY = 62.5;

    private  _pieceColour: { fill, stroke };

    constructor(pieceColour) {
        this._pieceColour = this.checkColour(pieceColour);
    }

    public getCoordX(location) {
        return this.x + (this.offsetXY * (coordinates[location.file] - 1));
    }

    public getCoordY(location) {
        return this.y + (this.offsetXY * (8 - location.rank));
    }

    public toString(id: string, squareCoordinates: string, playerColour: { fill, stroke }): string {
        return "";
    }

    public get pieceColour(): { fill, stroke } {
        return this._pieceColour;
    }

    public getClasses(playerColour): string {
        const colour = this.checkColour(playerColour);
        if ((this.pieceColour === Colour.white && colour === Colour.white) ||
            (this.pieceColour === Colour.black && colour === Colour.black)) {
            return 'chesspiece draggable';
        }
        return 'chesspiece';
    }

    private checkColour(colour) {
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
