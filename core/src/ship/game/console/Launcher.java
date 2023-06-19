package ship.game.console;

import ship.game.server.Controller;
import ship.game.server.Game;
import ship.game.server.Player;

public class Launcher {

    public void play() {
        Game game = new Game();
        Player player1 = new Player();
        Player player2 = new Player();
        Controller controller = new Controller(game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        controller.play();

    }
}
