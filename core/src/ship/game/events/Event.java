package ship.game.events;

import ship.game.Card;
import ship.game.Player;

public class Event {
    private EventType type;
    private Card card;
    private Player player;


    public Event(EventType type) {
        this.type = type;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public EventType getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }

    public Player getPlayer() {
        return player;
    }
}
