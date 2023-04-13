package ship.game;

import java.util.List;

public class Card {

    public enum Type {
        SHIP,
        STORM,
        COIN,
        CANNON
    }

    private final Card.Type type;
    private String shipNum;
    private int num;

    // private pole z wstawionym assetem jpg?


    public Card(Card.Type type, int num) { // for storm, coin, cannon
        this.type = type;
        this.num = num;
    }

    public Card(Card.Type type, String shipNum, int num) { // for ship
        this.type = type;
        this.shipNum = shipNum;
        this.num = num;
    }

    public String getShipNum() {
        return shipNum;
    }

    public int getNum() {
        return num;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", shipNum='" + shipNum + '\'' +
                ", num=" + num +
                '}';
    }
}
