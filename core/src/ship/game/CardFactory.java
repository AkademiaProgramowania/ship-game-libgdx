package ship.game;

import java.util.*;

public class CardFactory {

    int shipCardQuantity = 24; // zostawione do przerobienia metody na pętle
    private static final int stormCardQuantity = 8;
    private static final int coinCardQuantity = 20;
    private static final int cannonCardQuantity = 3;

    //List<Card> allCards = new ArrayList<>();

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
        ships.add(new Card("S1", 1));
        ships.add(new Card("S1", 2));
        ships.add(new Card("S1", 3));
        ships.add(new Card("S1", 4));
        ships.add(new Card("S1", 5));
        ships.add(new Card("S1", 6));
        ships.add(new Card("S2", 1));
        ships.add(new Card("S2", 2));
        ships.add(new Card("S2", 3));
        ships.add(new Card("S2", 4));
        ships.add(new Card("S2", 5));
        ships.add(new Card("S2", 6));
        ships.add(new Card("S3", 1));
        ships.add(new Card("S3", 2));
        ships.add(new Card("S3", 3));
        ships.add(new Card("S3", 4));
        ships.add(new Card("S3", 5));
        ships.add(new Card("S3", 6));
        ships.add(new Card("S4", 1));
        ships.add(new Card("S4", 2));
        ships.add(new Card("S4", 3));
        ships.add(new Card("S4", 4));
        ships.add(new Card("S4", 5));
        ships.add(new Card("S4", 6));
        return ships;
    }

    public List<Card> createListOfStormCards() {
        List<Card> storms = new ArrayList<>();
        for (int i = 0; i < stormCardQuantity; i++) {
            storms.add(new Card("storm", i + 1)); // i +1 zamiast inicjalizować int index i w pętli index ++
        }
        return storms;
    }

    public List<Card> createListOfCoinCards() {
        List<Card> coins = new ArrayList<>();
        for (int i = 0; i < coinCardQuantity; i++) {
            coins.add(new Card("coin", i + 1));
        }
        return coins;
    }

    public List<Card> createListOfCannonCards() {
        List<Card> cannons = new ArrayList<>();
        for (int i = 0; i < cannonCardQuantity; i++) {
            cannons.add(new Card("cannon", i + 1));
        }
        return cannons;
    }
}

