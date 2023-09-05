package ship.game.client;

import com.badlogic.gdx.scenes.scene2d.Group;

public class PlayerGroup extends Group {
    private CollectedCardGroup collectedCardGroup;
    private CounterGroup resourcesGroup, tradeGroup;

    public PlayerGroup(CollectedCardGroup collectedCardGroup, CounterGroup resourcesGroup, CounterGroup tradeGroup) {
        this.collectedCardGroup = collectedCardGroup;
        this.resourcesGroup = resourcesGroup;
        this.tradeGroup = tradeGroup;
        addActor(collectedCardGroup);
        addActor(resourcesGroup);
        addActor(tradeGroup);

        setGroupsPlacement();
    }

    private void setGroupsPlacement() {
        resourcesGroup.setBounds(getX(), getY(), collectedCardGroup.getHeight() / 3, collectedCardGroup.getHeight());
        collectedCardGroup.setPosition(resourcesGroup.getWidth() + getX(), getY());
        tradeGroup.setBounds(collectedCardGroup.getX() + collectedCardGroup.getWidth(), getY(),
                resourcesGroup.getWidth(), resourcesGroup.getHeight());
        resourcesGroup.setCountersWithinGroup();
        tradeGroup.setCountersWithinGroup();
    }

    public CollectedCardGroup getCollectedCardGroup() {
        return collectedCardGroup;
    }

    public CounterGroup getResourcesGroup() {
        return resourcesGroup;
    }

    public CounterGroup getTradeGroup() {
        return tradeGroup;
    }
}
