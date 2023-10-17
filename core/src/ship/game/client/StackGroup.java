package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ship.game.server.Card;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

public class StackGroup extends Group {
    private CardActor topCard;
    private CardActor bottomCard;
    private ActiveCardGroup activeCardGroup;

    public StackGroup(ActiveCardGroup activeCardGroup) {
        this.activeCardGroup = activeCardGroup;
//        topCard = new CardActor(new Card(Card.Type.SHIP, "S2", 3, 1),       //3
//                new Texture(Gdx.files.internal("ships/ship2/S2-3.jpg")));
        //topCard = new CardActor(new Card(Card.Type.COIN, 1, 1), new Texture(Gdx.files.internal("coin.jpg")));     //2
        //topCard = new CardActor(new Card(Card.Type.CANNON, 1, 1), new Texture(Gdx.files.internal("counter/cannon.png")));  //1
        topCard = new CardActor(new Card(Card.Type.SHIP, "S1", 4, 1),     //1
                new Texture(Gdx.files.internal("ships/ship1/S1-4.jpg")));
        bottomCard = new CardActor(new Card(Card.Type.SHIP, "S1", 5, 1),        //1
                new Texture(Gdx.files.internal("ships/ship1/S1-5.jpg")));
        addActor(bottomCard);
        addActor(topCard);
       // setSize(CARD_WIDTH, CARD_HEIGHT);
//        StackClickListener stackClickListener = new StackClickListener(this);
//        addListener(stackClickListener);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                CardActor touchedCard = topCard;
                topCard = bottomCard;
                //todo poszukac lepszego sposobu na sprawdzenei jaka karta zostala kliknieta
//                if (event.getRelatedActor() != touchedCard) {
//                    return true;
//                }
                touchedCard.flipCard();
                Action moveAction = Actions.moveBy(0, activeCardGroup.getY() - getY(), 2f);
                Action changeGroup = Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        activeCardGroup.addActor(touchedCard);
                        System.out.println("Zmiana grupy");
                        touchedCard.setPosition(0, 0);
                    }
                });
                touchedCard.addAction(Actions.sequence(moveAction, changeGroup));
                System.out.println("kliknieto w grupe");
                return true;
            }
        });
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture texture = new Texture(Gdx.files.internal("ships/ship1/S1-3.jpg"));
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation(), 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);

        batch.setColor(Color.WHITE);
        super.draw(batch, parentAlpha);
    }

    public CardActor getTopCard() {
        return topCard;
    }
}
