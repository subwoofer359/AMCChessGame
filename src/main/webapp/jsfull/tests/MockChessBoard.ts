import * as Chessboard from "../Chessboard";

export const boardWidth = Chessboard.boardWidth;

export const letterCoordinates = Chessboard.letterCoordinates;

export let updateChessBoardCall: boolean = false;

export function createBlankChessBoardSVG(): string {
    return "";
}

export function createChessBoard(playerColour, chessboardJSON): void {
    updateChessBoardCall = true;
    Chessboard.createChessBoard(playerColour, chessboardJSON);
}

export function move(moveStr: string, callback ?: () => void ): void {
    Chessboard.move(moveStr, callback);
}

export function setUpdateChessBoardCall(value: boolean) {
    updateChessBoardCall = value;
}
