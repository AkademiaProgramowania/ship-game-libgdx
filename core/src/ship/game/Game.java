package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game{

    private CardFactory factory = new CardFactory();
    private List<Card> allCards;
    private List<Card> mainStack = new ArrayList<>(); // stos główny
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    public Game() {
        allCards = factory.createCards(); // createCards() zamiast factory.getallCards żeby za każdym razem robić nowe karty
    }

    public List<Card> shuffle() {
        List<Card> shuffled = allCards;
        Collections.shuffle(shuffled);
        return shuffled;
    }
    public List<Card> getAllCards() {
        return allCards;
    }

    public Card findCardByTypeAndInt(String type, int num) {
        for (Card card : allCards) {
            if(card.getType().equals(type) && card.getNum() == num){
                return card;
            }
        }
        return null;
    }

    public Card findCardByType(String type) { // metoda wynajdzie pierwszą kartę danego typu
        for (Card card : allCards) {
            if(card.getType().equals(type)){
                return card;
            }
        }
        return null;
    }

    public List<Card> getMainStack() {
        return mainStack;
    }

    public List<Card> getTemporaryStack() {
        return temporaryStack;
    }
}
