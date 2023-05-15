package ship.game.server;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String collectedShipType; //todo private + gettery
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
        for (Card ship : ships) {
            if (ship.getSecondShipType().equals(givenType)) {
                return ship;
            }
        }
        return null;
    }

    private void setIfFirstCollected(Card card) {
        if (collectedShipType == null) {
            collectedShipType = card.getSecondShipType();
        }
    }

    public boolean checkIfLastShipCard() {
        return  getShipsCollected(true).size() == 6;
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

/*    public void removeIfPresent(Card card) { // spr co powórnywać. Typ i numer (coin, cannon) lub typ, drugi typ i numer (ship)
        for (Card card1 : ownStack) {
            if ((card1.getType().equals(card.getType()) && (card1.getNum() == card.getNum())) ||
                    (card1.getType().equals(card.getType()) && card1.getSecondShipType().equals(card.getSecondShipType()) &&
                            (card1.getNum()) == card.getNum())) {
                ownStack.remove(card1);
            }
        }
    }*/

    public boolean stillPlaying(boolean stillPlaying) {
        return stillPlaying;
    }

    public boolean isStillPlaying() { // geter do metody stillPlaying
        return stillPlaying;
    }

    public int getPlayerNum() {
        return playerNum;
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
