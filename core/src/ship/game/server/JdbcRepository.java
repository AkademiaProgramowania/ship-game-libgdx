package ship.game.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class JdbcRepository implements Repository {
    @Override
    public int savePlayers(List<Player> players) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor");
            String baseStatementPlayers = "INSERT INTO players (collected_ship_type, stack_size, player_index) values ('%s','%d', %d);";
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
            connection.close();
            System.out.println("Players saved");
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
            statement.executeUpdate(createTableCards);

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
}
