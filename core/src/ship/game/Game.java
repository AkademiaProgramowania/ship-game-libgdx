package ship.game;

import com.badlogic.gdx.utils.compression.lz.BinTree;
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

    private List<Card> toReturn = new ArrayList<>();

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    private int currentPlayerIndex = 0;

    public Game() {
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
        EventBus.subscribe(EventType.GAME_START, this);
        EventBus.subscribe(EventType.DRAW_CARD_DECISION, this);
        EventBus.subscribe(EventType.CLICK_ON_COIN, this);
        EventBus.subscribe(EventType.CLICK_ON_CANNON, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP_COLLECTED, this);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void gameStart() { // setowanie playera w evencie
        // event wyciągnięty do zmiennej by móc to zrobić
        shuffle(getMainStack());
        Event event = new Event(EventType.TURN_START);
        event.setPlayer(getCurrentPlayer());
        EventBus.notify(event);
    }

    public Card draw() {
        checkIfMainStackIsOut();
        Card drawn = mainStack.get(0);
        mainStack.remove(0);
        return drawn;
    }

    public void checkIfMainStackIsOut() {
        if (mainStack.isEmpty()) {
            mainStack = shuffle(temporaryStack);
            System.out.println("Stack shuffled");
            // EventBus.notify(new Event(EventType.STACK_SHUFFLED));
        }
    }

    public List<Card> shuffle(List<Card> list) {
        Collections.shuffle(list);
        return list;
    }

    public void drawAndAssign() {
        Card drawn = draw();
        System.out.println("Drawn: " + drawn);

        if (drawn.getType().equals(Card.Type.SHIP)) {
            getCurrentPlayer().checkIfFirstShipCardAndSetCollected(drawn);
            Event collected = new Event(EventType.SHIP_TYPE_TO_COLLECT);
            collected.setPlayer(getCurrentPlayer());
            EventBus.notify(collected);
        }
        if (!drawn.getType().equals(Card.Type.STORM)) {
            getCurrentPlayer().addToOwnStack(drawn);
        } else { // gdy drawn == STORM
            temporaryStack.add(drawn);
            System.out.println("Temporary stack: " + temporaryStack.toString());
            if (toReturn.isEmpty()) {
                switchToNextPlayer();
            } else { // jeżeli toReturn nie jest empty
                System.out.println("Spr co jest w toReturn" + toReturn);
                countCardsToReturn(); // wywołuje doStorm() w controllerze
                temporaryStack.addAll(toReturn);
            }
        }
        if (getCurrentPlayer().checkIfLastShipCard()) {
            Event endGame = new Event(EventType.GAME_END);
            endGame.setPlayer(getCurrentPlayer());
            EventBus.notify(endGame);
        }

        Event drawCardEvent = new Event(EventType.DRAW_CARD);
        drawCardEvent.setCard(drawn);
        drawCardEvent.setPlayer(getCurrentPlayer());
        EventBus.notify(drawCardEvent);
    }

  /*  public void passCardToAPlayerIfNotStorm() {
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
    }*/

/*    public void addReturnedToTemporaryStack() {
        temporaryStack.addAll(getCurrentPlayer().chooseCardsToReturn());
    }*/

    public void switchToNextPlayer() {
        if (currentPlayerIndex == players.size() - 1) { // ostatni gracz -1: arrayList liczony od zera, int od 1
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer());
        EventBus.notify(event);
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
        Event event = new Event(EventType.CARD_PURCHASE);
        event.setCard(purchased);
        EventBus.notify(event);
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

    public void countCardsToReturn() {
        // licz wartość zwróconych kart i dopóki nie będzie 3,
        // wysyłaj event do controllera
        System.out.println("wywolana metoda count cards");
        int sumValue = 0;
        while (sumValue < 3)
            for (Card card : toReturn) {
                int value = card.getValue();
                sumValue = sumValue + value;
                System.out.println(sumValue);

                Event doStorm = new Event(EventType.DO_STORM);
                doStorm.setPlayer(getCurrentPlayer());
                EventBus.notify(doStorm);
            }
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
        if (event.getType() == EventType.GAME_START) {
            gameStart();
        }
        if (event.getType() == EventType.DRAW_CARD_DECISION) {
            drawAndAssign();
        }

        if (event.getType() == EventType.CLICK_ON_COIN) {
            Card card = event.getPlayer().findCoinToReturn();
            toReturn.add(card);
            // czy kartę z ownStack ma usuwac player (obecnie) czy game?
        }
        if (event.getType() == EventType.CLICK_ON_CANNON) {
            Card card = event.getPlayer().findCannonToReturn();
            toReturn.add(card);
        }
        if (event.getType() == EventType.CLICK_ON_SHIP) {
            Card card = event.getPlayer().findShipToReturn();
            toReturn.add(card);
        }
        if (event.getType() == EventType.CLICK_ON_SHIP_COLLECTED) {
            Card card = event.getPlayer().findCollectedShipToReturn();
            toReturn.add(card);
        }
    }
}
