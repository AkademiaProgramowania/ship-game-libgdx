package ship.game.events;

public enum EventType {
    GAME_START,
    TURN_START,
    DRAW_CARD_DECISION,
    GAME_END,
    PASS_DECISION,
    DRAW_CARD,
    CLICK_ON_COIN,
    CLICK_ON_CANNON,
    CLICK_ON_SHIP,
    CLICK_ON_SHIP_COLLECTED,
    SELECT_CARDS_TO_RETURN,
    CARD_PURCHASE_DECISION,

    // stare:
    SET_SHIP_TYPE_TO_COLLECT, // Typy: "S1"/"S2"/"S3"/"S4"
    SHIP_COLLECTED, // zebrane 6 części statku
    PLAYER_SWITCHED, // zmiana na gracza gdy kończy rundę lub zadecyduje że pas
    STORM_CAME,
    STORM_NO_CARDS,
    SHOW_CARD,
    SHOW_CARD_RETURNED,
    SHOW_STACK,
    CARD_PURCHASE,
    STACK_SHUFFLED,
    CURRENT_PLAYER;

}
