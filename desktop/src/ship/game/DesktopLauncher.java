package ship.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ship.game.client.ShipGame;
import ship.game.console.Launcher;

import static ship.game.client.GUIParams.HEIGHT;
import static ship.game.client.GUIParams.WIDTH;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    public static void main(String[] arg) {
        Launcher launcher = new Launcher();
        launcher.play();
        /*Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ShipGame");
        config.setWindowedMode(WIDTH, HEIGHT);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new ShipGame(), config);*/
        }
    }