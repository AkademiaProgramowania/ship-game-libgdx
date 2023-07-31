package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
        addActor(topCard);
        addActor(bottomCard);
        setSize(CARD_WIDTH, CARD_HEIGHT);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Click stack group");
                return true;
            }
        });
    }
}
