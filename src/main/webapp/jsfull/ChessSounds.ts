import { IChessAudio } from "./IChessAudio";

export class ChessSounds implements IChessAudio {

    private errorSound: HTMLAudioElement = new Audio("/AMCChessGame/audio/alert1.mp3");

    private checkSound: HTMLAudioElement = new Audio("/AMCChessGame/audio/tone.mp3");

    private winSound: HTMLAudioElement = new Audio("/AMCChessGame/audio/applause.mp3");

    private moveSound = new Audio("/AMCChessGame/audio/chessSound.mp3");

    public playErrorSound(): void {
        this.errorSound.play();
    }

    public playCheckSound(): void {
        this.checkSound.play();
    }

    public playPlayerWinsSound(): void {
        this.winSound.play();
    }

    public playMoveSound(): void {
        this.moveSound.play();
    }
}
