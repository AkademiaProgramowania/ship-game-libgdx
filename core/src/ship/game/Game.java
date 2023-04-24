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
        EventBus.subscribe(EventType.STACK_SHUFFLED, this);
        EventBus.subscribe(EventType.STACK_FILLED, this);
        EventBus.subscribe(EventType.SHIP_TYPE_TO_COLLECT, this);
        EventBus.subscribe(EventType.CURRENT_PLAYER, this);
        EventBus.subscribe(EventType.STORM_CAME, this);
        EventBus.subscribe(EventType.STORM_NO_CARDS, this);
        EventBus.subscribe(EventType.SHOW_CARD, this);
        EventBus.subscribe(EventType.SHOW_CARD_RETURNED, this);
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
            mainStack = shuffle(temporaryStack);
            EventBus.notify(new Event(EventType.STACK_SHUFFLED));
        }
    }

    public List<Card> shuffle(List<Card> list) {
        Collections.shuffle(list);
        return list;
    }

    public void passCardToAPlayerIfNotStorm() {
        Card drawn = draw(); // karta jest wyciągana ze stosu i przekazywana do odp podzbioru w ownStack:

        if (drawn.getType().equals(Card.Type.COIN) || drawn.getType().equals(Card.Type.CANNON)) {
            getCurrentPlayer().addToOwnStack(drawn);
            EventBus.notify(new Event(EventType.STACK_FILLED));
        }
        if (drawn.getType().equals(Card.Type.SHIP)) {
            getCurrentPlayer().addToOwnStack(drawn);
            getCurrentPlayer().checkIfFirstShipCardAndSetCollected(drawn);
            //getCurrentPlayer().checkIfSetShipCardsAsCollected();
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            temporaryStack.add(drawn);
            EventBus.notify(new Event(EventType.STORM_CAME));
            if (getCurrentPlayer().getOwnStack().size() <= 3) {
                EventBus.notify(new Event(EventType.STORM_NO_CARDS));
                EventBus.notify(new Event(EventType.SHOW_CARD_RETURNED));
                temporaryStack.addAll(getCurrentPlayer().getOwnStack());
                switchToNextPlayer();
                return;
            }
            getCurrentPlayer().chooseCardsToReturn();
            addReturnedToTemporaryStack();
            switchToNextPlayer();
        }
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

    public int checkNumberOfMissingShipCards() {
        int num = 0;
        int requiredPieces = 6;
        List<Card> cards = getCurrentPlayer().getOwnStack();
        for (Card card : cards) {
            if (card.getSecondShipType().equals(getCurrentPlayer().getCollectedShipType())) {
            }
            num++;
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

    public List<Card> getMainStack() {
        return mainStack;
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.CARD_DRAWN) {
            System.out.println("Card drawn: " + event.getCard());
        }
        if (event.getType() == EventType.STACK_SHUFFLED) {
            System.out.println("Cards have been shuffled");
        }
        if (event.getType() == EventType.STACK_FILLED) {
            System.out.println("Stack filled with " + event.getCard());
        }
        if (event.getType() == EventType.CURRENT_PLAYER) {
            System.out.println("Current player " + getCurrentPlayer().getPlayerNum());
        }
        /*if (event.getType() == EventType.SHIP_TYPE_TO_COLLECT) {
            System.out.println(getCurrentPlayer().getPlayerNum() + " it's your ship type to collect: " + getCurrentPlayer().collectedShipType);
        }*/
        if (event.getType() == EventType.STORM_CAME) {
            System.out.println("A storm is coming. Give back 3 cards");
        }
        if (event.getType() == EventType.STORM_NO_CARDS) {
            System.out.println("To little cards on stack.");
        }
        if (event.getType() == EventType.SHOW_CARD_RETURNED) {

            System.out.println("Card given back " + event.getCard());
        }
        if (event.getType() == EventType.PLAYER_SWITCHED) {
            System.out.println("Next player turn");
        }
        if (event.getType() == EventType.CARD_PURCHASE) {
            System.out.println("Purchased " + event.getCard());
        }
    }
}
