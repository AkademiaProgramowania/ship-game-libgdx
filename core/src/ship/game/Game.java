package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements EventListener {

    private List<Card> mainStack = new ArrayList<>();
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

    private List<Card> toReturn = new ArrayList<>(); // jest w game, bo to zawsze tymczasowy zbiór, niezwiązany z playerem

    private List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze

    private int currentPlayerIndex = 0;
    private int valueToReturnIfStorm = 3;

    public Game() {
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
        EventBus.subscribe(EventType.GAME_START, this);
        EventBus.subscribe(EventType.DRAW_CARD_DECISION, this);
        EventBus.subscribe(EventType.CLICK_ON_COIN, this);
        EventBus.subscribe(EventType.CLICK_ON_CANNON, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP_COLLECTED, this);
        EventBus.subscribe(EventType.CARD_PURCHASE_DECISION, this);

    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void gameStart() { // setowanie playera w evencie
        // event wyciągnięty do zmiennej by móc to zrobić
        temporaryStack.clear(); //todo raczej nie potrzebne
        shuffle(mainStack);
        Event event = new Event(EventType.TURN_START);
        event.setPlayer(getCurrentPlayer());
        EventBus.notify(event);
    }

    public Card draw() {
        checkIfMainStackIsOutAndShuffle();
        Card drawn = mainStack.get(0);
        mainStack.remove(0);
        return drawn;
    }

    public void checkIfMainStackIsOutAndShuffle() {
        if (mainStack.isEmpty()) {
            mainStack = shuffle(temporaryStack);
            System.out.println("Stack shuffled");
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
            getCurrentPlayer().addShipCard(drawn);

            if (getCurrentPlayer().checkIfLastShipCard()) {
                Event endGame = new Event(EventType.GAME_END);
                endGame.setPlayer(getCurrentPlayer());
                EventBus.notify(endGame);
            }
        }
        if (drawn.getType().equals(Card.Type.COIN) || drawn.getType().equals(Card.Type.CANNON)) {
            getCurrentPlayer().addCard(drawn);
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            temporaryStack.add(drawn);
        }

        Event drawCardEvent = new Event(EventType.DRAW_CARD);
        drawCardEvent.setCard(drawn);
        drawCardEvent.setPlayer(getCurrentPlayer());
        EventBus.notify(drawCardEvent);

        getCurrentPlayer().showOwnStack(); //to debug
    }

    public void switchToNextPlayer() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer()); // kolejny player = current z kodu powyżej
        EventBus.notify(event);
        getCurrentPlayer().stillPlaying(true); // kolejny!

    }

    public void buyShipCard() {
        if (getCurrentPlayer().getCards(Card.Type.COIN).size() >= 3) {
            // wskazana w controllerze karta idzie na stos statku do kolekcjonowania
            // póki co nie piszę tu kodu
        }
/*        int playerIndex =
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
        EventBus.notify(event);*/
    }

/*    public void selectCardsToReturn() {
        Event selectCards = new Event(EventType.SELECT_CARDS_TO_RETURN);
        selectCards.setPlayer(getCurrentPlayer());
        EventBus.notify(selectCards);
        System.out.println("Selected: " + getToReturn().toString() + " total value: " + getToReturnValue());

    }*/

   /* public boolean checkiIfLessThan3AndReturn() {
        int allCardsValue = 0;
        boolean less = false;
        List<Card> all = new ArrayList<>();
        all.addAll(getCurrentPlayer().cannons);
        all.addAll(getCurrentPlayer().coins);
        all.addAll(getCurrentPlayer().shipsToReturn);
        all.addAll(getCurrentPlayer().shipsCollected);
        for (Card card : all) {
            allCardsValue = allCardsValue + card.getValue();
        }
        if (allCardsValue <= valueToReturnIfStorm) {
            temporaryStack.addAll(all);
            less = true;
        }

        return less;
    }*/

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayer(int requiredIndex) {
        return players.get(requiredIndex);
    }

    public List<Card> getMainStack() {
        return mainStack;
    }

    public List<Card> getToReturn() {
        return toReturn;
    }

    public int getToReturnValue() {
        int value = 0;
        for (Card card : toReturn) {
            value = value + card.getValue();
        }
        return value;
    }

    @Override
    public void react(Event event) {
        switch (event.getType()) {
            case GAME_START:
                gameStart();
                break;
            case DRAW_CARD_DECISION:
                drawAndAssign();
                break;
            case CARD_PURCHASE_DECISION:
                buyShipCard();
                switchToNextPlayer();
                break;
            case PASS_DECISION:
                switchToNextPlayer();
                break;
            case CLICK_ON_COIN: // przez event.getPlayer nie działało bo controller nie setował playera przy tworzeniu eventu
               /* if (getCurrentPlayer().coins.size() > 0) {
                    Card card = getCurrentPlayer().coins.get(0);
                    toReturn.add(card);
                    getCurrentPlayer().coins.remove(card);
                    selectCardsToReturn();
                } else {
                    selectCardsToReturn();
                }
                break;*/
            case CLICK_ON_CANNON:
              /*  if (getCurrentPlayer().cannons.size() > 0) {
                    Card card1 = getCurrentPlayer().cannons.get(0);
                    toReturn.add(card1);
                    getCurrentPlayer().cannons.remove(card1);
                } else {
                    selectCardsToReturn();
                }
                break;*/
            case CLICK_ON_SHIP:
              /*  if (getCurrentPlayer().shipsToReturn.size() > 0) {
                    Card card2 = getCurrentPlayer().shipsToReturn.get(0);
                    toReturn.add(card2);
                    getCurrentPlayer().shipsToReturn.remove(card2);
                } else {
                    selectCardsToReturn();
                }
                break;*/
            case CLICK_ON_SHIP_COLLECTED:
              /*  if (getCurrentPlayer().shipsCollected.size() > 0) {
                    Card card3 = getCurrentPlayer().shipsCollected.get(0);
                    toReturn.add(card3);
                    getCurrentPlayer().shipsCollected.remove(card3);
                } else {
                    selectCardsToReturn();
                }*/
            default:
                System.out.println("default w game - react");
        }
    }
}
