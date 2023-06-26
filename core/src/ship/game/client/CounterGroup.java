package ship.game.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;


public class CounterGroup extends Group {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void addCounter(CounterActor actor) {
        addActor(actor);
        //TODO zerowe wartosci z getterow
        actor.setPosition((getChildren().size - 1) * ((int) (0.1 * getWidth() + actor.getWidth())),
                (int) (0.4 * getHeight()));
        //System.out.println(getWidth());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        batch.begin();
    }
}
