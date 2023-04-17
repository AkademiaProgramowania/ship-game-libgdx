package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    String collectedShipColor = null;
    private final List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    private Game game;

    public Player(Game game) {
        this.game = game;
    }

    public void draw() {
        List<Card> all = game.getMainStack();
        Card drawn = all.get(0);
        all.remove(0);
    }

    public Card findCardByTypeInOwnStack(Card.Type type) { // metoda wynajdzie pierwszą kartę danego typu
        for (Card card : ownStack) {
            if (card.getType().equals(type)) {
                return card;
            }
        }
        return null;
    }

    public List<Card> getOwnStack() {
        return ownStack;
    }
}
