package ship.game.server;

import ship.game.server.events.Event;
import ship.game.server.events.EventBus;
import ship.game.server.events.EventListener;
import ship.game.server.events.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements EventListener {

    private List<Card> mainStack;
    private final int mainStackIndex = 5;
    private List<Card> temporaryStack = new ArrayList<>();
    private final int temporaryStackIndex = 6;
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Repository repository;

    public Game(Repository repository) {
        this.repository = repository;
        CardFactory factory = new CardFactory();
        mainStack = factory.createCards();
        EventBus.subscribe(EventType.GAME_START, this);
        EventBus.subscribe(EventType.DRAW_CARD_DECISION, this);
        EventBus.subscribe(EventType.CLICK_ON_COIN, this);
        EventBus.subscribe(EventType.CLICK_ON_CANNON, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP, this);
        EventBus.subscribe(EventType.CLICK_ON_SHIP_COLLECTED, this);
        EventBus.subscribe(EventType.CARD_PURCHASE_DECISION, this);
        EventBus.subscribe(EventType.PASS_DECISION, this);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void gameStart() { // setowanie playera w evencie
        // event wyciągnięty do zmiennej by móc to zrobić
        //temporaryStack.clear();
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
            temporaryStack.clear();
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

        if (drawn.getType().equals(Card.Type.SHIP) && shipIsNotCollected(drawn)) {
            getCurrentPlayer().setCollected(drawn);
        }
        if (!drawn.getType().equals(Card.Type.STORM)) {
            getCurrentPlayer().addCard(drawn);
            drawn.setPlayerIndex(getCurrentPlayer().getPlayerIndex());
            if (getCurrentPlayer().checkIfLastShipCard()) {
                Event endGame = new Event(EventType.GAME_END);
                endGame.setPlayer(getCurrentPlayer());
                EventBus.notify(endGame);
                return;
            }
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            addToTemporaryStack(drawn);
        }
        Event drawCardEvent = new Event(EventType.DRAW_CARD);
        drawCardEvent.setCard(drawn);
        drawCardEvent.setPlayer(getCurrentPlayer());
        EventBus.notify(drawCardEvent);
        getCurrentPlayer().showOwnStack(); //to debug
    }

    public boolean shipIsNotCollected(Card shipCard) {
        boolean available = true;
        for (Player player : players) {
            if (player.isCollectingThisShip(shipCard)) {
                available = false;
            }
        }
        return available;
    }

    public void switchToNextPlayer() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        for (Player player : players) {
            player.setStillPlaying(player.equals(getCurrentPlayer())); // setowanie na true gdy warunek w nawiasie = true i odwrotnie
        }
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer()); // kolejny player = current z kodu powyżej
        EventBus.notify(event);
        System.out.println("Ustawienie gracza na: " + getCurrentPlayer().getPlayerIndex());
    }

    public void buyCard(Event event) {
        // przekazywanie żądanej karty między playerami:
        getCurrentPlayer().addCard(event.getCard()); // current player, requested card
        event.getPlayer().removeCard(event.getCard()); // requested player, requested card
        // płacenie za kartę:
        event.getPlayer().addAll(getCurrentPlayer().get3CoinsToPay()); // gracz z eventu ma dostać 3 monety od currentPlayera
    }

    public void saveGame() {
        repository.savePlayers(players);
        repository.createTableCards();
        for (Player player : players) {
            repository.saveCards(player.getOwnStack(), player.getPlayerIndex());
        }
        repository.saveCards(mainStack, mainStackIndex);
        repository.saveCards(temporaryStack, temporaryStackIndex);
    }

    public void restorePlayersFromDB() {
        players.clear();
        players = repository.getPlayersFrom();
    }

    public void restoreCardsFromDB() {
        mainStack.clear();
        temporaryStack.clear();

        List<Card> P1Cards = repository.getCardsFrom(1);
        for (Card card : P1Cards) {
            players.get(0).addCard(card); // uwaga na indeks!
        }
        List<Card> P2Cards = repository.getCardsFrom(2);
        for (Card card : P2Cards) {
            players.get(1).addCard(card);
        }
        mainStack = repository.getCardsFrom(5);
        temporaryStack = repository.getCardsFrom(6);
    }

    public void addToTemporaryStack(Card card) {
        temporaryStack.add(card);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayerByIndex(int requiredIndex) {
        if (requiredIndex < 0) {
            System.out.println("required index < 0");
        } else if (requiredIndex >= players.size()) {
            System.out.println("required index is bigger than number of players");
        }
        return players.get(requiredIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getMainStack() {
        return mainStack;
    }

    public List<Card> getTemporaryStack() {
        return temporaryStack;
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
                buyCard(event);
                switchToNextPlayer();
                break;
            case PASS_DECISION:
                switchToNextPlayer();
                break;
            default:
                System.out.println("default w game - react");
        }
    }
}
