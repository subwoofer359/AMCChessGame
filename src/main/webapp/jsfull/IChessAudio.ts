export interface IChessAudio {

    playErrorSound(): void;

    playCheckSound(): void;

    playPlayerWinsSound(): void;

    playMoveSound(): void;
}

export class NoChessSounds implements IChessAudio {
    public playErrorSound(): void {
        // Play no sound
    }

    public playCheckSound(): void {
        // Play no sound
    }

    public playPlayerWinsSound(): void {
        // Play no sound
    }

    public playMoveSound(): void {
        // Play no sound
    }
}
