package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

    public StackGroup() {
        topCard = new CardActor(new Card(Card.Type.SHIP, "S1", 1, 1),
                new Texture(Gdx.files.internal("ships/ship1/S1-1.jpg")));
        bottomCard = new CardActor(new Card(Card.Type.SHIP, "S1", 1, 1),
                new Texture(Gdx.files.internal("ships/ship1/S1-2.jpg")));
        addActor(bottomCard);
        addActor(topCard);
       // setSize(CARD_WIDTH, CARD_HEIGHT);
//        StackClickListener stackClickListener = new StackClickListener(this);
//        addListener(stackClickListener);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                topCard.flipCard();
                topCard.addAction(Actions.moveBy(0, -200, 2f));
                topCard = bottomCard;
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
