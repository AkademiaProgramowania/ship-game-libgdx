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
    private final List<Player> players = new ArrayList<>();
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
            String baseStatementPlayers = "INSERT INTO players (collected_ship_type, stack_size, player_index) values ('%s','%d', %d);";
            String baseStatementCards = "INSERT INTO cards (type, second_ship_type, picture_index, storm_value, owner) values ('%s','%s',%d,%d,%d);";
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

            // uzupełnianie tabeli players
            for (Player player : players) {
                String sqlStatement = String.format(baseStatementPlayers, player.getCollectedShipType(),
                        player.getStackSize(), player.getPlayerIndex());
                statement.execute(sqlStatement);
            }

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
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assignNewPlayersFromDB() {
        for (Player player : players) {
            player.getOwnStack().clear();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String baseStatementPlayers = "SELECT * FROM players WHERE player_index = %d;";
            ResultSet resultSet1 = statement.executeQuery(String.format(baseStatementPlayers, 1));
            while (resultSet1.next()) {
                String collectedType = resultSet1.getString("collected_ship_type");
                int playerIndex = resultSet1.getInt("player_index"); // player index = owner w tabeli cards
                Player newPlayer = new Player(playerIndex);
                newPlayer.setCollectedShipType(collectedType);
                addPlayer(newPlayer);
                // todo dopisać setowanie last_turn
                // poprawić isStillPlaying
            }
            ResultSet resultSet2 = statement.executeQuery(String.format(baseStatementPlayers, 2));
            while (resultSet2.next()) {
                String collectedType = resultSet2.getString("collected_ship_type");
                int playerIndex = resultSet2.getInt("player_index"); // player index = owner w tabeli cards
                Player newPlayer = new Player(playerIndex);
                newPlayer.setCollectedShipType(collectedType);
                addPlayer(newPlayer);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assignNewCardsFromDB() {
        mainStack.clear();
        temporaryStack.clear();
        List<Card> P1Cards = new ArrayList<>();
        List<Card> P2Cards = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String query1 = "SELECT * FROM cards WHERE owner = 1;";
            ResultSet resultSet1 = statement.executeQuery(query1);
            while (resultSet1.next()) {
                String collectedType = resultSet1.getString("type");
                String secondShipType = resultSet1.getString("second_ship_type");
                int pictureIndex = resultSet1.getInt("picture_index");
                int stormValue = resultSet1.getInt("storm_value");
                int playerIndex = resultSet1.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                P1Cards.add(newCard);
            }
            connection.close();
            System.out.println("rozmiar listy P1" + P1Cards.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String query2 = "SELECT * FROM cards WHERE owner = 2;";
            ResultSet resultSet2 = statement.executeQuery(query2);
            while (resultSet2.next()) {
                String collectedType = resultSet2.getString("type");
                String secondShipType = resultSet2.getString("second_ship_type");
                int pictureIndex = resultSet2.getInt("picture_index");
                int stormValue = resultSet2.getInt("storm_value");
                int playerIndex = resultSet2.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                P2Cards.add(newCard);
            }
            connection.close();
            System.out.println("rozmiar listy P2" + P2Cards.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Card card : P1Cards) {
            players.get(0).addCard(card); // uwaga na indeks!
        }
        for (Card card : P2Cards) {
            players.get(1).addCard(card);
        }

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String query3 = "SELECT * FROM cards WHERE owner = 5;";
            ResultSet resultSet3 = statement.executeQuery(query3);
            while (resultSet3.next()) {
                String collectedType = resultSet3.getString("type");
                String secondShipType = resultSet3.getString("second_ship_type");
                int pictureIndex = resultSet3.getInt("picture_index");
                int stormValue = resultSet3.getInt("storm_value");
                int playerIndex = resultSet3.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                mainStack.add(newCard);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String query4 = "SELECT * FROM cards WHERE owner = 6;";
            ResultSet resultSet4 = statement.executeQuery(query4);
            while (resultSet4.next()) {
                String collectedType = resultSet4.getString("type");
                String secondShipType = resultSet4.getString("second_ship_type");
                int pictureIndex = resultSet4.getInt("picture_index");
                int stormValue = resultSet4.getInt("storm_value");
                int playerIndex = resultSet4.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                temporaryStack.add(newCard);
            }
            connection.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
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
