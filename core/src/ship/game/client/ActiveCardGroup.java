package ship.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ship.game.server.Card;

import java.util.List;

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
                float targetX = gameScreen.getCurrentPlayerGroup().findTargetX(activeCard.getCard());
                float targetY = gameScreen.getCurrentPlayerGroup().findTargetY(activeCard.getCard());
                gameScreen.getStage().addActor(activeCard);
                activeCard.setPosition(getX(), getY());
                Action moveAction = Actions.moveTo(targetX, targetY, 1f);
                Action fadeAction = Actions.fadeOut(1f);

                if (activeCard.getCardType() == Card.Type.COIN || activeCard.getCardType() == Card.Type.CANNON) {
                    activeCard.addAction(Actions.sequence(moveAction, fadeAction));
                } else if (activeCard.getCardType() == Card.Type.SHIP && isCollected()) {
                    activeCard.addAction(moveAction);
                } else if (activeCard.getCardType() == Card.Type.SHIP && !isCollected()) {

                }
                //todo znikniecie + podzial na zbierany statek i statki na handel -> wyviagnac statek z
                return true;
            }
        });
    }

    private boolean isCollected() {
        return false;
    }

//    private float findTargetX(Card.Type type) {
//        float targetX = 0;
//        Card card = activeCard.getCard();
//        PlayerGroup playerGroup = gameScreen.getCurrentPlayerGroup();
//        List<CounterActor> counters = playerGroup.getResourcesCounters();
//
//        if (type == Card.Type.SHIP && isCollected()) {
//            int shipIndex = card.getPictureIndex() - 1;
//            System.out.println("player group X: " + playerGroup.getCollectedCardGroup().getX());
//            targetX = playerGroup.getCollectedCardGroup().getX() + (shipIndex % 3) * CARD_WIDTH + playerGroup.getX();
//            System.out.println(targetX);
//        } else if (type == Card.Type.CANNON || type == Card.Type.COIN) {
//            for (CounterActor counter : counters) {
//                if (counter.getType() == type) {
//                    targetX = counter.getX();
//                }
//            }
//        }
//        return targetX;
//    }
//
//    private float findTargetY(Card.Type type) {
//        //todo mozliwe ze te logike mozna napisac w klasie playergroup
//        Card card = activeCard.getCard();
//        PlayerGroup playerGroup = gameScreen.getCurrentPlayerGroup();
//        List<CounterActor> counters = playerGroup.getResourcesCounters();
//        float targetY = playerGroup.getY() + playerGroup.getCollectedCardGroup().getY();
//
//        if (type == Card.Type.SHIP && isCollected()) {
//            int shipIndex = card.getPictureIndex() - 1;
//            if (shipIndex < 3) {
//                targetY += CARD_HEIGHT;
//            }
//        } else if (type == Card.Type.CANNON || type == Card.Type.COIN) {
//            for (CounterActor counter : counters) {
//                if (counter.getType() == type) {
//                    targetY += counter.getY();
//                }
//            }
//        } else if (type == Card.Type.SHIP && !isCollected()) {
//
//        }
//        System.out.println("targetY: " + targetY);
//        return targetY;
//    }

    @Override
    public void addActor(Actor actor) {
        activeCard = (CardActor) actor;
        super.addActor(actor);
    }
}
