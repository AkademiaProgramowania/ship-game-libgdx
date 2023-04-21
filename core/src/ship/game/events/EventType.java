package ship.game.events;

public enum EventType {
    CARD_DRAWN,
    SHIP_TYPE_TO_COLLECT, // Typy: "S1"/"S2"/"S3"/"S4"
    GAME_END,
    SHIP_COLLECTED, // zebrane 6 części statku
    PLAYER_SWITCHED, // zmiana na gracza gdy kńćzy rundę lub zadecyduje że pas
    STORM_CAME,
    STORM_NO_CARDS,
    SHOW_CARD,
    SHOW_CARD_RETURNED,
    SHOW_STACK,
    RETURNED_CARDS,
    CARD_PURCHASE,
    STACK_SHUFFLED,
    CURRENT_PLAYER,
    STACK_FILLED;
}
