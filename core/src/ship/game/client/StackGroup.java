package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import ship.game.server.Card;

public class StackGroup extends Group {
    private CardActor topCard;
    private CardActor bottomCard;

    public StackGroup() {
        topCard = new CardActor(new Card(Card.Type.SHIP, "S1", 1, 1),
                new Texture(Gdx.files.internal("ships/ship1/S1-1.jpg")));
        bottomCard = new CardActor(new Card(Card.Type.SHIP, "S1", 1, 1),
                new Texture(Gdx.files.internal("ships/ship1/S1-2.jpg")));
        addActor(topCard);
        addActor(bottomCard);
    }
}
