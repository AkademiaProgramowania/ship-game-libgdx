package ship.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final ShipGame game;

    private OrthographicCamera camera;

    public GameScreen(final ShipGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
