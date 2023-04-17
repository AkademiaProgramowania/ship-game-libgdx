package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> allCards;
    private List<Card> mainStack = new ArrayList<>(); // stos główny
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    public Game(List<Player> players) { // lista playerów żeby można było po nich iterować później
        CardFactory factory = new CardFactory();
        allCards = factory.createCards(); // createCards() zamiast factory.getallCards żeby za każdym razem robić nowe karty
        this.players = players;
    }

    public Game() {
        CardFactory factory = new CardFactory();
        allCards = factory.createCards();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Card findCardByTypeAndInt(Card.Type type, int num) {
        for (Card card : allCards) {
            if (card.getType().equals(type) && card.getNum() == num) {
                return card;
            }
        }
        return null;
    }

    public List<Card> getAllCards() {
        return allCards;
    }

    public List<Card> getMainStack() {
        return mainStack;
    }

    public List<Card> getTemporaryStack() {
        return temporaryStack;
    }
}
