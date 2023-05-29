package ship.game.server;

public class Card {

    public enum Type {
        SHIP,
        STORM,
        COIN,
        CANNON
    }
    private final Card.Type type;
    private String secondShipType; // Typy: "S1"/"S2"/"S3"/"S4" // all fields can be final
    private int num;
    private int value;

    // private pole z wstawionym assetem jpg?

    public Card(Card.Type type, int num, int value) { // for storm, coin, cannon
        this.type = type;
        this.num = num;
        this.value = value;
    }

    public Card(Card.Type type, String secondShipType, int num, int value) { // for ship
        this.type = type;
        this.secondShipType = secondShipType;
        this.num = num;
        this.value = value;
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

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", secondShipType='" + secondShipType + '\'' +
                ", num=" + num +
                ", value=" + value +
                '}';
    }
}
