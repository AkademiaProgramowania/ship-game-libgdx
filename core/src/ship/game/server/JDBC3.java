package ship.game.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC3 {


    public static void main(String[] args) {

        CardFactory factory = new CardFactory(); // dodatkowy obiekt
        List<Card> cards = factory.createCards();
        Player player1 = new Player(1);
        player1.addCard(cards.get(0));
        player1.addCard(cards.get(1));

        Player player2 = new Player(2);
        player2.addCard(cards.get(2));
        player2.addCard(cards.get(3));

        System.out.println("P1 " + player1.getOwnStack());
        System.out.println("P2" + player2.getOwnStack());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game",
                    "root", "toor"); // user password to insert manually

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cards VALUES (0,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);


            String baseStatement = "Insert into cards (type, picture_index) values('%s', %o);";
            Statement statement = connection.createStatement();
            //tatement.execute("Insert into cards (type, picture_index) values('STORM', 0);");

            for (Card card : cards) {
                /*preparedStatement.setString(1, String.valueOf(card.getType()));
                preparedStatement.setString(2, card.getSecondShipType());
                preparedStatement.setInt(3, card.getPictureIndex());
                preparedStatement.setInt(4, card.getStormValue());
                if(card.getPlayerId() != null) {
                    preparedStatement.setInt(5, card.getPlayerId());
                }
                preparedStatement.executeUpdate();*/
                String sqlStatement = String.format(baseStatement, card.getType().name(), card.getPictureIndex());
                statement.execute(sqlStatement);

            }




        } catch (Exception e) {
            e.printStackTrace();

        }
        List<Card> cardsFromDB = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game",
                    "root", "toor"); // user
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from cards;");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String secondShipType = resultSet.getString("second_ship_type");
                int pictureIndex = resultSet.getInt("picture_index");
                int stormVal = resultSet.getInt("storm_value");
                int playerIndex = resultSet.getInt("player_index");
                Card newCard = new Card(Card.Type.valueOf(type), secondShipType, pictureIndex, stormVal);
                newCard.setPlayerIndex(playerIndex);
                cardsFromDB.add(newCard);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(cardsFromDB);


    }


}
