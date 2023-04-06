package ship.game;

import java.util.List;

public class Card {
    private String type; // ship, storm, cannon, coin
    private int num;

    // private pole z wstawionym assetem jpg?


    public Card(String type, int num) {
        this.type = type;
        this.num = num;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type='" + type + '\'' +
                ", num=" + num +
                '}';
    }
}
