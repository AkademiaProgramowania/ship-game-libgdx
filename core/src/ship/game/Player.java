package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Player implements EventListener {

    String collectedShipType;
    boolean stillPlaying;
    int playerNum;
    List<Card> shipsCollected = new ArrayList<>();
    List<Card> shipsToReturn = new ArrayList<>();
    List<Card> coins = new ArrayList<>();
    List<Card> cannons = new ArrayList<>();

    public Player(int playerNum) {
        this.playerNum = playerNum;

        //stare:
        EventBus.subscribe(EventType.SHOW_STACK, this);
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

    public int checkNumberOfMissingShipCards() {
        return 6 - (shipsCollected.size());
    }


    public void showOwnStack() {
        System.out.println("Stack - player " + getPlayerNum() + ":");
        for (Card cardToShow : shipsCollected) {
            System.out.println(cardToShow);
        }
        for (Card cardToShow : shipsToReturn) {
            System.out.println(cardToShow);
        }
        for (Card cardToShow : coins) {
            System.out.println(cardToShow);
        }
        for (Card cardToShow : cannons) {
            System.out.println(cardToShow);
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

/*    public Card giveRequestedShipCard(String type) { // działa player o wskazanym indeksie
        for (Card card1 : ownStack) {
            if (card1.getSecondShipType().equals(type)) {
                return card1;
            }
        }
        return null;
    }*/

    public boolean checkIfLastShipCard() {
        return shipsCollected.size() == 6;
    }

    public boolean stillPlaying(boolean stillPlaying) {
        return stillPlaying;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public String getCollectedShipType() {
        return collectedShipType;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerNum=" + playerNum +
                '}';
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.SHOW_STACK) {
            System.out.println("Your stack");
            showOwnStack();
        }

        /*if (event.getType() == EventType.SHIP_TYPE_TO_COLLECT) {
            System.out.println("Ship type set to collect player " + getPlayerNum() + " - " + collectedShipType);
        }*/
    }
}
