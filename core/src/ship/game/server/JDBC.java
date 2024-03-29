package ship.game.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBC {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("INSERT INTO players");

            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "PRIMARY KEY (id));\n";

            statement.executeUpdate(createTableCards);
            System.out.println("Table created");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// jeśli ich tam jeszcze nie ma: (if exist)

   /* String sql = "CREATE TABLE GamePlayers" +
            "(id INTEGER not null," +
            "player INTEGER," + // playerNum
            "cards VARCHAR(255)," + // lista obiektów
            "mainStack VARCHAR(255)," + // lista obiektów
            "temporaryStack VARCHAR(255)," + // lista obiektów
            "lastPlayer VARCHAR(255)," + // tak/nie - czy dany player był ostatni albo coś podobnego
            "PRIMARY KEY(id))";*/