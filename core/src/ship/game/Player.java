package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Player implements EventListener {

    String collectedShipType;
    int playerNum;
    private List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    private List<Card> toReturn = new ArrayList<>();

    public Player(int playerNum) {
        this.playerNum = playerNum;

        //stare:
        EventBus.subscribe(EventType.SHOW_STACK, this);
        EventBus.subscribe(EventType.RETURNED_CARDS, this);
    }

    public void addToOwnStack(Card card) {
        ownStack.add(card);
        showOwnStack();
        //EventBus.notify(new Event(EventType.STACK_FILLED));
    }

    public Card findCardByTypeInOwnStack(Card.Type type) { // uwaga! Metoda da pierwszą kartę danego typu
        for (Card card : ownStack) {
            if (card.getType().equals(type)) {
                return card;
            }
        }
        return null;
    }



/*    public Card findShipCardToReturn() { // w pierwszej kolejności oddawać te, które nie są collectedShipCards
        List<Card> toReturn = new ArrayList<>();
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && (!card.getSecondShipType().equals(collectedShipType))) {
                toReturn.add(card);
            }
        }
        if (toReturn.size() > 0) {
            for (Card card : toReturn) {
                return card;
            }
        } else {
            for (Card card : ownStack) {
                if (card.getType().equals(Card.Type.SHIP)) {
                    return card; // dodać usuwanie z ownStack?
                }
            }
        }
        return null;
    }*/


    public boolean checkIfFirstShipCardAndSetCollected(Card card) { // przekazuję drawn żeby z niej pobrać typ
        if (collectedShipType == null) {
            collectedShipType = card.getSecondShipType();
        }
        return false;
    }

/*    public void checkIfSetShipCardsAsCollected() {
        for (Card card : ownStack) { // każda karta
            if (card.getSecondShipType().equals(collectedShipType)) {
                // jeśli jej drugi typ jest taki sam jak ustawiony na kolekcjonowanie
                collectedShipType = card.getSecondShipType();
            }
        }
    }*/

    public void showOwnStack() {
        System.out.println("Stack - player " + getPlayerNum() + ":");
        List<Card> shipsCollected = new ArrayList<>();
        List<Card> shipsToReturn = new ArrayList<>();
        List<Card> coins = new ArrayList<>();
        List<Card> cannons = new ArrayList<>();

        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && (!card.getSecondShipType().equals(collectedShipType))) {
                shipsCollected.add(card);
            }
            if (card.getType().equals(Card.Type.SHIP) && (card.getSecondShipType().equals(collectedShipType))) {
                shipsToReturn.add(card);
            }
            if (card.getType().equals(Card.Type.COIN)) {
                coins.add(card);
            }
            if (card.getType().equals(Card.Type.CANNON)) {
                cannons.add(card);
            }
        }
        for (Card cardToShow : shipsCollected) {
            System.out.println(cardToShow);
           // EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
        }
        for (Card cardToShow : shipsToReturn) {
            System.out.println(cardToShow);
            //EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
        }
        for (Card cardToShow : coins) {
            System.out.println(cardToShow);
            //EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
        }
        for (Card cardToShow : cannons) {
            System.out.println(cardToShow);
            //EventBus.notify(new Event(EventType.SHOW_CARD, cardToShow));
        }
    }

/*    public List<Card> chooseCardsToReturn() {
        EventBus.notify(new Event(EventType.SHOW_STACK));
        showOwnStack();

        List<Card> toReturn = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int num = 0;

        while (num <= 3) {
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
    }*/


    public Card findCoinToReturn() {
        Card card = findCardByTypeInOwnStack(Card.Type.COIN);
        ownStack.remove(card);
        return card;
    }
    public Card findCannonToReturn() {
        Card card = findCardByTypeInOwnStack(Card.Type.CANNON);
        ownStack.remove(card);
        return card;
    }

    public Card findShipToReturn() {
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && (!card.getSecondShipType().equals(collectedShipType))) {
                ownStack.remove(card);
                return card;
            }
        }
        return null;
    }

    public int checkHowManyCoins() {
        int num = 0;
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.COIN)) {
                num++;
            }
        }
        return num;
    }

    public Card findCollectedShipToReturn() {
        for (Card card : ownStack) {
            if (card.getSecondShipType().equals(collectedShipType)) {
                ownStack.remove(card);
                return card;
            }
        }
        return null;
    }

    public Card giveRequestedShipCard(String type) { // działa player o wskazanym indeksie
        for (Card card1 : ownStack) {
            if (card1.getSecondShipType().equals(type)) {
                return card1;
            }
        }
        return null;
    }

    public boolean checkIfLastShipCard() {
        boolean last = false;
        int num = 0;
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.SHIP) && (card.getSecondShipType().equals(collectedShipType))) {
                num++;
            }
        }
        if (num == 5) {
            last = true;
        }
        return last;
    }

    public Card giveCoinCard() {
        for (Card card1 : ownStack) {
            if (card1.getType().equals(Card.Type.COIN)) {
                return card1;
            }
        }
        return null;
    }

    public int getPlayerNum() {
        return playerNum;
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
                "playerNum=" + playerNum +
                ", ownStack=" + ownStack +
                '}';
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.SHOW_STACK) {
            System.out.println("Your stack");
        }
        if (event.getType() == EventType.RETURNED_CARDS) {
            System.out.println("3 cards are back to the main stack: ");
        }
        /*if (event.getType() == EventType.SHIP_TYPE_TO_COLLECT) {
            System.out.println("Ship type set to collect player " + getPlayerNum() + " - " + collectedShipType);
        }*/
    }
}
