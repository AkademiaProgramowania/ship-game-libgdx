package ship.game.client;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ship.game.server.Card;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Objects;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

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
        //todo centruje countery przy tworzeniu playergroup, a dodajac je pozniej (tradecountery) nie bedzie sie aktualizowac
        resourcesGroup.setCountersWithinGroup();
        tradeGroup.setCountersWithinGroup();
    }

    public void obtainCard(CardActor cardActor) {
        //gdzie ma leciec
        float targetX = findTargetX(cardActor.getCard());
        float targetY = findTargetY(cardActor.getCard());

        //przygotowanie animacji
        Action moveAction = Actions.moveTo(targetX, targetY, 1f);
        Action fadeAction = Actions.fadeOut(1f);

        //rodzaj animacji zaleznie od miejsca docelowego
        if (cardActor.getCardType() == Card.Type.COIN || cardActor.getCardType() == Card.Type.CANNON) {  //coin / cannon
            cardActor.addAction(Actions.sequence(moveAction, fadeAction));
            updateCounters(cardActor.getCard()); //update counter
        } else if (cardActor.getCardType() == Card.Type.SHIP) {
            if (isCollected()) {
                cardActor.addAction(moveAction);
            } else {
                cardActor.addAction(Actions.sequence(moveAction, fadeAction));
            }
        }
    }


    private CounterGroup prepareCounter(CardActor cardActor) {   //
        return null;
    }


    public float findTargetX(Card card) {
        float targetX = 0;

        switch (card.getType()) {
            case CANNON:
            case COIN:
                for (CounterActor counter : getResourcesCounters()) {
                    if (counter.getType() == card.getType()) {
                        targetX = counter.getX();
                    }
                }
                break;
            case SHIP:
                if (isCollected()) {
                    int shipIndex = card.getPictureIndex() - 1;
                    targetX = collectedCardGroup.getX() + (shipIndex % 3) * CARD_WIDTH;
                } else {
                    targetX += tradeGroup.getX();
                }
                break;
            case STORM:
                throw new NotImplementedException();
        }

        return targetX + getX();
    }

    public float findTargetY(Card card) {
        float targetY = 0;

        switch (card.getType()) {
            case CANNON:
            case COIN:
                for (CounterActor counter : getResourcesCounters()) { // same
                    if (counter.getType() == card.getType()) {
                        targetY += counter.getY();
                    }
                }
                break;
            case SHIP:
                if (isCollected()) { // inaczej
                    int shipIndex = card.getPictureIndex() - 1;
                    if (shipIndex < 3) {
                        targetY += CARD_HEIGHT;
                    }
                } else {
                    updateCounters(card); //?????!!!!!! pilniejesze, jak tego sie pozbedziemy to szukamy miejsca dla tych findów
                    moveShipCounters(); // updatuje countery
                    for (CounterActor shipCounter : getTradeCounters()) {
                        if (Objects.equals(shipCounter.getShipType(), card.getSecondShipType())) {
                            targetY += shipCounter.getY();
                        }
                    }
                }
                break;
            case STORM:
                throw new NotImplementedException();
        }

        return targetY + getY() + collectedCardGroup.getY();
    }

    private boolean isCollected() {
        return false;
    }

    public void updateCounters(Card card) {
        //todo bitmapp font jeden na apke
        if (card.getType() == Card.Type.COIN || card.getType() == Card.Type.CANNON) {
            for (CounterActor resourceCounter : resourcesGroup.getCounters()) {
                if (Objects.equals(card.getType(), resourceCounter.getType())) {
                    resourceCounter.increaseAmount();
                    return;
                }
            }
        } else if (card.getType() == Card.Type.SHIP) {
            for (CounterActor shipCounter : tradeGroup.getCounters()) {
                if (Objects.equals(shipCounter.getShipType(), card.getSecondShipType())) {
                    shipCounter.increaseAmount();
                    return;
                }
            }
        }
        tradeGroup.addCounter(new CounterActor(card.getType(), card.getSecondShipType(), new BitmapFont()));
    }

    public void moveShipCounters() {
        tradeGroup.setCountersWithinGroup();
    }

    public List<CounterActor> getResourcesCounters() {
        return resourcesGroup.getCounters();
    }

    public List<CounterActor> getTradeCounters() {
        return tradeGroup.getCounters();
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
