package ship.game.client;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class CardTransporterGroup extends Group {
    boolean obtainCard(CardActor cardActor) {
        Vector2 stagePosition = cardActor.localToStageCoordinates(new Vector2(0 ,0));
        getStage().addActor(cardActor);
        cardActor.setPosition(stagePosition.x, stagePosition.y);

        Action moveAction = Actions.moveTo(getX(), getY(), 2f);
        Action changeGroup = Actions.run(new Runnable() {
            @Override
            public void run() {
                addActor(cardActor);
                cardActor.setPosition(0, 0);
            }
        });
        cardActor.addAction(Actions.sequence(moveAction, changeGroup));
        return true;
    }
}
