package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Player {

    String collectedShipType;
    private final List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    public void addToOwnStack(Card card) {
        ownStack.add(card);
        EventBus.notify(new Event(EventType.STACK_FILLED, card));
    }

    public Card findCardByTypeInOwnStack(Card.Type type) { // metoda da pierwszą kartę danego typu
        for (Card card : ownStack) {
            if (card.getType().equals(type)) {
                return card;
            }
        }
        return null;
    }

    public Card findShipCardToReturn() { // te które sa w ownStack a nie są collectedShipCards
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && !card.getSecondShipType().equals(collectedShipType)) {
                return card;
            }
        }
        return null;
    }

    public Card findCardByTypeAndNumInOwnStack(Card.Type type, int num) {
        for (Card card : ownStack) {
            if (card.getType().equals(type) && card.getNum() == num) {
                return card;
            }
        }
        return null;
    }

    public void checkIfSetToCollected(Card card) {
        for (Card card1 : ownStack) { // każda karta z ownStack
            if (!card1.getType().equals(Card.Type.SHIP)) { // jeśli żadna karta w stacku nie ma typu SHIP
                collectedShipType = card1.getSecondShipType();// to aktualny secondType ustawiony na zbierany
                EventBus.notify(new Event(EventType.SHIP_TYPE_TO_COLLECT)); // i notyfikacja
            }

        }

    }

    @Override
    public String toString() {
        return "Player{" +
                "ownStack=" + ownStack +
                '}';
    }
}
