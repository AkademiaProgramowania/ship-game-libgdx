package ship.game.server;

import ship.game.server.events.Event;
import ship.game.server.events.EventBus;
import ship.game.server.events.EventListener;
import ship.game.server.events.EventType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements EventListener {

    private List<Card> mainStack;
    private List<Card> temporaryStack = new ArrayList<>(); // stos tymczasowy, tu są odkładane karty zanim stos głowny
    // się skończy i będzie nowe tasowanie

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
        EventBus.subscribe(EventType.PASS_DECISION, this);
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
            for (Card card : mainStack) {
                card.setOwner(5);
            }
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
            getCurrentPlayer().setCollected(drawn); // tu tylko zmiana pola collectedShipType
            // liczbę dotychczas zebranych statków tego typu pokazuje jako kolekcjonowane w controllerze
            // czy zmienić komunikat na info o przejściu ze staku na handel do kolekcjonowanych?
        }
        if (!drawn.getType().equals(Card.Type.STORM)) {
            getCurrentPlayer().addCard(drawn);
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
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer()); // kolejny player = current z kodu powyżej
        EventBus.notify(event);
        getCurrentPlayer().stillPlaying(true); // kolejny! // don't understand
        System.out.println("Ustawienie gracza na: " + getCurrentPlayer().toString());

    }

   // uwaga z przypisaniem karty waściwemu graczowi - do testów
    public void buyCard(Event event) {// player = requested player, card = requested card
        event.getPlayer().removeCard(event.getCard()); // requested player, requested card
        getCurrentPlayer().addCard(event.getCard()); // current player, requested card. Setowanie ownera karty w metodzie addCard
        int num = 0;
        do {
            event.getPlayer().addCard(getCurrentPlayer().getCoinCard()); // add coin, nie requested card z eventu
            // usuwanie COIN ze staka currentPlayera w metodzie
            num++;
        } while (num <= 2);
        // todo spr czy karty monet przechodzą ze stacka currentPlayer na stack requestedPlayer
        System.out.println("current player - monety: " + getCurrentPlayer().getCards(Card.Type.COIN));
        System.out.println("current player - statki: " + getCurrentPlayer().getShipsCollected(true));
        System.out.println("requested player - monety: " + getCurrentPlayer().getCards(Card.Type.COIN));
    }

    public void savePlayers() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            PreparedStatement preparedStatement = null;
            for (Player player : players) {

                preparedStatement = connection.prepareStatement("INSERT INTO players VALUES (?,?,?,?,?);");
                preparedStatement.setInt(1, 20+player.getPlayerIndex());
                preparedStatement.setInt(2, player.getPlayerIndex());
                preparedStatement.setString(3, player.getCollectedShipType());
                preparedStatement.setInt(4, player.getStackSize());
                preparedStatement.setString(4, player.getPlayingStatus());
                //assert preparedStatement != null;
                System.out.println(preparedStatement.executeUpdate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayersFromDB(){
        List<Player> playersFromDB = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String select1 = "SELECT * FROM players;";
            ResultSet resultSet = statement.executeQuery(select1);
            while (resultSet.next()) {
                int playerIndex = resultSet.getInt("player_index");
                String collectedType = resultSet.getString("collected_ship_type");
                int stackSize = resultSet.getInt("stack_size");
                String lastTurn = resultSet.getString("last_turn");
                Player newPlayer = new Player(playerIndex, collectedType); // tworzy obiekt za pomocą drugiego konstrukt.
                playersFromDB.add(newPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playersFromDB;
    }

    public void addToTemporaryStack(Card card) {
        temporaryStack.add(card);
        card.setOwner(6);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayerByIndex(int requiredIndex) {
        return players.get(requiredIndex);
    }


    public List<Card> getTemporaryStack() {
        return temporaryStack;
    }

    public List<Player> getPlayers() {
        return players;
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
