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
    private int pictureIndex;
    private int stormValue;

    // private pole z wstawionym assetem jpg?

    public Card(Card.Type type, int pictureIndex, int stormValue) { // for storm, coin, cannon
        this.type = type;
        this.pictureIndex = pictureIndex;
        this.stormValue = stormValue;
    }

    public Card(Card.Type type, String secondShipType, int pictureIndex, int stormValue) { // for ship
        this.type = type;
        this.secondShipType = secondShipType;
        this.pictureIndex = pictureIndex;
        this.stormValue = stormValue;
    }

    public Type getType() {
        return type;
    }

    public String getSecondShipType() {
        return secondShipType;
    }

    public int getPictureIndex() {
        return pictureIndex;
    }

    public int getStormValue() {
        return stormValue;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", secondShipType='" + secondShipType + '\'' +
                ", num=" + pictureIndex +
                ", value=" + stormValue +
                '}';
    }
}
