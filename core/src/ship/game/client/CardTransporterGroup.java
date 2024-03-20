package ship.game.client;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class CardTransporterGroup extends Group {
    boolean obtainCard(CardActor cardActor) {
        Vector2 stagePosition = cardActor.localToStageCoordinates(new Vector2(0 ,0));
        Vector2 destination = getDestination(cardActor);
        getStage().addActor(cardActor);
        cardActor.setPosition(stagePosition.x, stagePosition.y);

        Action moveAction = Actions.moveTo(getX() + destination.x, getY() + destination.y, 2f);
        Action changeGroup = Actions.run(new Runnable() {
            @Override
            public void run() {
                addActor(cardActor);
                cardActor.setPosition(destination.x, destination.y);
            }
        });
        cardActor.addAction(Actions.sequence(moveAction, changeGroup));
        return true;
    }

    Vector2 getDestination(CardActor cardActor) {
        return new Vector2(0, 0);
    }
}
