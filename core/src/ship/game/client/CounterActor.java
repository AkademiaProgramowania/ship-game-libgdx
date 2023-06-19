package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ship.game.server.Card;

public class CounterActor extends Actor {
    private Texture texture;
    private Card.Type type;
    private String shipType;
    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont font;
    private int amount = 0;

    public CounterActor(Card.Type type, BitmapFont font) {
        this.type = type;
        this.font = font;
        setUpTexture();
        setBounds(0, 100, 100, 100);
    }

    public CounterActor(Card.Type type, String shipType) {
        this.type = type;
        this.shipType = shipType;
        amount = 1;
        setUpTexture();
        setBounds(0, 0, 100, 100);
    }

    private void setUpTexture() {
        if (type == Card.Type.COIN) {
            texture = new Texture(Gdx.files.internal("counter/coin.png"));
        } else if (type == Card.Type.CANNON) {
            texture = new Texture(Gdx.files.internal("counter/cannon.png"));
        }  else if (type == Card.Type.SHIP) {
            texture = new Texture(Gdx.files.internal("counter/ship.png"));
        } else {
            throw new IllegalStateException("Unexpected card type!");
        }
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
        layout.setText(font, amount + "");
        font.setColor(Color.WHITE);
        float cardCenterX = getX() + getWidth() / 2 - layout.width / 2;
        //float cardCenterY = getY() + getHeight() / 2 - layout.height / 2;
        font.draw(batch, layout, cardCenterX, getY() + font.getLineHeight());
    }
}
