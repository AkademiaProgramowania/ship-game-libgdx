package ship.game.server;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String collectedShipType;
    private boolean stillPlaying = true;
    int id;

    private List<Card> ownStack = new ArrayList<>();

    public Player() {
    }

    public Player(String collectedShipType) {
        this.collectedShipType = collectedShipType;
    }

    public void addCard(Card card) {
        ownStack.add(card);
    }

    public void removeCard(Card card) {
        ownStack.remove(card);
    }


    public List<Card> getCards(Card.Type type) {
        List<Card> cards = new ArrayList<>(); // may be change to stream filer collect
        for (Card card : ownStack) {
            if (card.getType().equals(type)) {
                cards.add(card);
            }
        }
        return cards;
    }

    public Card getCoinToPay() {
        Card toReturn = null;
        for (Card card : ownStack) {
            if (card.getType().equals(Card.Type.COIN)) {
                toReturn = card;
                ownStack.remove(card);
            }
        }
        return toReturn;
    }

    public List<Card> getShipsCollected(boolean collected) {
        List<Card> ships = getCards(Card.Type.SHIP); //wszystkie typu SHIP
        List<Card> result = new ArrayList<>();
        // may be changed to stream filer collect https://www.baeldung.com/java-stream-filter-lambda
        // (may be hard as we have boolean collected and method isCollectingThisShip
        for (Card card : ships) {
            if (isCollectingThisShip(card) && collected) {
                result.add(card);
            } else if (!isCollectingThisShip(card) && !collected) {
                result.add(card);
            }
        }
        return result; // zwraca jeden result
    }

/*    public void addShipCard(Card card) {
        setIfFirstCollected(card);
        ownStack.add(card);
    }*/

    public Card getSelectedShipCard(String givenType) {
        List<Card> ships = getCards(Card.Type.SHIP);
        Card selected = null;
        if (!ships.isEmpty()) {
            for (Card ship : ships) { // logic can be replaced by Stream and Optional statement
                // https://www.baeldung.com/java-stream-findfirst-vs-findany - return Optional of given type
                // https://www.baeldung.com/java-optional
                //
                if (ship.getSecondShipType().equals(givenType)) {
                    selected = ship;
                }
            }
        } else {
            System.out.println("Player has no requested card"); // probably bug, we must check if second player has ship of given type
        }
        return selected;
    }

    public void setCollected(Card card) {
        if (collectedShipType == null) { // collected u tego playera, a u innych?
            collectedShipType = card.getSecondShipType();
        }
    }

    public boolean checkIfLastShipCard() {
        return getShipsCollected(true).size() == 6;
    }

    public boolean chceckIfMoreThan3() { // may be replaced by streams and method filter().count
        // https://www.baeldung.com/java-stream-filter-count
        boolean more = false;
        int val = 0;
        for (Card card : ownStack) {
            val = val + card.getStormValue();
        }
        if (val > 3) {
            more = true;
        }
        return more;
    }

    public boolean isCollectingThisShip(Card card) {
        boolean isCollecting = false;
        // the condition inside if(... ) can be just returned as the method is boolean
        if ((collectedShipType != null) && (card.getType() == Card.Type.SHIP && card.getSecondShipType().equals(collectedShipType))) {
            isCollecting = true;
        }
        return isCollecting;
    }

    public int checkNumberOfMissingShipCards() {
        return 6 - getShipsCollected(true).size();
    }

    public void showOwnStack() {
        System.out.println("Stack - player " + id + ":");
        for (Card card : ownStack) {
            System.out.println(card);
        }
    }

    public boolean hasCards() {
        return ownStack.size() > 0;
    }

    public boolean stillPlaying(boolean stillPlaying) { // don't understand this method and it's usage
        return stillPlaying;
    }

    public String getPlayingStatus() {
        String status = "";
        if (isStillPlaying()) {
            status = "T";
        } else {
            status = "F";
        }
        return status;
    }

    public boolean isStillPlaying() { // geter do metody stillPlaying
        return stillPlaying;
    }

    public int getStackSize() {
        return ownStack.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectedShipType() {
        return collectedShipType;
    }

    public void setCollectedShipType(String collectedShipType) {
        this.collectedShipType = collectedShipType;
    }

    public List<Card> getOwnStack() {
        return ownStack;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", id=" + id +
                ", collectedShipType='" + collectedShipType + '\'' +
                ", last turn=" + getPlayingStatus() + '\'' +
                '}';
    }
}
