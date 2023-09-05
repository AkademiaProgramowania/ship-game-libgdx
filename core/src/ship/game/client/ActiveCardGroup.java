package ship.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ship.game.server.Card;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

public class ActiveCardGroup extends Group {
    private CardActor activeCard;
    private GameScreen gameScreen;

    public ActiveCardGroup(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Grupa aktywna");
                float targetX = findTargetX(activeCard.getCard().getType());
                float targetY = findTargetY(activeCard.getCard().getType());
                gameScreen.getStage().addActor(activeCard);
                activeCard.setPosition(getX(), getY());
                activeCard.addAction(Actions.moveTo(targetX, targetY, 2f));
                return true;
            }
        });
    }

    private float findTargetX(Card.Type type) {
        float targetX = 0;
        Card card = activeCard.getCard();
        PlayerGroup playerGroup = gameScreen.getCurrentPlayerGroup();

        if (type == Card.Type.SHIP) {
            int shipIndex = card.getPictureIndex() - 1;
            System.out.println("player group X: " + playerGroup.getCollectedCardGroup().getX());
            targetX = playerGroup.getCollectedCardGroup().getX() + (shipIndex % 3) * CARD_WIDTH + playerGroup.getX();
            System.out.println(targetX);
        } else if (type == Card.Type.COIN) {

        } else if (type == Card.Type.CANNON) {

        }
        return targetX;
    }

    private float findTargetY(Card.Type type) {
        Card card = activeCard.getCard();
        PlayerGroup playerGroup = gameScreen.getCurrentPlayerGroup();
        float targetY = playerGroup.getY() + playerGroup.getCollectedCardGroup().getY();

        if (type == Card.Type.SHIP) {
            int shipIndex = card.getPictureIndex() - 1;
            if (shipIndex < 3) {
                targetY += CARD_HEIGHT;
            }
        }
        System.out.println("targetY: " + targetY);
        return targetY;
    }

    @Override
    public void addActor(Actor actor) {
        activeCard = (CardActor) actor;
        super.addActor(actor);
    }
}
