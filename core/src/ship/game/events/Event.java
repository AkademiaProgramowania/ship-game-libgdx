package ship.game.events;

import ship.game.Card;

public class Event {
    private EventType type;
    private Card card;

    public Event(EventType type, Card card) {
        this.type = type;
        this.card = card;
    }

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }
}
