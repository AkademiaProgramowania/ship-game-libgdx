package ship.game.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;


public class CounterGroup extends CardTransporterGroup {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private List<CounterActor> counters = new ArrayList<>();

    public void addCounter(CounterActor actor) {
        addActor(actor);
        counters.add(actor);
       // actor.setPosition(getX(), (getChildren().size - 1) * ((int) (0.1 * getHeight() + actor.getHeight()))); //todo  sabotuje nam te countery po prawej
        //System.out.println(getWidth());
    }

    public void setCountersWithinGroup() {
        //todo refactor
        int spacingY, spacingX;
        Actor currentActor;
        for (int i = 0; i < getChildren().size; i++) {
            currentActor = getChildren().get(i);
            currentActor.setHeight(getHeight() / 4);
            currentActor.setWidth(getHeight() / 4);
            spacingX = (int) (getWidth() - currentActor.getWidth()) / 2;
            spacingY = (int) ((getHeight() - getChildren().size * currentActor.getHeight()) / (getChildren().size + 1));
            currentActor.setPosition(spacingX, getY() + i * currentActor.getHeight() + (i + 1) * spacingY);
        }
    }

    boolean obtainCard(CardActor cardActor) {
        for (CardTransporterGroup counter : counters) {
            if(counter.obtainCard(cardActor)) {
                return true;
            }
        }
        return false;
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

    public List<CounterActor> getCounters() {
        return counters;
    }
}
