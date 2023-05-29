package ship.game.server;

import java.sql.*;

public class JDBC{
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ship_game", "root", "toor"); // user password to insert manually
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from players");

            while (resultSet.next()) {
                System.out.println("player number: " + resultSet.getString("playerNum"));
                System.out.println("coin: " + resultSet.getString("coin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
