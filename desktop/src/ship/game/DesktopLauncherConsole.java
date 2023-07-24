package ship.game;

import ship.game.console.Launcher;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncherConsole {

    public static void main(String[] arg) {
        Launcher launcher = new Launcher();
        launcher.play();
    }
}