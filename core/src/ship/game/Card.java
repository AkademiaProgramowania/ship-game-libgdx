package ship.game;

public class Card {

    public enum Type {
        SHIP,
        STORM,
        COIN,
        CANNON
    }

    private final Card.Type type;
    private String secondShipType; // Typy: "S1"/"S2"/"S3"/"S4"
    private int num;

    // private pole z wstawionym assetem jpg?


    public Card(Card.Type type, int num) { // for storm, coin, cannon
        this.type = type;
        this.num = num;
    }

    public Card(Card.Type type, String secondShipType, int num) { // for ship
        this.type = type;
        this.secondShipType = secondShipType;
        this.num = num;
    }


    public Type getType() {
        return type;
    }

    public String getSecondShipType() {
        return secondShipType;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", shipNum='" + secondShipType + '\'' +
                ", num=" + num +
                '}';
    }
}
