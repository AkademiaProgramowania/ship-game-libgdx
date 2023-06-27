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
    private final int mainStackIndex = 5;
    private final List<Card> temporaryStack = new ArrayList<>();
    private final int temporaryStackIndex = 6;

    private final List<Player> players = new ArrayList<>(); // pole do przechowywania zainicjalizowane w konstruktorze
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
        //temporaryStack.clear(); //todo raczej nie potrzebne
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
        Event event = new Event(EventType.PLAYER_SWITCHED);
        event.setPlayer(getCurrentPlayer()); // kolejny player = current z kodu powyżej
        EventBus.notify(event);
        getCurrentPlayer().stillPlaying(true); // kolejny!
        // todo refactor (should not contain selector argument)
        // https://rules.sonarsource.com/java/RSPEC-2301
        System.out.println("Ustawienie gracza na: " + getCurrentPlayer().getPlayerIndex());

    }

    // uwaga z przypisaniem karty waściwemu graczowi - do testów
    public void buyCard(Event event) {// w evencie jest ustawiony requested player i requested card
        // przekazywanie żądanej karty między playerami:
        getCurrentPlayer().addCard(event.getCard()); // current player, requested card
        event.getPlayer().removeCard(event.getCard()); // requested player, requested card
        // płacenie za kartę:
        int num = 0;
        do {
            event.getPlayer().addCard(getCurrentPlayer().getCoinToPay()); // gracz z eventu ma dostać 3 monety od currentPlayera
            // usuwanie COIN ze staka currentPlayera w metodzie
            num++;
        } while (num <= 2);
        // todo spr czy karty monet przechodzą ze stacka currentPlayer na stack requestedPlayer
        System.out.println("current player - monety: " + getCurrentPlayer().getCards(Card.Type.COIN));
        System.out.println("current player - statki: " + getCurrentPlayer().getShipsCollected(true));
        System.out.println("requested player - monety: " + getCurrentPlayer().getCards(Card.Type.COIN));
    }

    public void saveGame() {
        // przy klażdym savie tworzy tabele (najpierw players potem cards) jeśli jej nie ma
        // uzupełnia players z collected_ship_type i stack size (orientacyjnie żeby spr czy się zgadza)
        // uzupełnia karty z owner
        // nie potrzeba update (gdyby player był, update do ustawienia playera UPDATE cards SET player_id = null WHERE cards.id = 4; (przykładowo)

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            String baseStatementPlayers = "INSERT INTO players (collected_ship_type, stack_size, player_index) values ('%s','%o', %o);";
            String baseStatementCards = "INSERT INTO cards (type, second_ship_type, picture_index, storm_value, owner) values ('%s','%s',%o,%o,%o);";
            Statement statement = connection.createStatement();

            // tabela players
            String delete = "DELETE FROM players WHERE player_index BETWEEN 1 AND 2;";
            statement.execute(delete);
            String createTablePlayers = "CREATE TABLE IF NOT EXISTS players (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "collected_ship_type VARCHAR(255),\n" +
                    "stack_size INTEGER,\n" +
                    "player_index INTEGER, \n" +
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTablePlayers);
            System.out.println("Table players created");

            // uzupełnianie tabeli players
            for (Player player : players) {
                String sqlStatement = String.format(baseStatementPlayers, player.getCollectedShipType(),
                        player.getStackSize(), player.getPlayerIndex());
                statement.execute(sqlStatement);
            }
            System.out.println("table players filled");

            // tabela cards
            String dropCards = " DROP TABLE IF EXISTS cards;";
            statement.executeUpdate(dropCards);
            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "owner INTEGER, \n" +
                    "PRIMARY KEY (id));";// +
                    //"FOREIGN KEY (player_index) REFERENCES players(player_index));"; // nie wstawiać foreign key które nie jest unikalne!
            statement.executeUpdate(createTableCards);
            System.out.println("Table cards created");

            // uzupełnianie tabeli cards przy każdym savie od nowa - wszystkie karty z właściwym ownerem (1-2 players, 5 mainStack, 6 temporaryStack)
            // przerobić na pozyskiwanie kart z roznych staków: playerów, mainStack, temporary.
            // usunąć podwójne card creation i listę allCards z game

            for (Player player : players) { // dla każdego playera
                List<Card> playerStack = new ArrayList<>(player.getOwnStack()); // tworzy i uzupełnia listę kart
                for (Card card : playerStack) { // potem każdą kartę z tej listy wstawia do tabeli
                    String sqlStatement = String.format(baseStatementCards, card.getType().name(),
                            card.getSecondShipType(), card.getPictureIndex(),
                            card.getStormValue(), card.getPlayerIndex());
                    statement.execute(sqlStatement);
                }
            }
            for (Card card : mainStack) {
                String sqlStatement = String.format(baseStatementCards, card.getType().name(),
                        card.getSecondShipType(), card.getPictureIndex(),
                        card.getStormValue(), mainStackIndex);
                statement.execute(sqlStatement);
            }
            for (Card card : temporaryStack) {
                String sqlStatement = String.format(baseStatementCards, card.getType().name(),
                        card.getSecondShipType(), card.getPictureIndex(),
                        card.getStormValue(), temporaryStackIndex);
                statement.execute(sqlStatement);

            }
            System.out.println("Table cards filled");
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayersFromDB() {
        List<Player> playersFromDB = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String select1 = "SELECT * FROM players;";
            ResultSet resultSet = statement.executeQuery(select1);
            while (resultSet.next()) {
                String collectedType = resultSet.getString("collected_ship_type");
                int playerIndex = resultSet.getInt("player_index"); // player index = owner w tabeli cards
                Player newPlayer = new Player(playerIndex);
                newPlayer.setCollectedShipType(collectedType);
                playersFromDB.add(newPlayer);
                // todo dopisać setowanie last_turn
                // poprawić isStillPlaying
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playersFromDB;
    }

    public List<Card> getCardsFromDB() {
        List<Card> cardsFromDB = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String select1 = "SELECT * FROM cards;";
            ResultSet resultSet = statement.executeQuery(select1);
            while (resultSet.next()) {
                String collectedType = resultSet.getString("type");
                String secondShipType = resultSet.getString("second_ship_type");
                int pictureIndex = resultSet.getInt("picture_index");
                int stormValue = resultSet.getInt("storm_value");
                int playerIndex = resultSet.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                cardsFromDB.add(newCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardsFromDB;
    }

    public void assignPlayersFromDB() {
        players.clear();
        List<Player> playersFromDB = getPlayersFromDB();
        for (Player player : playersFromDB) {
            addPlayer(player);
            System.out.println(player);
        }
    }

    public void assignCardsFromDB() {
        mainStack.clear();
        temporaryStack.clear();
        List<Card> cardsFromDB = getCardsFromDB();
        for (Card card : cardsFromDB) { // dla każdej karty z DB
            if (card.getPlayerIndex() == 1) {
                getPlayerByIndex(0).addCard(card);
            }
            if (card.getPlayerIndex() == 2-1) {
                getPlayerByIndex(1).addCard(card);
            }
            if (card.getPlayerIndex() == 5) {
                mainStack.add(card);
            }
            if (card.getPlayerIndex() == 6) {
                temporaryStack.add(card);
            }
        }
        for (Player player : players) {
            player.showOwnStack();
        }
        System.out.println("Mainstack: " + " size: " + mainStack.size() + " " + mainStack.toString());
        System.out.println("Temporary: " + " size: " + temporaryStack.size() + " " + temporaryStack.toString());

    }

    public void addToTemporaryStack(Card card) {
        temporaryStack.add(card);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayerByIndex(int requiredIndex) {
        return players.get(requiredIndex);
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
