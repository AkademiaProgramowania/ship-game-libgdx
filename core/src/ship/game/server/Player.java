package ship.game.server;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String collectedShipType;
    private boolean stillPlaying = true;

    private int playerIndex;

    private List<Card> ownStack = new ArrayList<>();

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
    }


    public void addCard(Card card) {
        ownStack.add(card);
    }

    public void addAll(List<Card> list) {
        ownStack.addAll(list);
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

    public List<Card> get3CoinsToPay() {
        List<Card> coins = getCards(Card.Type.COIN);
        List<Card> cardsToReturn = new ArrayList<>();
        if (coins.size() >= 3) {
            cardsToReturn.add(coins.get(0));
            cardsToReturn.add(coins.get(1));
            cardsToReturn.add(coins.get(2));
            ownStack.removeAll(cardsToReturn);
        } else {
            System.out.println("Not enough coins!");
        }
        System.out.println("Cards to return:" + cardsToReturn);
        return cardsToReturn;
    }

    public List<Card> getShipsCollected(boolean collected) { // todo refactor selector arguments
        // https://rules.sonarsource.com/java/RSPEC-2301
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

    public void setAsCollectedMostPopularType() {
        List<Card> biggestNotCollectedShipsList = getBiggestNotCollectedShipsList();
        if (biggestNotCollectedShipsList == null || biggestNotCollectedShipsList.isEmpty()) {
            collectedShipType = null;
        } else {
            collectedShipType = biggestNotCollectedShipsList.get(0).getSecondShipType();
        }
    }

    private List<Card> getBiggestNotCollectedShipsList() {
        List<Card> shipsNotCollected = getShipsCollected(false); // statki nie zbierane
        String[] types = {"S1", "S2", "S3", "S4"};
        List<Card> biggestList = null;
        for (String type : types) { //dla każdego typu
            List<Card> shipsOfGivenType = getByCollectedShipType(shipsNotCollected, type);  // wszystkie danego typu
            if (biggestList == null) { // jeśli nie ma największej listy
                biggestList = shipsOfGivenType; // to są to danego typu
            } else if (biggestList.size() < shipsOfGivenType.size()) { // jeśli rozmiar zbioru danego typu jest większy niż biggest
                biggestList = shipsOfGivenType; // to rozmiar danego typu jest teraz największy
            }
        }
        return biggestList;
    }

    private List<Card> getByCollectedShipType(List<Card> ships, String type) {
        List<Card> result = new ArrayList<>();
        for (Card card : ships) {
            if (card.getSecondShipType().equals(type)) {
                result.add(card);
            }
        }
        return result;
    }

    public Card getSelectedShipCard(String givenType) {
        List<Card> ships = getCards(Card.Type.SHIP);
        Card selected = null;
        if (!ships.isEmpty()) {
            for (Card ship : ships) { // logic can be replaced by Stream and Optional statement
                // https://www.baeldung.com/java-stream-findfirst-vs-findany - return Optional of given type
                // https://www.baeldung.com/java-optional
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
        return ((collectedShipType != null) && (card.getType() == Card.Type.SHIP && card.getSecondShipType().equals(collectedShipType)));
    }

    public int checkNumberOfMissingShipCards() {
        return 6 - getShipsCollected(true).size();
    }

    public void showOwnStack() {
        System.out.println("Stack - gracz " + playerIndex + ":");
        for (Card card : ownStack) {
            System.out.println(card);
        }
    }

    public boolean hasCards() {
        return ownStack.size() > 0;
    }

    public void setStillPlaying(boolean stillPlaying) {
        this.stillPlaying = stillPlaying;
    }

    public boolean isStillPlaying() {
        return stillPlaying;
    }

    public String getPlayingStatus() {
        String status = "";
        if (stillPlaying) {
            status = "T";
        } else {
            status = "F";
        }
        return status;
    }

    public void setPlayingStatus(String status) {
        if (status.equals("T")) {
            stillPlaying = true;
        }
        if (status.equals("F"))
            stillPlaying = false;
    }

    public int getStackSize() {
        return ownStack.size();
    }

    public int getPlayerIndex() {
        return playerIndex;
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
        return "Player {" +
                "playerIndex=" + playerIndex +
                ", collectedShipType='" + collectedShipType + '\'' +
                ", ownStack=" + ownStack.size() +
                '}';
    }
}
