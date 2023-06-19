package ship.game.client;


import com.badlogic.gdx.scenes.scene2d.Group;

public class CollectedShipGroup extends Group {
    public void addCard(CardActor actor) {
        addActor(actor);
        if (actor.getCardNum() < 4) {
            actor.setPosition((actor.getCardNum() - 1) * actor.getWidth(), actor.getHeight());
        } else {
            actor.setPosition((actor.getCardNum() - 1) % 3 * actor.getWidth(), 0);
        }
    }
}
