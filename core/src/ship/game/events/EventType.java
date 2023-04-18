package ship.game.events;

public enum EventType {
    CARD_DRAWN,
    SHIP_TYPE_TO_COLLECT, // Typy: "S1"/"S2"/"S3"/"S4"
    TURN_END,
    CARD_TO_RETURN,
    SHIP_COLLECTED, // zebrane 6 części statku
    PLAYER_SWITCHED, // zmiana na gracza gdy kńćzy rundę lub zadecyduje że pas
    STORM_CAME,
    STACK_FILLED;
}
