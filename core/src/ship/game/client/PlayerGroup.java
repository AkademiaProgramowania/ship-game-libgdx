package ship.game.client;

import com.badlogic.gdx.scenes.scene2d.Group;

public class PlayerGroup extends Group {
    private CollectedShipGroup collectedShipGroup;
    private CounterGroup resourcesGroup, tradeGroup;

    public PlayerGroup(CollectedShipGroup collectedShipGroup, CounterGroup resourcesGroup, CounterGroup tradeGroup) {
        this.collectedShipGroup = collectedShipGroup;
        this.resourcesGroup = resourcesGroup;
        this.tradeGroup = tradeGroup;
        addActor(collectedShipGroup);
        addActor(resourcesGroup);
        addActor(tradeGroup);

        setGroupsPlacement();
    }

    private void setGroupsPlacement() {
        resourcesGroup.setBounds(getX(), getY(), collectedShipGroup.getHeight() / 3, collectedShipGroup.getHeight());
        collectedShipGroup.setPosition(resourcesGroup.getWidth() + getX(), getY());
        tradeGroup.setBounds(collectedShipGroup.getX() + collectedShipGroup.getWidth(), getY(),
                resourcesGroup.getWidth(), resourcesGroup.getHeight());
        resourcesGroup.setCountersWithinGroup();
        tradeGroup.setCountersWithinGroup();
    }

}
