package ship.game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    public static void main(String[] arg) {
        Launcher launcher = new Launcher();
        launcher.play();




    }

/*        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ShipGame");
        config.setWindowedMode(1800, 900);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new ShipGame(), config);*/

    }