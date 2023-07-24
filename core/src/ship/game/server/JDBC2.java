package ship.game.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC2 {

    public static void main(String[] args) {
       /* CardFactory factory = new CardFactory(); // dodatkowy obiekt
        List<Card> cards = factory.createCards();

        Game game = new Game(); // dodatkowy obiekt
        game.addPlayer(new Player(1)); // pobierać playerów z prawdziwej game!
        game.addPlayer(new Player(2));
        List<Player> players = game.getPlayers();
        ResultSet resultSet = null;
        Card newCard = null;
        Player newPlayer = null;
        List<Card> cardsFromDB = new ArrayList<>();
        List<Player> playersFromDB = new ArrayList<>();
*/

/*        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game",
                    "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cards VALUES (0,?,?,?,?,null);",
                    Statement.RETURN_GENERATED_KEYS);


*//*            // testowa tabela
            String putOneTestCard = "INSERT INTO cards VALUES (0, 'SHIP', 1, 'S1', 1, 1);";
            statement.executeUpdate(putOneTestCard);
            System.out.println("done");
            connection.close();*//*
*//*
            // tabela players
            String createTablePlayers = "CREATE TABLE players (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "collected_ship_type VARCHAR(255),\n" +
                    "last_turn CHAR,\n" +
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTablePlayers);
            System.out.println("Table players created");*//*

*//*            // tabela cards
            // dodać przed tworzeniem tabeli if exist
            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "player_id INTEGER, \n" + // 1-4 players Ownerem może być tylko player. Jesli player nie ma karty, to player_id == null
                    // player_id = foreign key do id z tabeli palyers
                    // w kolumnie player_id mogą być tylko wartośc 1/2 (dwóch graczy) liub null (mainstack/temporary)
                    "PRIMARY KEY (id),\n" +
                    "FOREIGN KEY (player_id) REFERENCES players(id));";
            //SELECT * FROM cards INNER JOIN players ON players.id = cards.player_id;
            statement.executeUpdate(createTableCards);
            System.out.println("Table cards created");*//*

*//*
            // uzupełnienie tabeli cards
            for (Card card : cards) {
                preparedStatement.setString(1, String.valueOf(card.getType()));
                preparedStatement.setString(2, card.getSecondShipType());
                preparedStatement.setInt(3, card.getPictureIndex());
                preparedStatement.setInt(4, card.getStormValue());
                if(card.getPlayerId() != null) {
                   preparedStatement.setInt(5, card.getPlayerId());
                }
                //preparedStatement.setInt(5, card.getPlayerId());
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); // wyciąganie id z BD, dodane do obiektu Card
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    card.setId(id);
                }
            }
            System.out.println("Data transfered to table");
            connection.close();
*//*

*//*
            // pobieranie kart z BD
            String select = "SELECT * FROM cards;";
            resultSet = statement.executeQuery(select);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String secondShipType = resultSet.getString("second_ship_type");
                int pictureIndex = resultSet.getInt("picture_index");
                int stormVal = resultSet.getInt("storm_value");
                int playerId = resultSet.getInt("player_id");
                newCard = new Card(Card.Type.valueOf(type), secondShipType, pictureIndex, stormVal);
                newCard.setId(id); // dlaczego drugi raz set.id?
                newCard.setPlayerId(playerId);
                cardsFromDB.add(newCard);
            }
            System.out.println("Cards from DB");
            for (Card card : cardsFromDB) {
                System.out.println(card);
            }
*//*


*//*            // tabela players
            String createTablePlayers = "CREATE TABLE players (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "collected_ship_type VARCHAR(255),\n" +
                    "last_turn CHAR,\n" +
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTablePlayers);
            System.out.println("Table players created");*//*

*//*            // uzupełnianie tabeli players
            for (Player player : players) {
                preparedStatement = connection.prepareStatement("INSERT INTO players VALUES (0,?,?);", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, player.getCollectedShipType());
                preparedStatement.setString(2, player.getPlayingStatus());
                ResultSet generatedPlayerKeys = preparedStatement.getGeneratedKeys();
                if (generatedPlayerKeys.next()) {
                    int id = generatedPlayerKeys.getInt("id");
                    player.setId(id);
                }
                preparedStatement.executeUpdate();
            }

            System.out.println("Players data transfered to DB");
            //connection.close();*//*

*//*            // pobieranie playerów z BD
            String select1 = "SELECT * FROM players;";
            resultSet = statement.executeQuery(select1);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String collectedType = resultSet.getString("collected_ship_type");
                //int stackSize = resultSet.getInt("stack_size");
                //String lastTurn = resultSet.getString("last_turn");
                newPlayer = new Player(id, collectedType); // tworzy obiekt za pomocą drugiego konstrukt.
                newPlayer.setId(id);
                playersFromDB.add(newPlayer);
            }*//*

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
