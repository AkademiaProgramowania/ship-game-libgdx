package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game{

    private final List<Card> allCards;
    private final List<Card> mainStack = new ArrayList<>(); // stos główny
    private final List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie
    private final List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    public Game() {
        CardFactory factory = new CardFactory();
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

    public Card findCardByTypeAndInt(Card.Type type, int num) {
        for (Card card : allCards) {
            if(card.getType().equals(type) && card.getNum() == num){
                return card;
            }
        }
        return null;
    }

    public Card findCardByTypeInOwnStack(Card.Type type) { // metoda wynajdzie pierwszą kartę danego typu
        for (Card card : ownStack) {
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

    public List<Card> getOwnStack() {
        return ownStack;
    }
}
