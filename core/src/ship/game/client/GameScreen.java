package ship.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ship.game.server.Card;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    final ShipGame game;

    private OrthographicCamera camera;
    private Stage stage;
    private int playerCount = 4;

    public GameScreen(final ShipGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new ScreenViewport(camera), game.getBatch());
        Gdx.input.setInputProcessor(stage);

        List<Card> ships = new ArrayList<>();
        // TODO refactor, every ship - color should be initialized by a loop
        ships.add(new Card(Card.Type.SHIP, "S1", 1, 1));
        ships.add(new Card(Card.Type.SHIP, "S1", 2, 1));
        ships.add(new Card(Card.Type.SHIP, "S1", 3, 1));
        ships.add(new Card(Card.Type.SHIP, "S1", 4, 1));
        ships.add(new Card(Card.Type.SHIP, "S1", 5, 1));
        ships.add(new Card(Card.Type.SHIP, "S1", 6, 1));

//        CardActor cardActor = new CardActor(ships.get(0), new Texture(Gdx.files.internal("ships/ship1/S1-1.jpg")));
//        CardActor cardActor1 = new CardActor(ships.get(1), new Texture(Gdx.files.internal("ships/ship1/S1-2.jpg")));
//        CardActor cardActor2 = new CardActor(ships.get(2), new Texture(Gdx.files.internal("ships/ship1/S1-3.jpg")));
//        CardActor cardActor3 = new CardActor(ships.get(3), new Texture(Gdx.files.internal("ships/ship1/S1-4.jpg")));
//        CardActor cardActor4 = new CardActor(ships.get(4), new Texture(Gdx.files.internal("ships/ship1/S1-5.jpg")));
//        CardActor cardActor5 = new CardActor(ships.get(5), new Texture(Gdx.files.internal("ships/ship1/S1-6.jpg")));
//
//        CollectedShipGroup collectedShipGroup = new CollectedShipGroup();
//        collectedShipGroup.addCard(cardActor);
//        collectedShipGroup.addCard(cardActor1);
//        collectedShipGroup.addCard(cardActor2);
//        collectedShipGroup.addCard(cardActor3);
//        collectedShipGroup.addCard(cardActor4);
//        collectedShipGroup.addCard(cardActor5);
//        //stage.addActor(collectedShipGroup);
//        CounterGroup resourcesGroup = new CounterGroup();
//        CounterGroup tradeGroup = new CounterGroup();
//        resourcesGroup.addCounter(new CounterActor(Card.Type.COIN, game.getFont()));
//        resourcesGroup.addCounter(new CounterActor(Card.Type.CANNON, game.getFont()));
//        tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S2", game.getFont()));
//        tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S3", game.getFont()));
//        tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S4", game.getFont()));
        //resourcesGroup.setBounds(80, 200, 100, 250);
        //tradeGroup.setBounds(520, 200, 100, 350);
        //stage.addActor(resourcesGroup);
        //stage.addActor(tradeGroup);
        for (int i = 0; i < playerCount; i++) {
            CardActor cardActor = new CardActor(ships.get(0), new Texture(Gdx.files.internal("ships/ship1/S1-1.jpg")));
            CardActor cardActor1 = new CardActor(ships.get(1), new Texture(Gdx.files.internal("ships/ship1/S1-2.jpg")));
            CardActor cardActor2 = new CardActor(ships.get(2), new Texture(Gdx.files.internal("ships/ship1/S1-3.jpg")));
            CardActor cardActor3 = new CardActor(ships.get(3), new Texture(Gdx.files.internal("ships/ship1/S1-4.jpg")));
            CardActor cardActor4 = new CardActor(ships.get(4), new Texture(Gdx.files.internal("ships/ship1/S1-5.jpg")));
            CardActor cardActor5 = new CardActor(ships.get(5), new Texture(Gdx.files.internal("ships/ship1/S1-6.jpg")));

            CollectedShipGroup collectedShipGroup = new CollectedShipGroup();
            collectedShipGroup.addCard(cardActor);
            collectedShipGroup.addCard(cardActor1);
            collectedShipGroup.addCard(cardActor2);
            collectedShipGroup.addCard(cardActor3);
            collectedShipGroup.addCard(cardActor4);
            collectedShipGroup.addCard(cardActor5);
            //stage.addActor(collectedShipGroup);
            CounterGroup resourcesGroup = new CounterGroup();
            CounterGroup tradeGroup = new CounterGroup();
            resourcesGroup.addCounter(new CounterActor(Card.Type.COIN, game.getFont()));
            resourcesGroup.addCounter(new CounterActor(Card.Type.CANNON, game.getFont()));
            tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S2", game.getFont()));
            tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S3", game.getFont()));
            tradeGroup.addCounter(new CounterActor(Card.Type.SHIP, "S4", game.getFont()));
            PlayerGroup playerGroup = new PlayerGroup(collectedShipGroup, resourcesGroup, tradeGroup);
            //todo refactor -> kod wylicza pozycje
            if (i > 1) {
                playerGroup.setX(800);
            }
            if (i % 2 == 1) {
                playerGroup.setY(500);
            }
            stage.addActor(playerGroup);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();
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
