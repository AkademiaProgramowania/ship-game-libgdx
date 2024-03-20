package ship.game.client;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

public class CollectedCardGroup extends CardTransporterGroup {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public CollectedCardGroup() {
        setWidth(3 * CARD_WIDTH);
        setHeight(2 * CARD_HEIGHT);
    }

    boolean obtainCard(CardActor cardActor) {
        //todo czy karta jest zbierana
        boolean collected = true;
        if (collected) {
            return super.obtainCard(cardActor);
        } else {
            return false;
        }
    }

    @Override
    Vector2 getDestination(CardActor cardActor) {
        int x, y;
        if (cardActor.getCardNum() < 4) {
            x = (cardActor.getCardNum() - 1) * CARD_WIDTH;
            y = CARD_HEIGHT;
        } else {
            x = (cardActor.getCardNum() - 1) % 3 * CARD_WIDTH;
            y = 0;
        }
        return new Vector2(x, y);
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
