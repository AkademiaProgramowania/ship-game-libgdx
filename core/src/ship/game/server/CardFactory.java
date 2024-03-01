package ship.game.server;

import java.util.*;

public class CardFactory {

    int shipS1Quantity = 6; // (1 - 6)
    int shipS2Quantity = 6; // (7 - 12)
    int shipS3Quantity = 6; // (13 - 18)
    int shipS4Quantity = 6; // (19 - 24)


    private static final int stormCardQuantity = 8; // (25 - 32)
    private static final int coinCardQuantity = 20; // (33 - 52)
    private static final int cannonCardQuantity = 3; // (53 - 55)

    public List<Card> createCards() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(createListOfShips());
        cards.addAll(createListOfStormCards());
        cards.addAll(createListOfCoinCards());
        cards.addAll(createListOfCannonCards());
        return cards;
    }

    private List<Card> createListOfShips() {
        List<Card> ships = new ArrayList<>();
        for (int i = 0; i < shipS1Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S1", i + 1, 1));
        }
        for (int i = 0; i < shipS2Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S2", i + 1, 1));
        }
        for (int i = 0; i < shipS3Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S3", i + 1, 1));
        }
        for (int i = 0; i < shipS4Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S4", i + 1, 1));
        }
        return ships;
    }

    public List<Card> createListOfStormCards() {
        List<Card> storms = new ArrayList<>();
        for (int i = 0; i < stormCardQuantity; i++) {
            storms.add(new Card(Card.Type.STORM, 0, 0));
        }
        return storms;
    }

    public List<Card> createListOfCoinCards() {
        List<Card> coins = new ArrayList<>();
        for (int i = 0; i < coinCardQuantity; i++) {
            coins.add(new Card(Card.Type.COIN, 0, 1));
        }
        return coins;
    }

    public List<Card> createListOfCannonCards() {
        List<Card> cannons = new ArrayList<>();
        for (int i = 0; i < cannonCardQuantity; i++) {
            cannons.add(new Card(Card.Type.CANNON, 0, 3));
        }
        return cannons;
    }
}

