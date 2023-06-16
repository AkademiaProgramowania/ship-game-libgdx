package ship.game.server;

public class Card {



    public enum Type {
        SHIP,
        STORM,
        COIN,
        CANNON
    }
    private final Card.Type type;
    private final int cardIndex;
    private String secondShipType; // Typy: "S1"/"S2"/"S3"/"S4" // all fields can be final
    private int pictureIndex;
    private int stormValue;
    private int owner;


    // private pole z wstawionym assetem jpg?

    public Card(Card.Type type, int cardIndex, int pictureIndex, int stormValue, int owner) { // for storm, coin, cannon
        this.type = type;
        this.cardIndex = cardIndex;
        this.pictureIndex = pictureIndex;
        this.stormValue = stormValue;
        this.owner = owner;
    }

    public Card(Card.Type type, int cardIndex, String secondShipType, int pictureIndex, int stormValue, int owner) { // for ship
        this.type = type;
        this.cardIndex = cardIndex;
        this.secondShipType = secondShipType;
        this.pictureIndex = pictureIndex;
        this.stormValue = stormValue;
        this.owner = owner;
    }

    public Type getType() {
        return type;
    }

    public int getCardIndex() {
        return cardIndex;
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

    public int getOwner() {
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", cardIndex=" + cardIndex +
                ", secondShipType='" + secondShipType + '\'' +
                ", pictureIndex=" + pictureIndex +
                ", stormValue=" + stormValue +
                ", owner=" + owner +
                '}';
    }
}
