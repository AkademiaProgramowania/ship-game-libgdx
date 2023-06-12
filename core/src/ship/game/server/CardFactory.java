package ship.game.server;

import java.util.*;

public class CardFactory {

    int shipS1Quantity = 6;
    int shipS2Quantity = 6;
    int shipS3Quantity = 6;
    int shipS4Quantity = 6;


    private static final int stormCardQuantity = 8;
    private static final int coinCardQuantity = 20;
    private static final int cannonCardQuantity = 3;

    public List<Card> createCards(){
        List<Card> cards = new ArrayList<>();
        cards.addAll(createListOfShips());
        cards.addAll(createListOfStormCards());
        cards.addAll(createListOfCoinCards());
        cards.addAll(createListOfCannonCards());
        // spr czy w bazie danych są karty, jeśli nie ma to przyg. karty
        // każda karta z ArrayList jako insert do bazy danych
        // ver1 - ze statement
        // ver2 -  z preprare statement to lepiej
        // metoda nic nie zwraca
        return cards;
    }

    public List<Card> getCardsFromDatabase() {
        // select* from cards
        // wynik - ResultSet
        // przeglądanie ResultSet
        // wciągane informacji z każdej z kolumn, tworzenie obiektu Card i dodawanie do listy
        return null;
    }
    // next - relacje tabeli

    private List<Card> createListOfShips() {
        List<Card> ships = new ArrayList<>();
        for (int i = 0; i < shipS1Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S1", i + 1, 1));
        }
        for (int i = 0; i < shipS2Quantity ; i++) {
            ships.add(new Card(Card.Type.SHIP, "S2", i + 1, 1));
        }
        for (int i = 0; i < shipS3Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S3", i + 1, 1));
        }
        for (int i = 0; i < shipS4Quantity; i++) {
            ships.add(new Card(Card.Type.SHIP, "S4", i + 1, 1));
        }
/*        ships.add(new Card(Card.Type.SHIP,"S1", 1, 1));
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
        ships.add(new Card(Card.Type.SHIP,"S4", 6, 1));*/
        return ships;
    }

    public List<Card> createListOfStormCards() {
        List<Card> storms = new ArrayList<>();
        for (int i = 0; i < stormCardQuantity; i++) {
            storms.add(new Card(Card.Type.STORM, i + 1, 0));
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

