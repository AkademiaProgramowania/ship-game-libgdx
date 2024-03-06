package ship.game.client;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ship.game.server.Card;

public class ActiveCardGroup extends Group {
    private CardActor activeCard;
    private GameScreen gameScreen;

    public ActiveCardGroup(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickAction();
                return true;
            }
        });
    }

    private void clickAction(){
        //odłączenie od grupy
        gameScreen.getStage().addActor(activeCard);
        activeCard.setPosition(getX(), getY());

        gameScreen.getCurrentPlayerGroup().obtainCard(activeCard);
    }

    void obtainCard(CardActor cardActor, Group source){
        cardActor.flipCard();
        Action moveAction = Actions.moveBy(0, cardActor.getY() - source.getY(), 2f); //getY nie działa
        Action changeGroup = Actions.run(new Runnable() {
            @Override
            public void run() {
               addActor(cardActor);
                cardActor.setPosition(0, 0);
            }
        });
        cardActor.addAction(Actions.sequence(moveAction, changeGroup));
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
