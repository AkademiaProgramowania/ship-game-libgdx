package ship.game.console;

import ship.game.server.*;

import java.util.List;

public class Launcher {

    public void play() {
        Repository repository = new JdbcRepository();

        Game game = new Game(repository);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Controller controller = new Controller(game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        controller.play();

    }
}
