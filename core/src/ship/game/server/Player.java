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
        for (Card card : ships) {
            if (isCollectingThisShip(card) && collected) {
                result.add(card);
            } else if (!isCollectingThisShip(card) && !collected) {
                result.add(card);
            }
        }
        return result;
    }

/*    public void addShipCard(Card card) {
        setIfFirstCollected(card);
        ownStack.add(card);
    }*/

    public void setAsCollectedMostPopularType() {
        List<Card> biggestNotCollectedShipsList = getBiggestNotCollectedShipsList();
        if (biggestNotCollectedShipsList == null || biggestNotCollectedShipsList.isEmpty()) {
            collectedShipType = null;
        } else {
            collectedShipType = biggestNotCollectedShipsList.get(0).getSecondShipType();
        }
    }

    private List<Card> getBiggestNotCollectedShipsList() {
        List<Card> shipsCollected = getShipsCollected(false);
        String[] types = {"S1", "S2", "S3", "S4"};
        List<Card> biggestList = null;
        for (String type : types) {
            List<Card> shipsInThisType = getByCollectedShipType(shipsCollected, type);
            if (biggestList == null) {
                biggestList = shipsInThisType;
            } else if (biggestList.size() < shipsInThisType.size()) {
                biggestList = shipsInThisType;
            }
        }
        return biggestList;
    }

    private List<Card> getByCollectedShipType(List<Card> ships, String type){
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

    public void setCollected(Card card) {
        if (collectedShipType == null) {
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
            val = val + card.getStormValue();
        }
        if (val > 3) {
            more = true;
        }
        return more;
    }

    public boolean isCollectingThisShip(Card card) {
        boolean isCollecting = false;
        if ((collectedShipType != null) && (card.getType() == Card.Type.SHIP && card.getSecondShipType().equals(collectedShipType))) { // TODO just return the condition
            isCollecting = true;
        }
        return isCollecting;
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

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
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
