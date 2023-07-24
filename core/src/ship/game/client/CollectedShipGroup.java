package ship.game.client;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

public class CollectedShipGroup extends Group {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public CollectedShipGroup() {
        setWidth(3 * CARD_WIDTH);
        setHeight(2 * CARD_HEIGHT);
    }

    public void addCard(CardActor actor) {
        addActor(actor);
        if (actor.getCardNum() < 4) {
            actor.setPosition((actor.getCardNum() - 1) * actor.getWidth(), actor.getHeight());
        } else {
            actor.setPosition((actor.getCardNum() - 1) % 3 * actor.getWidth(), 0);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        Color color = getColor();
//        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        batch.begin();
//        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
//                getWidth(), getHeight(), getScaleX(), getScaleY(),
//                getRotation(), 0, 0, texture.getWidth(), texture.getHeight(),
//                false, false);

        //batch.setColor(Color.WHITE);
    }


}
