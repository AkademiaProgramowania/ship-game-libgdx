package ship.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.List;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    public static void main(String[] arg) {

        Game game = new Game();
        System.out.println("all cards, pierwotna kolejność, 55");
        System.out.println(game.getAllCards().toString());

        System.out.println("all cards, po tasowaniu, 55");
        System.out.println(game.shuffle().toString());

    }

/*        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ShipGame");
        config.setWindowedMode(1800, 900);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new ShipGame(), config);*/

    }