package ship.game.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC2 {

        public static void main(String[] args) {
            CardFactory factory = new CardFactory();
            List<Card> cards = factory.createCards();
            ResultSet resultSet = null;
            Card newCard = null;
            List<Card> cardsFromDB = new ArrayList<>();

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = null;




/*                // to działa:
                String putOneTestCard = "INSERT INTO cards VALUES (0, 'SHIP', 'S1', 1, 1);";
                statement.executeUpdate(putOneTestCard);
                System.out.println("done");
                connection.close();*/

                // dodać przed tworzeniem tabeli if exist

/*                // to działa:
            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "PRIMARY KEY (id));\n";

            statement.executeUpdate(createTableCards);
            System.out.println("Table created");*/

/*                    // to działa:
                for (Card card : cards) {
                    preparedStatement = connection.prepareStatement("INSERT INTO cards VALUES (0,?,?,?,?);");
                    preparedStatement.setString(1, String.valueOf(card.getType()));
                    preparedStatement.setString(2, card.getSecondShipType());
                    preparedStatement.setInt(3, card.getPictureIndex());
                    preparedStatement.setInt(4, card.getStormValue());
                   //assert preparedStatement != null;
                    System.out.println(preparedStatement.executeUpdate());

                }
                System.out.println("1 = row affected, 2 = no");
                connection.close();*/

                // to działa:
                String select = "SELECT * FROM cards;";
                resultSet = statement.executeQuery(select);
                while (resultSet.next()){
                    String type = resultSet.getString("type");
                    String secondType = resultSet.getString("second_ship_type");
                    int pictureIndex = resultSet.getInt("picture_index");
                    int stormVal = resultSet.getInt("storm_value");
                    newCard = new Card(Card.Type.valueOf(type), secondType, pictureIndex,stormVal); // tworzy obiekt za pomocą konstrukt.
                    cardsFromDB.add(newCard);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int index = 0;
            for (Card card : cardsFromDB) {
                index++;
                System.out.println(index + " " + card.toString());
            }
        }
    }

   /* String sql = "CREATE TABLE GamePlayers" +
            "(id INTEGER not null," +
            "player INTEGER," + // playerNum
            "cards VARCHAR(255)," + // lista obiektów
            "mainStack VARCHAR(255)," + // lista obiektów
            "temporaryStack VARCHAR(255)," + // lista obiektów
            "lastPlayer VARCHAR(255)," + // tak/nie - czy dany player był ostatni albo coś podobnego
            "PRIMARY KEY(id))";*/

