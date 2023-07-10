package ship.game.server;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "collected_ship_type")
    private String collectedShipType;

    @Column(name = "player_index")
    private int playerIndex;

    @Column(name = "stack_size")
    private int stackSize;

    public PlayerEntity(int id, String collectedShipType, int playerIndex, int stackSize) {
        this.id = id;
        this.collectedShipType = collectedShipType;
        this.playerIndex = playerIndex;
        this.stackSize = stackSize;
    }

    public PlayerEntity(String collectedShipType, int playerIndex, int stackSize) {
        this.collectedShipType = collectedShipType;
        this.playerIndex = playerIndex;
        this.stackSize = stackSize;
    }

    public PlayerEntity() {
    }


    public int getId() {
        return id;
    }

    public String getCollectedShipType() {
        return collectedShipType;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEntity that = (PlayerEntity) o;
        return id == that.id && playerIndex == that.playerIndex && stackSize == that.stackSize && Objects.equals(collectedShipType, that.collectedShipType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, collectedShipType, playerIndex, stackSize);
    }


    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", collectedShipType='" + collectedShipType + '\'' +
                ", playerIndex=" + playerIndex +
                ", stackSize=" + stackSize +
                '}';
    }
}
