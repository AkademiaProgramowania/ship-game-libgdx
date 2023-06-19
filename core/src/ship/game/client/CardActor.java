package ship.game.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ship.game.server.Card;

public class CardActor extends Actor {
    private Card card;
    private Texture texture;

    public CardActor(Card card, Texture texture) {
        this.card = card;
        this.texture = texture;
        setBounds(0, 0, 100, 150);
    }

    public int getCardNum() {
        return card.getPictureIndex();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation(), 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);

        batch.setColor(Color.WHITE);
    }


}
