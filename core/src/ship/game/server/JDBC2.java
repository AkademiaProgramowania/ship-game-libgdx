package ship.game.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC2 {

    public static void main(String[] args) {
        CardFactory factory = new CardFactory(); // dodatkowy obiekt
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

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = null;

/*            // testowa tabela
            String putOneTestCard = "INSERT INTO cards VALUES (0, 'SHIP', 1, 'S1', 1, 1);";
            statement.executeUpdate(putOneTestCard);
            System.out.println("done");
            connection.close();*/

            // tabela cards
            // dodać przed tworzeniem tabeli if exist
            String createTableCards = "CREATE TABLE cards (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "type VARCHAR(255),\n" +
                    "card_index INTEGER, \n" +
                    "second_ship_type VARCHAR(255),\n" +
                    "picture_index INTEGER,\n" +
                    "storm_value INTEGER,\n" +
                    "owner INTEGER, \n" + // 1-4 players, 5 mainStack, 6 temporaryStack
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTableCards);
            System.out.println("Table cards created");


            // uzupełnienie tabeli cards
            for (Card card : cards) {
                preparedStatement = connection.prepareStatement("INSERT INTO cards VALUES (0,?,?,?,?,?,?);");
                preparedStatement.setString(1, String.valueOf(card.getType()));
                preparedStatement.setInt(2, card.getCardIndex());
                preparedStatement.setString(3, card.getSecondShipType());
                preparedStatement.setInt(4, card.getPictureIndex());
                preparedStatement.setInt(5, card.getStormValue());
                preparedStatement.setInt(6, card.getOwner());
                //assert preparedStatement != null;
                System.out.println(preparedStatement.executeUpdate());
            }
            System.out.println("1 = row affected, 2 = no");
            connection.close();

            // pobieranie kart z BD
            String select = "SELECT * FROM cards;";
            resultSet = statement.executeQuery(select);
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                int index = resultSet.getInt("card_index");
                String secondType = resultSet.getString("second_ship_type");
                int pictureIndex = resultSet.getInt("picture_index");
                int stormVal = resultSet.getInt("storm_value");
                int owner = resultSet.getInt("owner");
                newCard = new Card(Card.Type.valueOf(type), index, secondType, pictureIndex, stormVal, owner); // tworzy obiekt za pomocą konstrukt.
                cardsFromDB.add(newCard);
            }

            // tabela players
            // player ma: int index, String collectesShipType, stillPlaying, ownStack.size
            String createTablePlayers = "CREATE TABLE players (\n" +
                    "id INTEGER not null AUTO_INCREMENT,\n" +
                    "player_index INTEGER, \n" +
                    "collected_ship_type VARCHAR(255),\n" + // "is_playing BIT,\n" +
                    "stack_size INTEGER,\n" +
                    "last_turn CHAR,\n" +
                    "PRIMARY KEY (id));\n";
            statement.executeUpdate(createTablePlayers);
            System.out.println("Table players created");

            // uzupełnianie tabeli players
            System.out.println("Lista players");
            System.out.println(players.toString());

            for (Player player : players) {
                preparedStatement = connection.prepareStatement("INSERT INTO players VALUES (0,?,?,?,?);");
                preparedStatement.setInt(1, player.getPlayerIndex());
                preparedStatement.setString(2, player.getCollectedShipType());
                preparedStatement.setInt(3, player.getStackSize());
                preparedStatement.setString(4, player.getPlayingStatus());
                //assert preparedStatement != null;
                System.out.println(preparedStatement.executeUpdate());
            }
            System.out.println("1 = row affected, 2 = no");
            connection.close();

            // pobieranie playerów z BD
            String select1 = "SELECT * FROM players;";
            resultSet = statement.executeQuery(select1);
            while (resultSet.next()) {
                int playerIndex = resultSet.getInt("player_index");
                String collectedType = resultSet.getString("collected_ship_type");
                int stackSize = resultSet.getInt("stack_size");
                String lastTurn = resultSet.getString("last_turn");
                newPlayer = new Player(playerIndex, collectedType); // tworzy obiekt za pomocą drugiego konstrukt.
                playersFromDB.add(newPlayer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        int index = 0;
        System.out.println("Karty z DB:");
        for (Card card : cardsFromDB) {
            index++;
            System.out.println(index + " " + card.toString());
        }
        System.out.println("Playerzy z DB:");
        index = 0;
        for (Player player : playersFromDB) {
            index++;
            System.out.println(index + " " + player.toString());
        }
    }
}
// todo dostosowanie last_turn w isStillPlaying / playingStatus - switchToNextPlayer po wznowieniu gry

