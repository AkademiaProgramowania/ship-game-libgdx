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
        EventBus.subscribe(EventType.CARD_PURCHASE_DECISION, this);

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
            if (getCurrentPlayer().checkIfFirstShipCardAndSetCollected(drawn)) {
                getCurrentPlayer().shipsCollected.add(drawn);
                Event collected = new Event(EventType.SET_SHIP_TYPE_TO_COLLECT);
                collected.setPlayer(getCurrentPlayer());
                EventBus.notify(collected);
            } else {// spr czy tu dochodzi program
                getCurrentPlayer().shipsToReturn.add(drawn);
            }
        }
        if (drawn.getType().equals(Card.Type.COIN)) {
            getCurrentPlayer().coins.add(drawn);
        }
        if (drawn.getType().equals(Card.Type.CANNON)) {
            getCurrentPlayer().cannons.add(drawn);
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            temporaryStack.add(drawn);
            int value = 0; // to samo zliczanie w selectCardsToReturn()
            do {
                selectCardsToReturn();
                for (Card card : toReturn) {
                    value = value + card.getValue();
                }
            } while (value < 3);
            switchToNextPlayer(); // gdy storm, po zakońćzeniu oddawania kart w obu opcjach powyżej

        }
        // spr czy jest cannon, jeśli nie to daj dowolne 3 inne karty
        /*if (howMuchValueInOwnStack() <= 3) { // jeśli kart jest mniej niż za 3pkt
            temporaryStack.addAll(getCurrentPlayer().getOwnStack()); // zwraca wszystkie
            System.out.println("Temporary stack: " + temporaryStack.toString());
            // w temporary stack powinno być: drawn + wszystkie karty z ownStack gdy ich wartość < 3
        } else { // jesli kart jest więcej niż za 3 pkt
            selectCardsToReturn(); // select w controllerze, wybrane dodają się do toReturn w game
            temporaryStack.addAll(toReturn);
        }*/

        {
            Event endGame = new Event(EventType.GAME_END);
            endGame.setPlayer(getCurrentPlayer());
            EventBus.notify(endGame);
        }
        // czy przenieść na początek bloku by uniknać zbyt późnego wyświetlenia reakcji?
        Event drawCardEvent = new Event(EventType.DRAW_CARD);
        drawCardEvent.setCard(drawn);
        drawCardEvent.setPlayer(getCurrentPlayer());
        EventBus.notify(drawCardEvent);
    }

    public void switchToNextPlayer() {
        if (currentPlayerIndex == players.size() - 1) { // jeżeli aktualny jest ostatni
            currentPlayerIndex = 0; // to przestaw na pierwszego
        } else { // inaczej idż do kolejnego
            currentPlayerIndex++;
        }
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer());
        EventBus.notify(event);
        event.getPlayer().stillPlaying(true); // spr czy działa w dobra stronę
    }

    public void buyShipCard() {
        if (getCurrentPlayer().coins.size() >= 3) {
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

    public void selectCardsToReturn() {
        System.out.println(toReturn.toString());
        // licz wartość zwróconych kart i dopóki nie będzie 3,
        // wysyłaj event do controllera
        int sumValue = 0;
        while (sumValue < 3)
            for (Card card : toReturn) { // w toReturn są karty klikane w controller/selectCardsToReturn
                int value = card.getValue();
                sumValue = sumValue + value;
                System.out.println(sumValue);

                Event selectCards = new Event(EventType.SELECT_CARDS_TO_RETURN);
                selectCards.setPlayer(getCurrentPlayer());
                EventBus.notify(selectCards);
            }
    }

/*    public int howMuchValueInOwnStack() {
        // co to ma robić?
        // spr na jaką łączną wartośc jest kart w staku
        // jeśli na mniej niż 3,
        int sumaTymczasWarttosci = 0; // suma
        for (Card card : getCurrentPlayer().getOwnStack()) {
            int value = card.getValue();
            sumaTymczasWarttosci = sumaTymczasWarttosci + value; // dla kazdej karty bierze value i dodaje
        }
        return sumaTymczasWarttosci;
    }*/

/*    public void returnCardsIfLessThan3() {
        int sumaTymczasWarttosci = 0;
        for (Card card : getCurrentPlayer().getOwnStack()) {
            int value = card.getValue();
            sumaTymczasWarttosci = sumaTymczasWarttosci + value;
        }
        if (sumaTymczasWarttosci < 3) {
            temporaryStack.addAll(getCurrentPlayer().getOwnStack());
        }
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
            case CLICK_ON_COIN:
                if (event.getPlayer().coins.size() > 0) {
                    Card card = event.getPlayer().coins.get(0);
                    toReturn.add(card);
                } else {
                    selectCardsToReturn();
                }
                break;
            case CLICK_ON_CANNON:
                if (event.getPlayer().cannons.size() > 0) {
                    Card card1 = event.getPlayer().cannons.get(0);
                    toReturn.add(card1);
                } else {
                    selectCardsToReturn();
                }
                break;
            case CLICK_ON_SHIP:
                if (event.getPlayer().shipsToReturn.size() > 0) {
                    Card card2 = event.getPlayer().shipsToReturn.get(0);
                    toReturn.add(card2);
                } else {
                    selectCardsToReturn();
                }
                break;
            case CLICK_ON_SHIP_COLLECTED:
                if (event.getPlayer().shipsCollected.size() > 0) {
                    Card card3 = event.getPlayer().shipsCollected.get(0);
                    toReturn.add(card3);
                } else {
                    selectCardsToReturn();
                }
        }
    }
}
