package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements EventListener {

    private List<Card> mainStack = new ArrayList<>(); // stos główny
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    private int currentPlayerIndex = 0;

    public Game() {
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
        EventBus.subscribe(EventType.CARD_DRAWN, this);
        EventBus.subscribe(EventType.CURRENT_PLAYER, this);
        EventBus.subscribe(EventType.STORM_CAME, this);
        EventBus.subscribe(EventType.SHOW_STACK, this);
        EventBus.subscribe(EventType.PLAYER_SWITCHED, this);
        EventBus.subscribe(EventType.CARD_PURCHASE, this);
        EventBus.subscribe(EventType.SHIP_COLLECTED, this); // zebrane 6 części statku - dopisać
        EventBus.subscribe(EventType.GAME_END, this); // koniec gry - dopisać
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Card draw() {
        checkIfMainStackIsOut();
        Card drawn = mainStack.get(0);
        mainStack.remove(0);
        EventBus.notify(new Event(EventType.CARD_DRAWN, drawn));
        return drawn;
    }

    public void checkIfMainStackIsOut() {
        if (mainStack.isEmpty()) {
            shuffle();
        }
    }

    public void shuffle() {
        List<Card> shuffled = new ArrayList<>(temporaryStack);
        Collections.shuffle(shuffled);
        mainStack = shuffled;
    }

    public void passCardToAPlayerIfNotStorm() {
        Card drawn = draw(); // karta jest wyciągana ze stosu i przekazywana do odp podzbioru w ownStack:

        if (drawn.getType().equals(Card.Type.COIN) || drawn.getType().equals(Card.Type.CANNON)) {
            getCurrentPlayer().addToOwnStack(drawn);
            EventBus.notify(new Event(EventType.CURRENT_PLAYER));
        }
        if (drawn.getType().equals(Card.Type.SHIP)) {
            getCurrentPlayer().addToOwnStack(drawn);
            getCurrentPlayer().checkIfFirstShipCardAndSetCollected(drawn);
            getCurrentPlayer().checkIfSetShipCardsAsCollected();
            EventBus.notify(new Event(EventType.CURRENT_PLAYER));
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            EventBus.notify(new Event(EventType.STORM_CAME)); // kod zależny od STORM_CAME przenosimy do react?
            getCurrentPlayer().chooseCardsToReturn();
            addReturnedToTemporaryStack();
            switchToNextPlayer();
            EventBus.notify(new Event(EventType.CURRENT_PLAYER));
        }
        EventBus.notify(new Event(EventType.SHOW_STACK));
        getCurrentPlayer().showOwnStack();
    }

    public void addReturnedToTemporaryStack() {
        temporaryStack.addAll(getCurrentPlayer().chooseCardsToReturn());
    }

    public void switchToNextPlayer() {
        EventBus.notify(new Event(EventType.PLAYER_SWITCHED));
        if (currentPlayerIndex == players.size() - 1) { // ostatni gracz -1: arrayList liczony od zera, int od 1
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
    }

    public void buyShipCard(int playerIndex, String requestedType) {
        Card purchased = getPlayer(playerIndex).giveRequestedShipCard(requestedType);
        getCurrentPlayer().addToOwnStack(purchased);
        int num = 0;
        while (num <= 3) {
            Card coin = getCurrentPlayer().giveCoinCard();
            getPlayer(playerIndex).addToOwnStack(coin);
            num++;
        }
        EventBus.notify(new Event(EventType.CARD_PURCHASE, purchased));
    }

    public int giveNumberOfMissingShipCards() {
        List<Card> cards = getCurrentPlayer().getOwnStack();
        int num = 0;
        int requiredPieces = 6;
        for (Card card : cards) {
            if (card.getSecondShipType().equals(getCurrentPlayer().getCollectedShipType())) {
                num++;
            }
        }
        return requiredPieces - num;
    }

    public void endTurn() {
        switchToNextPlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayer(int requiredIndex) {
        return players.get(requiredIndex);
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.CARD_DRAWN) {
            System.out.println("Card drawn " + event.getCard());
        }
        if (event.getType() == EventType.CURRENT_PLAYER) {
            System.out.println("Current player " + getCurrentPlayer());
        }
        if (event.getType() == EventType.STORM_CAME) {
            System.out.println("A storm is coming. Give back 3 cards to avoid damages!");
        }
        if (event.getType() == EventType.SHOW_STACK) {
            System.out.println("Show stack");
        }
        if (event.getType() == EventType.PLAYER_SWITCHED) {
            System.out.println("Next player turn");
        }
        if (event.getType() == EventType.CARD_PURCHASE) {
            System.out.println("Purchased " + event.getCard());
        }
    }
}
