package ship.game;

import java.util.*;

public class CardFactory {

    int shipCardQuantity = 24; // zostawione do przerobienia metody na pętle
    private static final int stormCardQuantity = 8;
    private static final int coinCardQuantity = 20;
    private static final int cannonCardQuantity = 3;

    public List<Card> createCards(){
        List<Card> cards = new ArrayList<>();
        cards.addAll(createListOfShips());
        cards.addAll(createListOfStormCards());
        cards.addAll(createListOfCoinCards());
        cards.addAll(createListOfCannonCards());
        return cards;
    }

    // nie powielać przechowywania obiektów - niepotrzebne przechowywanie w osobnych listach,
    // zwracać metodami listy i dodawać do all od razu.

    private List<Card> createListOfShips() {
        List<Card> ships = new ArrayList<>();
        // redfactor, every ship - color should be initialized by a loop
        ships.add(new Card(Card.Type.SHIP,"S1", 1, 1));
        ships.add(new Card(Card.Type.SHIP,"S1", 2, 1));
        ships.add(new Card(Card.Type.SHIP,"S1", 3, 1));
        ships.add(new Card(Card.Type.SHIP,"S1", 4, 1));
        ships.add(new Card(Card.Type.SHIP,"S1", 5, 1));
        ships.add(new Card(Card.Type.SHIP,"S1", 6, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 1, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 2, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 3, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 4, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 5, 1));
        ships.add(new Card(Card.Type.SHIP,"S2", 6, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 1, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 2, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 3, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 4, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 5, 1));
        ships.add(new Card(Card.Type.SHIP,"S3", 6, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 1, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 2, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 3, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 4, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 5, 1));
        ships.add(new Card(Card.Type.SHIP,"S4", 6, 1));
        return ships;
    }

    public List<Card> createListOfStormCards() {
        List<Card> storms = new ArrayList<>();
        for (int i = 0; i < stormCardQuantity; i++) {
            storms.add(new Card(Card.Type.STORM, i + 1, 0)); // i +1 zamiast inicjalizować int index i w pętli index ++
        }
        return storms;
    }

    public List<Card> createListOfCoinCards() {
        List<Card> coins = new ArrayList<>();
        for (int i = 0; i < coinCardQuantity; i++) {
            coins.add(new Card(Card.Type.COIN, i + 1, 1));
        }
        return coins;
    }

    public List<Card> createListOfCannonCards() {
        List<Card> cannons = new ArrayList<>();
        for (int i = 0; i < cannonCardQuantity; i++) {
            cannons.add(new Card(Card.Type.CANNON, i + 1, 3));
        }
        return cannons;
    }
}

