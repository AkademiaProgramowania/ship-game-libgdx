package ship.game.server;

public class Card {

    public enum Type {
        SHIP,
        STORM,
        COIN,
        CANNON
    }

    private final Card.Type type;
    private String secondShipType; // Types: "S1"/"S2"/"S3"/"S4"
    private int pictureIndex;
    private int stormValue;
    private Integer playerIndex;

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

    public Integer getPlayerIndex() {
        return playerIndex;
    }
    public void setPlayerIndex(Integer playerIndex) {
        this.playerIndex = playerIndex;
    }
    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", secondShipType='" + secondShipType + '\'' +
                ", pictureIndex=" + pictureIndex +
                ", stormValue=" + stormValue +
                ", playerIndex=" + playerIndex +
                '}';
    }
}
