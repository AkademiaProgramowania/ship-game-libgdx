package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ship.game.server.Card;

import static ship.game.client.GUIParams.CARD_HEIGHT;
import static ship.game.client.GUIParams.CARD_WIDTH;

public class CardActor extends Actor {
    private Card card;
    private Texture activeTexture;
    private static Texture cardBack = new Texture(Gdx.files.internal("storm1.jpg"));
    private Texture cardFace;

    public CardActor(Card card, Texture cardFace) {
        this.card = card;
        this.cardFace = cardFace;
        this.activeTexture = cardBack;
        setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
    }

    public int getCardNum() {
        return card.getPictureIndex();
    }

    public Card getCard() {
        return card;
    }

    public Card.Type getCardType() {
        return card.getType();
    }

    public void flipCard() {
        activeTexture = cardFace;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(activeTexture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation(), 0, 0, activeTexture.getWidth(), activeTexture.getHeight(),
                false, false);

        batch.setColor(Color.WHITE);
    }


}
