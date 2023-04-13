package ship.game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    public static void main(String[] arg) {

       /* Game game = new Game();
        System.out.println("ilość " + game.getAllCards().size());
        System.out.println(game.getAllCards());

        System.out.println("po tasowaniu");
        System.out.println(game.shuffle());*/

        //Player player1 = new Player();
        //player1.draw();

        Controller controller = new Controller();
        controller.putDrawnCardInOwnStack();



        // assert that w shuffled nie ma wylosowanej karty

    }

/*        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ShipGame");
        config.setWindowedMode(1800, 900);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new ShipGame(), config);*/

    }