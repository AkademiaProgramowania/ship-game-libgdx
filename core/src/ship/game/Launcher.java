package ship.game;

public class Launcher {

    public void play() {
        Game game = new Game();
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Controller controller = new Controller(game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        controller.play();

    }
}
