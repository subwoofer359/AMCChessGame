export class StompObject {

    public headers: any;

    public url: string;

    public gameUUID: string;

    public playerName: string;

    public opponentName: string;

    public playerColour: string;

    constructor(url?: string, gameUid?: string, playerName?: string, opponentName?: string, playerColour?: string) {
        this.url = url || "";
        this.gameUUID = gameUid || "";
        this.playerName = playerName || "";
        this.opponentName = opponentName || "";
        this.playerColour = playerColour || "";
        this.headers = {};
    }
}
