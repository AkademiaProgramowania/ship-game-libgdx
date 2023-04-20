package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player implements EventListener {

    String collectedShipType;
    private final List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    public Player() {
        EventBus.subscribe(EventType.STACK_FILLED, this);
        EventBus.subscribe(EventType.SHIP_TYPE_TO_COLLECT, this);
        EventBus.subscribe(EventType.SHOW_CARD, this);
        EventBus.subscribe(EventType.SHOW_STACK, this);
        EventBus.subscribe(EventType.RETURNED_CARDS, this);

    }

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

    public Card findShipCardToReturn() { // sa SHIP a nie są collectedShipCards
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && !card.getSecondShipType().equals(collectedShipType)) {
                return card;
            }
        }
        return null;
    }

    public void checkIfFirstShipCardAndSetCollected(Card card) {
        for (Card card1 : ownStack) { // każda karta z ownStack
            if (!card1.getType().equals(Card.Type.SHIP)) { // jeśli żadna karta w stacku nie ma typu SHIP
                collectedShipType = card1.getSecondShipType();// i ustawia jej typ jako zbierany
                EventBus.notify(new Event(EventType.SHIP_TYPE_TO_COLLECT));
            }
        }
    }

    public void checkIfSetShipCardsAsCollected() {
        for (Card card : ownStack) {
            if (card.getSecondShipType().equals(collectedShipType)) {
                collectedShipType = card.getSecondShipType();
            }
        }
    }

    public Card showOwnStack() {
        List<Card> ships = new ArrayList<>();
        List<Card> coins = new ArrayList<>();
        List<Card> cannons = new ArrayList<>();

        for (Card card : ownStack) {
            if (card.equals(findCardByTypeInOwnStack(Card.Type.SHIP))) {
                ships.add(card);
            }
            if (card.equals(findCardByTypeInOwnStack(Card.Type.COIN))) {
                coins.add(card);
            }
            if (card.equals(findCardByTypeInOwnStack(Card.Type.CANNON))) {
                cannons.add(card);
            }
        }
        for (Card cardToShow : ships) {
            EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
            return cardToShow;
        }
        for (Card cardToShow : coins) {
            EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
            return cardToShow;
        }
        for (Card cardToShow : cannons) {
            EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
            return cardToShow;
        }
        return null;
    }

    public List<Card> chooseCardsToReturn() {
        EventBus.notify(new Event(EventType.SHOW_STACK));
        showOwnStack();

        List<Card> toReturn = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int num = 0;

        while (num <= 3) {
            System.out.println("Choose a card to return");
            System.out.println("You need " + (3 - num) + " cards more");
            System.out.println("For a ship enter 1");
            System.out.println("For a coin enter 2");
            System.out.println("For a cannon enter 3");
            int entered = scanner.nextInt();

            Card cardShip = findShipCardToReturn();
            Card cardCoin = findCardByTypeInOwnStack(Card.Type.COIN);
            Card cardCannon = findCardByTypeInOwnStack(Card.Type.CANNON);
            switch (entered) {
                case 1:
                    toReturn.add(cardShip);
                    num++;
                case 2:
                    toReturn.add(cardCoin);
                    num++;
                case 3:
                    toReturn.add(cardCannon);
                    num++;
            }
            EventBus.notify(new Event(EventType.RETURNED_CARDS));
            for (Card card : toReturn) {
                System.out.println(card);
            }
        }
        return toReturn;
    }

    public Card giveRequestedShipCard(String type) { // działa player o wskazanym indeksie
        for (Card card1 : ownStack) {
            if (card1.getSecondShipType().equals(type)) {
                return card1;
            }
        }
        return null;
    }

    public Card giveCoinCard() {
        for (Card card1 : ownStack) {
            if (card1.getType().equals(Card.Type.COIN)) {
                return card1;
            }
        }
        return null;
    }

    public List<Card> getOwnStack() {
        return ownStack;
    }

    public String getCollectedShipType() {
        return collectedShipType;
    }

    @Override
    public String toString() {
        return "Player{" +
                "ownStack=" + ownStack +
                '}';
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.STACK_FILLED) {
            System.out.println("Card added to your stack");
        }
        if (event.getType() == EventType.SHIP_TYPE_TO_COLLECT) {
            System.out.println("It's your ship type to collect: " + collectedShipType);
        }
        if (event.getType() == EventType.SHOW_CARD) {
            System.out.println(event.getCard());
        }
        if (event.getType() == EventType.SHOW_STACK) {
            System.out.println("Your stack");
        }
        if (event.getType() == EventType.RETURNED_CARDS) {
            System.out.println("3 cards are back to the main stack: ");
        }
    }
}
