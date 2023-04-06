package ship.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    int shipCardQuantity = 24;
    int stormCardQuantity = 8;
    int coinCardQuantity = 20;
    int cannonCardQuantity = 3;

    private final List<Card> shipCards = new ArrayList<>(shipCardQuantity); // 4 ship types consisting of 6 pcs, 1-3 top row, 4-6 bottom row
    private final List<Card> stormCards = new ArrayList<>(stormCardQuantity); // 8 pcs
    private final List<Card> coinCards = new ArrayList<>(coinCardQuantity); // 20 pcs
    private final List<Card> cannonCards = new ArrayList<>(cannonCardQuantity); // 3 pcs

    public Game() {
        createListOfShips();
        createListOfStormCards();
        createListOfCoinCards();
        createListOfCannonCards();
    }

    private void createListOfShips() {
        shipCards.add(new Card("S1", 1));
        shipCards.add(new Card("S1", 2));
        shipCards.add(new Card("S1", 3));
        shipCards.add(new Card("S1", 4));
        shipCards.add(new Card("S1", 5));
        shipCards.add(new Card("S1", 6));
        shipCards.add(new Card("S2", 1));
        shipCards.add(new Card("S2", 2));
        shipCards.add(new Card("S2", 3));
        shipCards.add(new Card("S2", 4));
        shipCards.add(new Card("S2", 5));
        shipCards.add(new Card("S2", 6));
        shipCards.add(new Card("S3", 1));
        shipCards.add(new Card("S3", 2));
        shipCards.add(new Card("S3", 3));
        shipCards.add(new Card("S3", 4));
        shipCards.add(new Card("S3", 5));
        shipCards.add(new Card("S3", 6));
        shipCards.add(new Card("S4", 1));
        shipCards.add(new Card("S4", 2));
        shipCards.add(new Card("S4", 3));
        shipCards.add(new Card("S4", 4));
        shipCards.add(new Card("S4", 5));
        shipCards.add(new Card("S4", 6));
    }

    public void createListOfStormCards() {
        int index = 1;
        for (int i = 0; i < stormCardQuantity; i++) {
            stormCards.add(new Card("storm", index++));
        }
    }

    public void createListOfCoinCards() {
        int index = 1;
        for (int i = 0; i < coinCardQuantity; i++) {
            stormCards.add(new Card("coin", index++));
        }
    }

    public void createListOfCannonCards() {
        int index = 1;
        for (int i = 0; i < cannonCardQuantity; i++) {
            stormCards.add(new Card("cannon", index++));
        }
    }

    public List<Card> getAllCards() {

        List<Card> allCards = new ArrayList<>();
        allCards.addAll(getShipCards());
        allCards.addAll(getStormCards());
        allCards.addAll(getCoinCards());
        allCards.addAll(getCannonCards());
        return allCards;
    }

    public List<Card> shuffle(){
        // jak nadać randomowy index pozycjom w zbiorze all
        // jakaś funkcja typu push w mapach?

        List<Card> all = getAllCards();
        Random random = new Random();
        int arrayIndex = random.nextInt(all.size());

        for (int i = 0; i < all.size(); i++) {
             i = arrayIndex;
        }
        return all;
    }

    public List<Card> getShipCards() {
        return shipCards;
    }
    public List<Card> getStormCards() {
        return stormCards;
    }
    public List<Card> getCoinCards() {
        return coinCards;
    }
    public List<Card> getCannonCards() {
        return cannonCards;
    }
}
