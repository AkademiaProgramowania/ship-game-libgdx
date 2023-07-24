package ship.game.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcRepository implements Repository {
    @Override
    public int savePlayers(List<Player> players) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor");
            String baseStatementPlayers = "INSERT INTO players (collected_ship_type, stack_size, player_index, playing_status) values ('%s', %d, %d, '%s');";
            Statement statement = connection.createStatement();

            // tworzenie tabeli players
            String delete = "DELETE FROM players WHERE player_index BETWEEN 1 AND 4;";
            statement.execute(delete);
            String createTablePlayers = "CREATE TABLE IF NOT EXISTS players (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "collected_ship_type VARCHAR(255),\n" +
                    "stack_size INTEGER,\n" +
                    "player_index INTEGER, \n" +
                    "playing_status VARCHAR(255), \n" +
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTablePlayers);

            // uzupełnianie tabeli players
            for (Player player : players) {
                String sqlStatement = String.format(baseStatementPlayers, player.getCollectedShipType(),
                        player.getStackSize(), player.getPlayerIndex(), player.getPlayingStatus());
                statement.execute(sqlStatement);
            }
            connection.close();
            System.out.println("Players saved");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public int createTableCards() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor");
            Statement statement = connection.createStatement();
            String dropCards = " DROP TABLE IF EXISTS cards;";
            statement.executeUpdate(dropCards);

            // tworzenie tabeli cards
            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "owner INTEGER, \n" +
                    "PRIMARY KEY (id));";// +
            statement.executeUpdate(createTableCards);
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }


    @Override
    public int saveCards(List<Card> cards, int index) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            String baseStatementCards = "INSERT INTO cards (type, second_ship_type, picture_index, storm_value, owner) values ('%s','%s',%d,%d,%d);";
            Statement statement = connection.createStatement();

            // uzupełnianie tabeli cards
            for (Card card : cards) {
                String sqlStatement = String.format(baseStatementCards, card.getType().name(),
                        card.getSecondShipType(), card.getPictureIndex(),
                        card.getStormValue(), index);
                statement.execute(sqlStatement);
            }
            connection.close();
            System.out.println("Cards " + index + " saved");

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public List<Player> getPlayersFrom() { // dopisać opcję dla większej ilości playerów (obecnie 2)
        List<Player> players = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String baseStatementPlayers = "SELECT * FROM players WHERE player_index = %d;";
            ResultSet resultSet1 = statement.executeQuery(String.format(baseStatementPlayers, 1));
            while (resultSet1.next()) {
                String collectedType = resultSet1.getString("collected_ship_type");
                int playerIndex = resultSet1.getInt("player_index"); // player index = owner w tabeli cards
                String playingStatus = resultSet1.getString("playing_status");
                Player newPlayer = new Player(playerIndex);
                newPlayer.setCollectedShipType(collectedType);
                newPlayer.setPlayingStatus(playingStatus);
                players.add(newPlayer);

            }
            ResultSet resultSet2 = statement.executeQuery(String.format(baseStatementPlayers, 2));
            while (resultSet2.next()) {
                String collectedType = resultSet2.getString("collected_ship_type");
                int playerIndex = resultSet2.getInt("player_index"); // player index = owner w tabeli cards
                String playingStatus = resultSet2.getString("playing_status");
                Player newPlayer = new Player(playerIndex);
                newPlayer.setCollectedShipType(collectedType);
                newPlayer.setPlayingStatus(playingStatus);
                players.add(newPlayer);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public List<Card> getCardsFrom(int ownerIndex) {
        List<Card> cards = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            String baseStatementCards = "SELECT * FROM cards WHERE owner = %d;";
            ResultSet resultSet1 = statement.executeQuery(String.format(baseStatementCards, ownerIndex));
            while (resultSet1.next()) {
                String collectedType = resultSet1.getString("type");
                String secondShipType = resultSet1.getString("second_ship_type");
                int pictureIndex = resultSet1.getInt("picture_index");
                int stormValue = resultSet1.getInt("storm_value");
                int playerIndex = resultSet1.getInt("owner");
                Card newCard = new Card(Card.Type.valueOf(collectedType), secondShipType, pictureIndex, stormValue);
                newCard.setPlayerIndex(playerIndex);
                cards.add(newCard);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }
}
