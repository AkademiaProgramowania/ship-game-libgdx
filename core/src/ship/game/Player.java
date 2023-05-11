package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Player implements EventListener {

    String collectedShipType; //todo private + gettery
    boolean stillPlaying = true;
    int playerNum;

    private List<Card> ownStack = new ArrayList<>();

    public Player(int playerNum) {
        this.playerNum = playerNum;

        //stare:
        EventBus.subscribe(EventType.SHOW_STACK, this);
    }

    public void addCard(Card card) {
        ownStack.add(card);
    }

    public void removeCard(Card card) {
        ownStack.remove(card);
    }


    public List<Card> getCards(Card.Type type) {
        List<Card> cards = new ArrayList<>();
        for (Card card : ownStack) {
            if (card.getType().equals(type)) {
                cards.add(card);
            }
        }
        return cards;
    }

    public List<Card> getOwnStack() {
        return ownStack;
    }

    public List<Card> getShipsCollected(boolean collected) {
        List<Card> ships = getCards(Card.Type.SHIP);
        List<Card> result = new ArrayList<>();
//        List<Card> collectedShips = new ArrayList<>();
//        List<Card> notCollected = new ArrayList<>();
        for (Card card : ships) {
            if (isCollectingThisShip(card) && collected) {
                result.add(card);
            } else if (!isCollectingThisShip(card) && !collected) {
                result.add(card);
            }
        }
       return result;
    }

    public void storeShipCard(Card ship) {
        setIfFirstCollected(ship);
        ownStack.add(ship);
    }

    private void setIfFirstCollected(Card card) { // przekazuję drawn żeby z niej pobrać typ
        if (collectedShipType == null) {
            collectedShipType = card.getSecondShipType();
        }
    }

    public boolean isCollectingThisShip(Card card) {
        return card.getType() == Card.Type.SHIP && collectedShipType.equals(card.getSecondShipType());
    }

    public int checkNumberOfMissingShipCards() {
        return 6 - getShipsCollected(true).size();
    }


    public void showOwnStack() {
        System.out.println("Stack - player " + getPlayerNum() + ":");

        for (Card card : ownStack) {
            System.out.println(card);
        }

        /*System.out.println("Statki zbierane " + shipsCollected.size() + " /Statki nie zbierane "
                + shipsToReturn.size() + " /Monety " + coins.size() + " /Dziala " + cannons.size());
        System.out.println("Collected type: " + collectedShipType);*/

    }

    public boolean hasCards() {
        return ownStack.size() > 0;
    }

    public void removeIfPresent(Card card) {

    }

    public boolean checkIfLastShipCard() {
        return  getShipsCollected(true).size() == 6;
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
                "collectedShipType='" + collectedShipType + '\'' +
                ", stillPlaying=" + stillPlaying +
                ", playerNum=" + playerNum +
                '}';
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.SHOW_STACK) {
            System.out.println("Your stack");
            showOwnStack();
        }
    }
}
