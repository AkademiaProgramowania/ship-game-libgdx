package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Card> mainStack = new ArrayList<>(); // stos główny
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    private int currentPlayerIndex = 0;

    public Game(List<Player> players) { // lista playerów żeby można było po nich iterować
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards(); // createCards() zamiast factory.getallCards żeby za każdym stworzeniem nowej Game robić nowe karty
        this.players = players;
    }

    public Game() {
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void switchToNextPlayer() {
        EventBus.notify(new Event(EventType.PLAYER_SWITCHED));
        if (currentPlayerIndex == players.size() - 1) { // ostatni gracz -1 bo index liczony od zera
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
    }

    public void passToAPlayerIfNotStorm() {
        Card drawn = draw(); // karta jest wyciągana ze stosu i przekazywana do odp podzbioru w ownStack:

        if (drawn.getType().equals(Card.Type.COIN) || drawn.getType().equals(Card.Type.CANNON)) {
            getCurrentPlayer().addToOwnStack(drawn); // do ownStack
            System.out.println("Curent players turn: " + getCurrentPlayer());
        }
        if (drawn.getType().equals(Card.Type.SHIP)) { // wszystkie karty ship najpierw idą do ownStack
            getCurrentPlayer().checkIfSetToCollected(drawn); // ustawia kartę na collected
            getCurrentPlayer().addToOwnStack(drawn);
            System.out.println("Curent players turn: " + getCurrentPlayer());
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            EventBus.notify(new Event(EventType.STORM_CAME));
            //chooseWhichToReturn(); gdzie umieścić metodę żeby mieć dostęp?
            switchToNextPlayer();
        }
        System.out.println("My stack");
        //showOwnStack();
        // czy decyzja o kontynuacji po każdym ifie?
    }

    public void pass() {
        switchToNextPlayer();
    }

    public Card draw() {
        Card drawn = mainStack.get(0);
        mainStack.remove(0);
        EventBus.notify(new Event(EventType.CARD_DRAWN, drawn));
        return drawn;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Card> getMainStack() {
        return mainStack;
    }

    public List<Card> getTemporaryStack() {
        return temporaryStack;
    }
}
