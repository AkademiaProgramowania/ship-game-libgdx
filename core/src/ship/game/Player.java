package ship.game;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String collectedShipType;
    private boolean stillPlaying = true;
    int playerNum;

    private List<Card> ownStack = new ArrayList<>();

    public Player(int playerNum) {
        this.playerNum = playerNum;
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

    public List<Card> getShipsCollected(boolean collected) {
        List<Card> ships = getCards(Card.Type.SHIP); //wszystkie typu SHIP
        List<Card> result = new ArrayList<>();
        for (Card card : ships) {
            if (isCollectingThisShip(card) && collected) {
                result.add(card);
            } else if (!isCollectingThisShip(card) && !collected) {
                result.add(card);
            }
        }
        return result; // zwraca jeden result
    }

    public void addShipCard(Card card) {
        setIfFirstCollected(card);
        ownStack.add(card);
    }

    public Card getSelectedShipCard(String givenType) {
        List<Card> ships = getCards(Card.Type.SHIP);
        Card selected = null;
        if (!ships.isEmpty()) {
            for (Card ship : ships) {
                if (ship.getSecondShipType().equals(givenType)) {
                    selected = ship;
                }
            }
        } else {
            System.out.println("Player has no requested card");
        }
        return selected;
    }

    public void setIfFirstCollected(Card card) {
        if (collectedShipType == null) { // collected u tego playera, a u innych?
            collectedShipType = card.getSecondShipType();
        }
    }

    public boolean checkIfLastShipCard() {
        return getShipsCollected(true).size() == 6;
    }

    public boolean chceckIfMoreThan3() {
        boolean more = false;
        int val = 0;
        for (Card card : ownStack) {
            val = val + card.getValue();
        }
        if (val > 3) {
            more = true;
        }
        return more;
    }

    public boolean isCollectingThisShip(Card card) {
        boolean isCollecting = false;
        if ((collectedShipType != null) && (card.getType() == Card.Type.SHIP && card.getSecondShipType().equals(collectedShipType))) {
            isCollecting = true;
        }
        return isCollecting;
    }

    public int checkNumberOfMissingShipCards() {
        return 6 - getShipsCollected(true).size();
    }

    public void showOwnStack() {
        System.out.println("Stack - player " + getPlayerNum() + ":");
        for (Card card : ownStack) {
            System.out.println(card);
        }
    }

    public boolean hasCards() {
        return ownStack.size() > 0;
    }

    public boolean stillPlaying(boolean stillPlaying) {
        return stillPlaying;
    }

    public boolean isStillPlaying() { // geter do metody stillPlaying
        return stillPlaying;
    }

    public int getPlayerNum() {
        return playerNum;
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
                "collectedShipType='" + collectedShipType + '\'' +
                ", stillPlaying=" + stillPlaying +
                ", playerNum=" + playerNum +
                '}';
    }
}
