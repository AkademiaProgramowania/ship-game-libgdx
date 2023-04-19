package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> mainStack = new ArrayList<>(); // stos główny
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    private int currentPlayerIndex = 0;

    public Game() {
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
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

    public void checkIfMainStackIsOut(){
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
            getCurrentPlayer().addToOwnStack(drawn); // do ownStack
            EventBus.notify(new Event(EventType.CURRENT_PLAYER));
        }
        if (drawn.getType().equals(Card.Type.SHIP)) {
            getCurrentPlayer().addToOwnStack(drawn);
            getCurrentPlayer().checkIfFirstShipCardAndSetCollected(drawn);
            getCurrentPlayer().checkIfSetShipCardsAsCollected();
            EventBus.notify(new Event(EventType.CURRENT_PLAYER));
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            EventBus.notify(new Event(EventType.STORM_CAME));
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
        EventBus.notify(new Event(EventType.CARD_PURCHASE));
    }

    public int giveNumberOfMissingShipCards(){
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
}
